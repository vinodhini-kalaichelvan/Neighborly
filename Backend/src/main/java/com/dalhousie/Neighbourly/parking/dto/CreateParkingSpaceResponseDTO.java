package com.dalhousie.Neighbourly.parking.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateParkingSpaceResponseDTO {
    private int id;
    private String parkingSpaceName;
    private String parkingType;
    private int price;
    private String priceType;
    private boolean available;
    private String feature;
    private String contactInfo;
    private Instant createdAt;
    private Instant updatedAt;
}