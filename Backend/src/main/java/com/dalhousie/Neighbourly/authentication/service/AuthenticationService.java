
package com.dalhousie.Neighbourly.authentication.service;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.OtpVerificationRequest;
public interface AuthenticationService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticateUser(AuthenticateRequest authenticateRequest);
    AuthenticationResponse verifyOtp(OtpVerificationRequest otpVarificationRequest);
} 