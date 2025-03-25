package com.dalhousie.Neighbourly.parking.dto;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrowseParkingSpaceRequestDTO {
    private Integer neighbourhoodId;
    private String parkingType;
    private Integer price;
    private String priceType;
    private String feature;
}
