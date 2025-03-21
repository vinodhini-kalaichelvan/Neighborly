package com.dalhousie.Neighbourly.parking.service;

import java.util.List;

import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceResponseDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceResponseDTO;

public interface ParkingSpaceService {

    public CreateParkingSpaceResponseDTO createParkingSpace(CreateParkingSpaceRequestDTO createParkingSpaceRequestDTO);
    public List<BrowseParkingSpaceResponseDTO> browseParkingSpaceByNeighbourhood(BrowseParkingSpaceRequestDTO browseParkingSpaceRequestDTO);
    public void reserveParkingSpace(int parkingSpaceId, int userId);
    public void unreserveParkingSpace(int parkingSpaceId, int userId);
    public void deleteParkingSpace(int parkingSpaceId, int userId);
}