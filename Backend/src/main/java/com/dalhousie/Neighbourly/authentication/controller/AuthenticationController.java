package com.dalhousie.Neighbourly.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.service.AuthenticationService;
import com.dalhousie.Neighbourly.util.CustomResponseBody;

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
            CustomResponseBody<AuthenticationResponse> responseBody =new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS,authenticationResponse,"user registered successfully, verify email");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }  catch (Exception e) {
            log.error("Unexpected error during user registration: {}", e.getMessage());
            CustomResponseBody<AuthenticationResponse> responseBody = new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "Something went wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<CustomResponseBody<AuthenticationResponse>> login(@Valid @RequestBody AuthenticateRequest authenticateRequest) {
        try {
            log.info("Authenticating user: {}", authenticateRequest.getEmail());
            AuthenticationResponse authenticationResponse = authenticationService.authenticateUser(authenticateRequest);
            CustomResponseBody<AuthenticationResponse> responseBody = new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, authenticationResponse, "user login successfully");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody); // Changed status to OK
        } catch (RuntimeException e) {
            log.error("Authentication failed: {}", e.getMessage());
            CustomResponseBody<AuthenticationResponse> responseBody = new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            log.error("Unexpected error during user login: {}", e.getMessage());
            CustomResponseBody<AuthenticationResponse> responseBody = new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "Something went wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    
}
