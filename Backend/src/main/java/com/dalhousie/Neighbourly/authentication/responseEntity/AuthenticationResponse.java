package com.dalhousie.Neighbourly.authentication.responseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private int userId;
    private String userType;
    private Integer neighbourhoodId;
}
