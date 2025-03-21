package com.dalhousie.Neighbourly.parking.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowseParkingSpaceResponseDTO {

    private int parkingSpaceId;
    private String parkingSpaceName;
    private String price;
    private String parkingType;
    private String priceType;
    private boolean available;
    private String contactInfo;
    private String feature;
    private String createdAt;
    private String updatedAt;
}
