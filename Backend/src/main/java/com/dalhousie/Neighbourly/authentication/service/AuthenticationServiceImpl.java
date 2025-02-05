package com.dalhousie.Neighbourly.authentication.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dalhousie.Neighbourly.authentication.jwt.JwtService;
import com.dalhousie.Neighbourly.authentication.requestEntity.AuthenticateRequest;
import com.dalhousie.Neighbourly.authentication.requestEntity.RegisterRequest;
import com.dalhousie.Neighbourly.authentication.responseEntity.AuthenticationResponse;
import com.dalhousie.Neighbourly.user.resident.model.Resident;
import com.dalhousie.Neighbourly.user.resident.service.ResidentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

        private final ResidentService residentService;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Transactional
        @Override
        public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
            if(residentService.isResidentPresent(registerRequest.getEmail())){
                new RuntimeException("provided user is already exists");
            }
            var resident = Resident.builder()
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();
            residentService.saveResident(resident);
            log.info("user entered");
            return AuthenticationResponse.builder()
                    .token(null)
                    .build();
        }

        
        @Override
        public AuthenticationResponse authenticateUser(AuthenticateRequest authenticateRequest) {

            if(!residentService.isResidentPresent(authenticateRequest.getEmail())){
                new UsernameNotFoundException("User not found with email: "+authenticateRequest.getEmail());
            }

            authenticationManager.authenticate(

            new UsernamePasswordAuthenticationToken
                            (authenticateRequest.getEmail(),
                                    authenticateRequest.getPassword()));
            var user = residentService.findResidentByEmail(authenticateRequest.getEmail()).get();

            var jwtToken = jwtService.generateToken(user,true);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }

}
