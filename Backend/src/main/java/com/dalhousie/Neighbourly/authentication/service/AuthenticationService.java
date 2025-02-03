package com.dalhousie.Neighbourly.authentication.service;

import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest);  
} 
