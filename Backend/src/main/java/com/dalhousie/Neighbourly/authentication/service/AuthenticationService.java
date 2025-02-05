package com.dalhousie.Neighbourly.authentication.service;

import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
public interface AuthenticationService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest);  

    AuthenticationResponse authenticateUser(AuthenticateRequest authenticateRequest);

} 
