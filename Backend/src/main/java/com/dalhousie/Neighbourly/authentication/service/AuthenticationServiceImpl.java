package com.dalhousie.Neighbourly.authentication.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dalhousie.Neighbourly.authentication.entity.Otp;
import com.dalhousie.Neighbourly.authentication.jwt.JwtService;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.OtpVerificationRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.service.OtpServiceImpl.TokenExpiredException;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

        private final UserService userService;
        private final OtpService otpService;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final JavaMailSender mailSender;
        private final AuthenticationManager authenticationManager;

        @Transactional
        @Override
        public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
                if (userService.isUserPresent(registerRequest.getEmail())) {
                        new RuntimeException("provided user is already exists");
                }
                var user = User.builder()
                                .name(registerRequest.getName())
                                .email(registerRequest.getEmail())
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                .build();
                userService.saveUser(user);
                log.info("user entered");
                Otp otp = otpService.generateOtp(user.getId());
                prepareAndDispatchMail(otp.getOtp(), user.getEmail());
                return AuthenticationResponse.builder()
                                .token(null)
                                .build();
        }

        @Override
        public AuthenticationResponse authenticateUser(AuthenticateRequest authenticateRequest) {

                if (!userService.isUserPresent(authenticateRequest.getEmail())) {
                        new UsernameNotFoundException("User not found with email: " + authenticateRequest.getEmail());
                }

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                authenticateRequest.getEmail(),
                                                authenticateRequest.getPassword()));
                var user = userService.findUserByEmail(authenticateRequest.getEmail()).get();

                if (!user.isEmailVerified()) {
                        throw new RuntimeException("Email not verified. Please verify before logging in.");
                }

                var jwtToken = jwtService.generateToken(user, user.isEmailVerified());
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        @Override
        public AuthenticationResponse verifyOtp(OtpVerificationRequest otpVarificationRequest) {
            Optional<Otp> otpOptional = otpService.findByOtp(otpVarificationRequest.getOtp());
            if (otpOptional.isEmpty()) {
                throw new TokenExpiredException("Invalid OTP. Please try again.");
            }
            Otp otp = otpOptional.get();
            if (!otpService.isOtpValid(otp)) {
                throw new TokenExpiredException("OTP has expired. Please request a new one.");
            }

            User user = userService.findUserById(otp.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + otp.getUserId()));
            user.setEmailVerified(true);
            userService.saveUser(user);

            String jwtToken = jwtService.generateToken(user, user.isEmailVerified());

            otpService.deleteOtp(otp);

            return AuthenticationResponse.builder().token(jwtToken).build();
        }

        private void prepareAndDispatchMail(String otp, String mail) {
                String subject = "Verify Your Email";
                String content = "<p>Hello,</p>"
                                + "<p>Your OTP for email verification is:</p>"
                                + "<h2>" + otp + "</h2>"
                                + "<p>This OTP is valid for 5 minutes.</p>";
                dispatchEmail(subject, content, mail);
        }

        private void dispatchEmail(String mailSubject, String mailBody, String recipientEmail) {
                try {
                        MimeMessage emailMessage = mailSender.createMimeMessage();
                        MimeMessageHelper emailHelper = new MimeMessageHelper(emailMessage, true);

                        emailHelper.setFrom("noreply@example.com", "Support Team");
                        emailHelper.setTo(recipientEmail);
                        emailHelper.setSubject(mailSubject);
                        emailHelper.setText(mailBody, true);

                        mailSender.send(emailMessage); 
                } catch (MessagingException | UnsupportedEncodingException ex) {
                        throw new RuntimeException("Error sending email: " + ex.getMessage(), ex);
                }
        }
}
