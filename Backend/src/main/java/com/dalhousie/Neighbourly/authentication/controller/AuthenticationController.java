package com.dalhousie.Neighbourly.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.ForgotPasswordRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.OtpVerificationRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.ResendOtpRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.ResetPasswordRequest;

import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.responseEntity.PasswordResetTokenResponse;
import com.dalhousie.Neighbourly.authentication.service.AuthenticationService;
import com.dalhousie.Neighbourly.authentication.service.OtpServiceImpl.TokenExpiredException;
import com.dalhousie.Neighbourly.util.CustomResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/check")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public ResponseEntity<CustomResponseBody<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthenticationResponse authenticationResponse = authenticationService.registerUser(registerRequest);
            log.info("User registered successfully with email: {}", registerRequest.getEmail());

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "user registered successfully, verify email";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, authenticationResponse, message);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (RuntimeException e) {
            log.error("Error while registering user: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "User already exists. Please verify if not";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);

        }  catch (Exception e) {
            log.error("Unexpected error during user registration: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "Something went wrong";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<CustomResponseBody<AuthenticationResponse>> login(@Valid @RequestBody AuthenticateRequest authenticateRequest) {
        try {
            log.info("Authenticating user: {}", authenticateRequest.getEmail());

            AuthenticationResponse authenticationResponse = authenticationService.authenticateUser(authenticateRequest);

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "user login successfully";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, authenticationResponse, message);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);

        } catch (RuntimeException e) {
            log.error("Authentication failed: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "User not found";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

        } catch (Exception e) {
            log.error("Unexpected error during user login: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "Something went wrong";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<CustomResponseBody<AuthenticationResponse>> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest){
        try{
            AuthenticationResponse authenticationResponse = authenticationService.verifyOtp(otpVerificationRequest);

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "verified email successfully";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, authenticationResponse, message);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        }catch (TokenExpiredException e) {
            log.error("OTP expired: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = e.getMessage();
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "Something went wrong";
            CustomResponseBody<AuthenticationResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<CustomResponseBody<String>> resendOtp(@RequestBody ResendOtpRequest resentOtpRequest) {
        try {
            authenticationService.resendOtp(resentOtpRequest.getEmail());

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "otp resented successfully";
            CustomResponseBody<String> responseBody = new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        }catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = e.getMessage();
            CustomResponseBody<String> responseBody = new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

        @PostMapping("/forgotPassword")
    public ResponseEntity<CustomResponseBody<PasswordResetTokenResponse>> forgotPassword(HttpServletRequest servletRequest, @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            String resetUrl = authenticationService.getURL(servletRequest) + ":3000/resetPassword";
            log.info(resetUrl);

            String email = forgotPasswordRequest.getEmail();
            PasswordResetTokenResponse passwordResetTokenResponse = authenticationService.forgotPassword(email, resetUrl);

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "Reset link sent";
            CustomResponseBody<PasswordResetTokenResponse> responseBody =
                    new CustomResponseBody<>(result, passwordResetTokenResponse, message);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);

        } catch (RuntimeException e) {
            log.error("User not found: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "User not found";
            CustomResponseBody<PasswordResetTokenResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "Something went wrong";
            CustomResponseBody<PasswordResetTokenResponse> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }


    @PostMapping("/passwordReset")
    public ResponseEntity<CustomResponseBody<String>>resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try{
            String email = resetPasswordRequest.getEmail();
            String password = resetPasswordRequest.getPassword();
            String token = resetPasswordRequest.getToken();

            authenticationService.resetPassword(email, password, token);

            CustomResponseBody.Result result = CustomResponseBody.Result.SUCCESS;
            String message = "Password reset successfully";
            CustomResponseBody<String> responseBody =
                    new CustomResponseBody<>(result, message, message);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);

        }catch (TokenExpiredException e) {
            log.error("Token expired: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "The password reset token has expired. Please request a new one.";
            CustomResponseBody<String> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

        }catch ( RuntimeException e) {
            log.error("User not found: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "User not found";
            CustomResponseBody<String> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

        }catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());

            CustomResponseBody.Result result = CustomResponseBody.Result.FAILURE;
            String message = "Something went wrong";
            CustomResponseBody<String> responseBody =
                    new CustomResponseBody<>(result, null, message);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    
}
