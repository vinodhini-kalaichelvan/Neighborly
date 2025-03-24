package com.dalhousie.Neighbourly.authentication.service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import com.dalhousie.Neighbourly.user.entity.UserType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dalhousie.Neighbourly.authentication.entity.Otp;
import com.dalhousie.Neighbourly.authentication.entity.PasswordReset;
import com.dalhousie.Neighbourly.authentication.jwt.JwtService;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.OtpVerificationRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.responseEntity.PasswordResetTokenResponse;
import com.dalhousie.Neighbourly.authentication.service.OtpServiceImpl.TokenExpiredException;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ResetTokenService resetTokenService;

    @Transactional
    @Override
    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        if (userService.isUserPresent(registerRequest.getEmail())) {
            throw new RuntimeException("provided user already exists");
        }

        String name = registerRequest.getName();
        String email = registerRequest.getEmail();
        String rawPassword = registerRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        var user = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .userType(UserType.USER)
                .build();

        userService.saveUser(user);
        log.info("user entered");

        Otp otp = otpService.generateOtp(user.getId());
        prepareAndDispatchOtpMail(otp.getOtp(), user.getEmail());
        return AuthenticationResponse.builder()
                .token(null)
                .build();
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticateRequest authenticateRequest) {

        if (!userService.isUserPresent(authenticateRequest.getEmail())) {
            throw new UsernameNotFoundException("User not found with email: " + authenticateRequest.getEmail());
        }

        String email = authenticateRequest.getEmail();
        String password = authenticateRequest.getPassword();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authToken);

        var user = userService.findUserByEmail(email).get();

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify before logging in.");
        }

        var jwtToken = jwtService.generateToken(user, user.isEmailVerified());

        AuthenticationResponse.AuthenticationResponseBuilder responseBuilder =
                AuthenticationResponse.builder();

        responseBuilder.token(jwtToken);
        responseBuilder.userType(user.getUserType().name());
        responseBuilder.userId(user.getId());
        responseBuilder.neighbourhoodId(user.getNeighbourhood_id());

        return responseBuilder.build();
    }

    @Override
    @Transactional
    public void resendOtp(String email) {
        Optional<User> user = userService.findUserByEmail(email);
        Otp otp = otpService.resendOtp(user.get().getId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found with ID: " + user.get().getId());
        }
        prepareAndDispatchOtpMail(otp.getOtp(), user.get().getEmail());
    }



    @Override
    public AuthenticationResponse verifyOtp(OtpVerificationRequest otpVerificationRequest) {
        Optional<Otp> otpOptional = otpService.findByOtp(otpVerificationRequest.getOtp());
        if (otpOptional.isEmpty()) {
            throw new TokenExpiredException("Invalid OTP. Please try again.");
        }
        Otp otp = otpOptional.get();
        if (!otpService.isOtpValid(otp)) {
            throw new TokenExpiredException("OTP has expired. Please request a new one.");
        }

        Supplier<RuntimeException> exceptionSupplier =
                () -> new RuntimeException("User not found with ID: " + otp.getUserId());

        User user = userService.findUserById(otp.getUserId())
                .orElseThrow(exceptionSupplier);

        user.setEmailVerified(true);
        //userService.saveUser(user);

        String jwtToken = jwtService.generateToken(user, user.isEmailVerified());

        otpService.deleteOtp(otp);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void prepareAndDispatchOtpMail(String otp, String mail) {

        String subject = "Verify Your Email";
        String line1 = "<p>Hello,</p>";
        String line2 = "<p>Your OTP for email verification is:</p>";
        String line3 = "<h2>" + otp + "</h2>";
        String line4 = "<p>This OTP is valid for 5 minutes.</p>";

        String content = line1 + line2 + line3 + line4;

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

    @Override
    public void resetPassword(String email, String password, String token) {

        Supplier<RuntimeException> userNotFound =
                () -> new RuntimeException("User not found with email: " + email);

        User user = userService.findUserByEmail(email)
                .orElseThrow(userNotFound);

        if(!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify before resetting password.");
        }

        Supplier<RuntimeException> resetNotFound =
                () -> new RuntimeException("User did not initiate a reset password request.");

        PasswordReset passwordReset = resetTokenService.findByUserId(user.getId())
                .orElseThrow(resetNotFound);

        if (!Objects.equals(token, passwordReset.getToken())) {
            throw new RuntimeException("Failed to authenticate token. Please request to reset your password again.");
        }

        if (!resetTokenService.isTokenValid(passwordReset)) {
            throw new RuntimeException("Token expired. Please request to reset your password again.");
        }

        String newPassword = passwordEncoder.encode(password);
        userService.updatePassword(email, newPassword);

        resetTokenService.deleteResetPasswordToken(passwordReset);
    }

    @Override
    public PasswordResetTokenResponse forgotPassword(String email, String resetUrl) {
       String resetToken = "";
        try {
            Supplier<UsernameNotFoundException> exceptionSupplier =
                    () -> new UsernameNotFoundException("User not found with email: " + email);

            User user = userService.findUserByEmail(email)
                    .orElseThrow(exceptionSupplier);

            resetToken = resetTokenService.createResetPasswordToken(user.getId()).getToken();
            log.info("Generated token: {}", resetToken);

            String resetPasswordLink = resetUrl + "?email=" + email;
            log.info("Reset password link: {}", resetPasswordLink);

            prepareAndDispatchResetPwdLink(resetPasswordLink,email);
        } catch (Exception e) {
            log.error("Error in forgotPassword: {}", e.getMessage());
            throw e;  // rethrow to handle at the controller level
        }
        return PasswordResetTokenResponse.builder().token(resetToken).build();
    }

    private void prepareAndDispatchResetPwdLink(String resetPasswordLink, String email) {
        String subject = "Reset Your Password";

        String line1 = "<p>Hello,</p>";
        String line2 = "<p>You have requested to reset your password. Please click the link below to create a new password. "
                + "This link is valid for only 5 minutes for your security.</p>";
        String line3 = "<p>If you did not request this change, please ignore this email.</p>";
        String line4 = "<p>Click the link below to reset your password:</p>";
        String line5 = "<p><a href=\"" + resetPasswordLink + "\">Reset My Password</a></p>";
        String line6 = "<p>For your safety, please do not share this link with anyone.</p>";
        String line7 = "<p>Thank you!</p>";

        String content = line1 + line2 + line3 + line4 + line5 + line6 + line7;

        dispatchEmail(subject, content, email);
    }


    private static final int LOCAL_ENV_PORT = 3000;

    public String getURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");

        try {
            java.net.URL oldURL = new java.net.URL(siteURL);

            // URL for the local environment
            if ("localhost".equalsIgnoreCase(oldURL.getHost())) {
                return new java.net.URL(
                        oldURL.getProtocol(),
                        oldURL.getHost(),
                        LOCAL_ENV_PORT,
                        oldURL.getFile()
                ).toString();
            }

            // URL for the production environment
            return new java.net.URL(
                    oldURL.getProtocol(),
                    oldURL.getHost(),
                    oldURL.getFile()
            ).toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to construct the correct URL", e);
        }
    }


}
