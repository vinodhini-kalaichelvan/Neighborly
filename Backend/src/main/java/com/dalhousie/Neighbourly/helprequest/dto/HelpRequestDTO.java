package com.dalhousie.Neighbourly.helprequest.dto;

import lombok.Data;

@Data
public class HelpRequestDTO {
    private int userId;
    private int neighbourhoodId;
    private String requestType;
    private String description;
}


//store the pincode as descreption in help requestcd F