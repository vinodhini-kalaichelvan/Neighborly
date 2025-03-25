package com.dalhousie.Neighbourly.parking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceResponseDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceResponseDTO;

import com.dalhousie.Neighbourly.util.CustomResponseBody;
import com.dalhousie.Neighbourly.parking.service.ParkingSpaceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/parkingSpace")
@RequiredArgsConstructor
@Slf4j
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;


    @PostMapping("/createParkingSpace")
    public ResponseEntity<CustomResponseBody<CreateParkingSpaceResponseDTO>> createParkingSpace(@RequestBody CreateParkingSpaceRequestDTO createParkingSpaceRequestDTO) {
        try{
            CreateParkingSpaceResponseDTO response = parkingSpaceService.createParkingSpace(createParkingSpaceRequestDTO);
            CustomResponseBody<CreateParkingSpaceResponseDTO> responseBody =
                    new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Parking Space created successfully");

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            CustomResponseBody<CreateParkingSpaceResponseDTO> errorBody =
                    new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }

    }

@GetMapping("/browseParkingSpace")
public ResponseEntity<CustomResponseBody<List<BrowseParkingSpaceResponseDTO>>> browseParkingSpace(
    @RequestParam(required = false) Integer neighbourhoodId,
    @RequestParam(required = false) String parkingType,
    @RequestParam(required = false) Integer price,
    @RequestParam(required = false) String priceType,
    @RequestParam(required = false) String feature) {

    try{

        // Create a request DTO object based on available parameters
        BrowseParkingSpaceRequestDTO requestDTO = BrowseParkingSpaceRequestDTO.builder()
                .neighbourhoodId(neighbourhoodId)
                .parkingType(parkingType)
                .price(price)
                .priceType(priceType)
                .feature(feature)
                .build();

        List<BrowseParkingSpaceResponseDTO> response = parkingSpaceService.browseParkingSpaceByNeighbourhood(requestDTO);

        CustomResponseBody<List<BrowseParkingSpaceResponseDTO>> responseBody =
                new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Parking Space browsed successfully");

        return ResponseEntity.ok(responseBody);
    } catch (RuntimeException e) {
        CustomResponseBody<List<BrowseParkingSpaceResponseDTO>> errorBody =
                new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}

@PostMapping("/reserveParkingSpace")
public ResponseEntity<CustomResponseBody<String>> reserveParkingSpace(@RequestParam Integer parkingSpaceId, @RequestParam Integer userId) {
    try {
        parkingSpaceService.reserveParkingSpace(parkingSpaceId, userId);
        CustomResponseBody<String> responseBody = new CustomResponseBody<>(
                CustomResponseBody.Result.SUCCESS,
                null,
                "Parking Space reserved successfully"
        );

        return ResponseEntity.ok(responseBody);
    } catch (RuntimeException e) {
        CustomResponseBody<String> errorBody = new CustomResponseBody<>(
                CustomResponseBody.Result.FAILURE,
                null,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}

@PostMapping("/unreserveParkingSpace")
public ResponseEntity<CustomResponseBody<String>> unreserveParkingSpace(@RequestParam Integer parkingSpaceId, @RequestParam Integer userId) {
    try {
        parkingSpaceService.unreserveParkingSpace(parkingSpaceId, userId);
        return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, null, "Parking Space unreserved successfully"));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
    }
}

    @DeleteMapping("/deleteParkingSpace")
    public ResponseEntity<CustomResponseBody<String>> deleteParkingSpace(@RequestParam Integer parkingSpaceId, @RequestParam Integer userId) {
        try {
            parkingSpaceService.deleteParkingSpace(parkingSpaceId, userId);
            return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, null, "Parking Space deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
        }
    }
}

