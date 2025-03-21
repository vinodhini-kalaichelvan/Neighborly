package com.dalhousie.Neighbourly.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.time.Instant;
import java.util.Optional;
import com.dalhousie.Neighbourly.authentication.entity.Otp;
import com.dalhousie.Neighbourly.authentication.entity.PasswordReset;
import com.dalhousie.Neighbourly.authentication.jwt.JwtService;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.OtpVerificationRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.entity.UserType;
import com.dalhousie.Neighbourly.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mail.javamail.JavaMailSender;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private OtpService otpService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ResetTokenService resetTokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .userType(UserType.USER).isEmailVerified(false)
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password");
        when(userService.isUserPresent(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(mimeMessage);

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1);
            return user;
        }).when(userService).saveUser(any(User.class));

 
    Otp mockOtp = new Otp(1, "123456", Instant.now().plusSeconds(300), 1);
    when(otpService.generateOtp(1)).thenReturn(mockOtp);

    AuthenticationResponse response = authenticationService.registerUser(request);

    // Assertions to verify both token and otp are null
    assertNull(response.getToken(), "Token should be null");
    assertNotNull(mockOtp.getOtp(), "OTP should not be null");

    // Verify that OTP service was called
    verify(otpService, times(1)).generateOtp(anyInt());
    }
    

    @Test
    void testAuthenticateUser_Success() {
        AuthenticateRequest request = new AuthenticateRequest("test@example.com", "password");
        user.setEmailVerified(true);

        when(userService.isUserPresent(request.getEmail())).thenReturn(true);
        when(userService.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), anyBoolean())).thenReturn("jwtToken");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        AuthenticationResponse response = authenticationService.authenticateUser(request);
        assertNotNull(response.getToken());
    }

    @Test
    void testAuthenticateUser_EmailNotVerified() {
        
        AuthenticateRequest request = new AuthenticateRequest("test@example.com", "password");
        when(userService.isUserPresent(request.getEmail())).thenReturn(true);
        when(userService.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> 
            authenticationService.authenticateUser(request)
        );
        assertEquals("Email not verified. Please verify before logging in.", exception.getMessage());
    }

    @Test
    void testVerifyOtp_Success() {
        OtpVerificationRequest request = new OtpVerificationRequest("123456");
        Otp otp = new Otp(1, "123456", Instant.now().plusSeconds(300), 1);
        when(otpService.findByOtp(request.getOtp())).thenReturn(Optional.of(otp));
        when(otpService.isOtpValid(otp)).thenReturn(true);
        when(userService.findUserById(otp.getUserId())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), anyBoolean())).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.verifyOtp(request);
        assertNotNull(response.getToken());
    }

    @Test
    void testResetPassword_Success() {// Otp(1, "123456", Instant.now().plusSeconds(300), 1)
        PasswordReset resetToken = new PasswordReset(1, "resetToken", user.getId(), Instant.now().plusSeconds(500));
        when(userService.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        user.setEmailVerified(true);
        when(resetTokenService.findByUserId(user.getId())).thenReturn(Optional.of(resetToken));
        when(resetTokenService.isTokenValid(resetToken)).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("newEncodedPassword");

        authenticationService.resetPassword(user.getEmail(), "newPassword", "resetToken");

        verify(userService, times(1)).updatePassword(eq(user.getEmail()), any());
    }
}