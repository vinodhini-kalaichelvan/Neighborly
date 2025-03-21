package com.dalhousie.Neighbourly.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrowseParkingSpaceRequestDTO {
    private Integer neighbourhoodId;
    private String parkingType;
    private Integer price;
    private String priceType;
    private String feature;
}
