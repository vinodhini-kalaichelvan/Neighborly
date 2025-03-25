package com.dalhousie.Neighbourly.parking.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceResponseDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceRequestDTO;
import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceResponseDTO;
import com.dalhousie.Neighbourly.parking.entity.ParkingFeature;
import com.dalhousie.Neighbourly.parking.entity.ParkingSpace;
import com.dalhousie.Neighbourly.parking.repository.ParkingSpaceRepository;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.parking.entity.ParkingType;
import com.dalhousie.Neighbourly.parking.entity.PriceType;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final UserRepository userRepository;
    private final NeighbourhoodRepository neighbourhoodRepository;

    public CreateParkingSpaceResponseDTO createParkingSpace(CreateParkingSpaceRequestDTO dto) {
        int userId = dto.getUserId();
        int neighbourhoodId = dto.getNeighbourhoodId();
        // Check if the user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the neighbourhood exists
        Neighbourhood neighbourhood = neighbourhoodRepository.findById(neighbourhoodId)
                .orElseThrow(() -> new RuntimeException("Neighbourhood not found"));

        // Create a new ParkingSpace entity and map the request data
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setUser(user); 
        parkingSpace.setNeighbourhood(neighbourhood);
        parkingSpace.setParkingSpaceName(dto.getParkingSpaceName());
        parkingSpace.setParkingType(ParkingType.valueOf(dto.getParkingType()));  
        parkingSpace.setAvailable(dto.isAvailable());
        parkingSpace.setPrice(dto.getPrice());
        parkingSpace.setParkingFeature(ParkingFeature.valueOf(dto.getFeature()));
        parkingSpace.setPriceType(PriceType.valueOf(dto.getPriceType())); 
        parkingSpace.setContactInfo(dto.getContactInfo());

        // Save the ParkingSpace entity
        ParkingSpace savedParkingSpace = parkingSpaceRepository.save(parkingSpace);

        // Return the mapped response DTO
        return mapToResponse(savedParkingSpace);
    }

    @Override
    public List<BrowseParkingSpaceResponseDTO> browseParkingSpaceByNeighbourhood(BrowseParkingSpaceRequestDTO browseParkingSpaceRequestDTO) {
        Integer neighbourhoodId = browseParkingSpaceRequestDTO.getNeighbourhoodId();

        Neighbourhood neighbourhood = neighbourhoodRepository.findByNeighbourhoodId(neighbourhoodId)
                .orElseThrow(() -> new RuntimeException("Neighbourhood not found"));
    
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByNeighbourhood(neighbourhood);
    
        return parkingSpaces.stream()
                .map(this::mapToBrowseResponse)
                .collect(Collectors.toList());
    }
    
    private BrowseParkingSpaceResponseDTO mapToBrowseResponse(ParkingSpace parkingSpace) {
        return BrowseParkingSpaceResponseDTO.builder()
                .parkingSpaceId(parkingSpace.getParkingSpaceId())
                .parkingSpaceName(parkingSpace.getParkingSpaceName())
                .price(String.valueOf(parkingSpace.getPrice()))
                .parkingType(parkingSpace.getParkingType().name())
                .priceType(parkingSpace.getPriceType().name())
                .available(parkingSpace.isAvailable())
                .contactInfo(parkingSpace.getContactInfo())
                .feature(parkingSpace.getParkingFeature().name())
                .createdAt(parkingSpace.getCreatedAt() != null ? parkingSpace.getCreatedAt().toInstant().toString() : Instant.now().toString())
                .updatedAt(parkingSpace.getUpdatedAt() != null ? parkingSpace.getUpdatedAt().toInstant().toString() : Instant.now().toString())
                .build();
    }
    
    // Helper method to map ParkingSpace entity to CreateParkingSpaceResponseDTO using builder
    private CreateParkingSpaceResponseDTO mapToResponse(ParkingSpace parkingSpace) {
      return CreateParkingSpaceResponseDTO.builder()
                .id(parkingSpace.getParkingSpaceId())
                .parkingSpaceName(parkingSpace.getParkingSpaceName())
                .parkingType(parkingSpace.getParkingType().name()) 
                .price(parkingSpace.getPrice())
                .priceType(parkingSpace.getPriceType().name())  
                .contactInfo(parkingSpace.getContactInfo())
                .createdAt(parkingSpace.getCreatedAt() != null ? parkingSpace.getCreatedAt().toInstant() : Instant.now())
                .updatedAt(parkingSpace.getUpdatedAt() != null ? parkingSpace.getUpdatedAt().toInstant() : Instant.now())
                .build();
    }

        @Override
        public void reserveParkingSpace(int parkingSpaceId, int userId) {
            Optional<ParkingSpace> optionalParkingSpace = parkingSpaceRepository.findByParkingSpaceId(parkingSpaceId);
            if (!optionalParkingSpace.isPresent()) {
                throw new RuntimeException("Parking space not found");
            }
            ParkingSpace parkingSpace = optionalParkingSpace.get();

            if (!parkingSpace.isAvailable()) {
                throw new RuntimeException("Parking space is not available");
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new RuntimeException("User not found");
            }
            User user = optionalUser.get();
            parkingSpace.setAssignToUser(user);
            parkingSpace.setAvailable(false);
    
            parkingSpaceRepository.save(parkingSpace);
        }

        @Override
        public void unreserveParkingSpace(int parkingSpaceId, int userId) {
            ParkingSpace parkingSpace = parkingSpaceRepository.findByParkingSpaceId(parkingSpaceId)
                    .orElseThrow(() -> new RuntimeException("Parking space not found"));
                    if(parkingSpace.isAvailable()){
                        throw new RuntimeException("Parking space is already available");
                    }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        
              if(userId != parkingSpace.getAssignToUser().getId()){
                throw new RuntimeException("User does not have permission to unreserve this parking space");      
              }
            parkingSpace.setAssignToUser(null);
            parkingSpace.setAvailable(true);
    
            parkingSpaceRepository.save(parkingSpace);
        }

        @Override
        public void deleteParkingSpace(int parkingSpaceId, int userId) {
            ParkingSpace parkingSpace = parkingSpaceRepository.findByParkingSpaceId(parkingSpaceId)
                    .orElseThrow(() -> new RuntimeException("Parking space not found"));
      
            if(parkingSpace.getUser() == null || parkingSpace.getUser().getId() != userId){
                throw new RuntimeException("User does not have permission to delete this parking space");
            }
            parkingSpaceRepository.delete(parkingSpace);
        }

}
