//package com.dalhousie.Neighbourly.parking.service;
//
//import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
//import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceRequestDTO;
//import com.dalhousie.Neighbourly.parking.dto.BrowseParkingSpaceResponseDTO;
//import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceRequestDTO;
//import com.dalhousie.Neighbourly.parking.dto.CreateParkingSpaceResponseDTO;
//import com.dalhousie.Neighbourly.parking.entity.ParkingSpace;
//import com.dalhousie.Neighbourly.parking.entity.ParkingType;
//import com.dalhousie.Neighbourly.parking.entity.PriceType;
//import com.dalhousie.Neighbourly.parking.repository.ParkingSpaceRepository;
//import com.dalhousie.Neighbourly.user.entity.User;
//import com.dalhousie.Neighbourly.user.repository.UserRepository;
//import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.dalhousie.Neighbourly.parking.entity.ParkingFeature;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ParkingSpaceServiceTest {
//
//    @Mock
//    private ParkingSpaceRepository parkingSpaceRepository;
//
//    @Mock
//    private NeighbourhoodRepository neighbourhoodRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private ParkingSpaceServiceImpl parkingSpaceService;
//
//    private Neighbourhood neighbourhood;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        // Mock existing neighbourhood and user before every test
//        neighbourhood = createNeighbourhood(101, "B3H 1B9");
//        user = createUser(101, "John Doe");
//    }
//
//    // Method to create Neighbourhood
//    private Neighbourhood createNeighbourhood(int id, String name) {
//        Neighbourhood neighbourhood = new Neighbourhood();
//        neighbourhood.setNeighbourhoodId(id);
//        neighbourhood.setName(name);
//        return neighbourhood;
//    }
//
//    // Method to create User
//    private User createUser(int id, String name) {
//        User user = new User();
//        user.setId(id);
//        user.setName(name);
//        return user;
//    }
//
//    // Method to create ParkingSpace
//    private ParkingSpace createParkingSpace(int parkingSpaceId, User user, Neighbourhood neighbourhood, String name,
//                                            String parkingType, int price, String priceType,
//                                            String feature, String contactInfo) {
//        ParkingSpace parkingSpace = new ParkingSpace();
//        parkingSpace.setParkingSpaceId(parkingSpaceId);
//        parkingSpace.setUser(user);
//        parkingSpace.setAssignToUser(null);
//        parkingSpace.setNeighbourhood(neighbourhood);
//        parkingSpace.setParkingSpaceName(name);
//        parkingSpace.setParkingType(ParkingType.valueOf(parkingType));
//        parkingSpace.setPrice(price);
//        parkingSpace.setPriceType(PriceType.valueOf(priceType));
//        parkingSpace.setContactInfo(contactInfo);
//        parkingSpace.setParkingFeature(ParkingFeature.valueOf(feature));
//        return parkingSpace;
//    }
//
//    @Test
//    void testCreateParkingSpace_Success() {
//
//
//        // Given
//        CreateParkingSpaceRequestDTO request = new CreateParkingSpaceRequestDTO(
//            101, 202, "Premium Parking", "COVERED", 100, "MONTHLY",  "SECURITY_CAMERAS", "123-456-7890"
//        );
//
//        ParkingSpace parkingSpace = createParkingSpace(1,user, neighbourhood, "Premium Parking", "COVERED", 100, "MONTHLY", "SECURITY_CAMERAS", "123-456-7890");
//
//        when(userRepository.findById(101)).thenReturn(Optional.of(user));
//        when(neighbourhoodRepository.findById(202)).thenReturn(Optional.of(neighbourhood));
//        when(parkingSpaceRepository.save(any(ParkingSpace.class))).thenReturn(parkingSpace);
//
//        // When
//        CreateParkingSpaceResponseDTO response = parkingSpaceService.createParkingSpace(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(1L, response.getId());
//        assertEquals("Premium Parking", response.getParkingSpaceName());
//        assertEquals(ParkingType.COVERED.name(), response.getParkingType());
//        assertEquals(100, response.getPrice());
//        assertEquals(PriceType.MONTHLY.name(), response.getPriceType());
//        assertEquals("123-456-7890", response.getContactInfo());
//
//        verify(parkingSpaceRepository, times(1)).save(any(ParkingSpace.class));
//        verify(userRepository, times(1)).findById(101);
//        verify(neighbourhoodRepository, times(1)).findById(202);
//    }
//
//    @Test
//    void testCreateParkingSpace_Failure_NeighbourhoodNotFound() {
//        // Given
//        CreateParkingSpaceRequestDTO request = new CreateParkingSpaceRequestDTO(
//            101, 999, "Premium Parking", "COVERED", 100, "MONTHLY", "EV_CHARGING", "123-456-7890"
//        );
//
//        when(userRepository.findById(101)).thenReturn(Optional.of(user));
//
//        when(neighbourhoodRepository.findById(999)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.createParkingSpace(request);
//        });
//
//        assertEquals("Neighbourhood not found", exception.getMessage());
//        verify(parkingSpaceRepository, never()).save(any(ParkingSpace.class));
//    }
//
//    @Test
//    void testCreateParkingSpace_Failure_UserNotFound() {
//        // Given
//        CreateParkingSpaceRequestDTO request = new CreateParkingSpaceRequestDTO(
//            999, 202, "Premium Parking", "COVERED", 100, "MONTHLY",  "SECURITY_CAMERAS", "123-456-7890"
//        );
//
//        when(userRepository.findById(999)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.createParkingSpace(request);
//        });
//
//        assertEquals("User not found", exception.getMessage());
//        verify(parkingSpaceRepository, never()).save(any(ParkingSpace.class));
//    }
//
//    @Test
//    void testBrowseParkingSpaceByNeighbourhood_Success() {
//
//        BrowseParkingSpaceRequestDTO request = new BrowseParkingSpaceRequestDTO(neighbourhood.getNeighbourhoodId(), "COVERED", 100, "MONTHLY", "EV_CHARGING");
//
//        // Create ParkingSpace objects
//        ParkingSpace parkingSpace1 = createParkingSpace(1,user, neighbourhood, "Premium Parking", "COVERED", 100, "MONTHLY", "EV_CHARGING", "123-456-7890");
//        ParkingSpace parkingSpace2 = createParkingSpace(2,user, neighbourhood, "Standard Parking", "UNCOVERED", 50, "DAILY", "SECURITY_CAMERAS", "987-654-3210");
//
//        // Mock the behaviour of neighbourhoodRepository
//        when(neighbourhoodRepository.findByNeighbourhoodId(neighbourhood.getNeighbourhoodId())).thenReturn(Optional.of(neighbourhood));
//
//        // Mock the parkingSpaceRepository to return the list of parking spaces for the given neighbourhood
//        when(parkingSpaceRepository.findByNeighbourhood(neighbourhood)).thenReturn(List.of(parkingSpace1, parkingSpace2));
//
//        // When: Call the service method
//        List<BrowseParkingSpaceResponseDTO> response = parkingSpaceService.browseParkingSpaceByNeighbourhood(request);
//
//        // Then: Verify the result
//        assertNotNull(response);
//        assertEquals(2, response.size());
//
//        // Validate the first parking space
//        assertEquals(1, response.get(0).getParkingSpaceId());
//        assertEquals("Premium Parking", response.get(0).getParkingSpaceName());
//        assertEquals("100", response.get(0).getPrice());
//        assertTrue(response.get(0).getFeature().contains("EV_CHARGING"));
//
//        // Validate the second parking space
//        assertEquals(2, response.get(1).getParkingSpaceId());
//        assertEquals("Standard Parking", response.get(1).getParkingSpaceName());
//        assertEquals("50", response.get(1).getPrice());
//        assertTrue(response.get(1).getFeature().contains("SECURITY_CAMERAS"));
//    }
//
//
//    @Test
//    void testBrowseParkingSpaceByNeighbourhood_Failure_NeighbourhoodNotFound() {
//        // Given
//        BrowseParkingSpaceRequestDTO request = new BrowseParkingSpaceRequestDTO(999, "COVERED", 100, "MONTHLY", "EV_CHARGING");
//
//        when(neighbourhoodRepository.findByNeighbourhoodId(999)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.browseParkingSpaceByNeighbourhood(request);
//        });
//
//        assertEquals("Neighbourhood not found", exception.getMessage());
//    }
//
//    @Test
//    void testReserveParkingSpace_Success() {
//        // Given
//        ParkingSpace parkingSpace = createParkingSpace(1, user, neighbourhood, "Premium Parking", "COVERED", 100, "MONTHLY", "EV_CHARGING", "123-456-7890");
//        when(parkingSpaceRepository.findByParkingSpaceId(1)).thenReturn(Optional.of(parkingSpace));
//        parkingSpace.setAssignToUser(user);
//
//        // When
//        parkingSpaceService.reserveParkingSpace(1, 101);
//
//        // Then
//        assertTrue(parkingSpace.isAvailable());
//        assertEquals(101, parkingSpace.getUser().getId());
//        verify(parkingSpaceRepository, times(1)).save(parkingSpace);
//    }
//
//    @Test
//    void testReserveParkingSpace_Failure_ParkingSpaceNotFound() {
//        // Given
//        when(parkingSpaceRepository.findByParkingSpaceId(1)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.reserveParkingSpace(1, 101);
//        });
//
//        assertEquals("Parking space not found", exception.getMessage());
//        verify(parkingSpaceRepository, never()).save(any(ParkingSpace.class));
//    }
//
//    @Test
//    void deleteParkingSpace_Success() {
//        // Given
//        ParkingSpace parkingSpace = createParkingSpace(1, user, neighbourhood, "Premium Parking", "COVERED", 100, "MONTHLY", "EV_CHARGING", "123-456-7890");
//
//        when(parkingSpaceRepository.findByParkingSpaceId(1)).thenReturn(Optional.of(parkingSpace));
//
//        // When
//        parkingSpaceService.deleteParkingSpace(1, 101);
//
//        // Then
//        verify(parkingSpaceRepository, times(1)).delete(parkingSpace);
//    }
//
//    @Test
//    void deleteParkingSpace_Failure_ParkingSpaceNotFound() {
//        // Given
//        when(parkingSpaceRepository.findByParkingSpaceId(1)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.deleteParkingSpace(1, 101);
//        });
//
//        assertEquals("Parking space not found", exception.getMessage());
//        verify(parkingSpaceRepository, never()).delete(any(ParkingSpace.class));
//
//
//    @Test
//    void deleteParkingSpace_Failure_UserDoesNotHavePermission() {
//        // Given
//        ParkingSpace parkingSpace = createParkingSpace(1, user, neighbourhood, "Premium Parking", "COVERED", 100, "MONTHLY", "EV_CHARGING", "123-456-7890");
//
//        when(parkingSpaceRepository.findByParkingSpaceId(1)).thenReturn(Optional.of(parkingSpace));
//
//        // When & Then
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkingSpaceService.deleteParkingSpace(1, 999);
//        });
//
//        assertEquals("User does not have permission to delete this parking space", exception.getMessage());
//        verify(parkingSpaceRepository, never()).delete(any(ParkingSpace.class));
//    }
//}
