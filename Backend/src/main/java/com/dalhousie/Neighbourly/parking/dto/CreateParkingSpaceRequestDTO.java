package com.dalhousie.Neighbourly.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateParkingSpaceRequestDTO {
    private int userId;
    private int neighbourhoodId;
    private String parkingSpaceName;
    private String parkingType;
    private int price;
    private String priceType;
    private boolean available;
    private String feature;
    private String contactInfo;
}