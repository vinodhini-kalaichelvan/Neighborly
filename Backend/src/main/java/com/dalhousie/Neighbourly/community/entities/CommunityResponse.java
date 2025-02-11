package com.dalhousie.Neighbourly.community.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommunityResponse {
    private int userId;
    private int neighbourhoodId;
    private String status;
}
