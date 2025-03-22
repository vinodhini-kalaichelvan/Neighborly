package com.dalhousie.Neighbourly.helprequest.service;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.repository.HelpRequestRepository;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpRequestServiceTest {

    @Mock
    private HelpRequestRepository helpRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NeighbourhoodRepository neighbourhoodRepository;

    @InjectMocks
    private HelpRequestService helpRequestService;

    @BeforeEach
    void setUp() {
        helpRequestService = new HelpRequestService(helpRequestRepository, userRepository, neighbourhoodRepository);
    }

    @Test
    void testStoreJoinRequest_success() {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setUserId(1);
        dto.setNeighbourhoodId(10);
        dto.setDescription("Join request description");

        User user = new User();
        user.setId(1);

        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setNeighbourhoodId(10);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(neighbourhoodRepository.findById(10)).thenReturn(Optional.of(neighbourhood));
        when(helpRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CommunityResponse response = helpRequestService.storeJoinRequest(dto);

        assertNotNull(response);
        assertEquals(1, response.getUserId());
        assertEquals(10, response.getNeighbourhoodId());

        verify(helpRequestRepository).save(any());
    }

    @Test
    void testStoreJoinRequest_userNotFound() {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setUserId(2);
        dto.setNeighbourhoodId(10);

        when(userRepository.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> helpRequestService.storeJoinRequest(dto));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testStoreJoinRequest_neighbourhoodNotFound() {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setUserId(1);
        dto.setNeighbourhoodId(99);

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(neighbourhoodRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> helpRequestService.storeJoinRequest(dto));
        assertEquals("Neighbourhood not found", exception.getMessage());
    }

    @Test
    void testStoreCreateRequest_success() {
            HelpRequestDTO dto = new HelpRequestDTO();
            dto.setUserId(1);
            dto.setDescription("Create community request");

            User user = new User();
            user.setId(1);

            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(helpRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            CommunityResponse response = helpRequestService.storeCreateRequest(dto);

            assertNotNull(response);
            assertEquals(1, response.getUserId());
            assertEquals(0, response.getNeighbourhoodId());

            verify(helpRequestRepository).save(any());
    }

    @Test
    void testStoreCreateRequest_userNotFound() {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setUserId(99);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> helpRequestService.storeCreateRequest(dto));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetAllJoinCommunityRequests_success() {
        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setNeighbourhoodId(10);

        HelpRequest request1 = new HelpRequest();
        request1.setNeighbourhood(neighbourhood);
        request1.setRequestType(HelpRequest.RequestType.JOIN);
        request1.setStatus(HelpRequest.RequestStatus.OPEN);

        HelpRequest request2 = new HelpRequest();
        request2.setNeighbourhood(neighbourhood);
        request2.setRequestType(HelpRequest.RequestType.JOIN);
        request2.setStatus(HelpRequest.RequestStatus.OPEN);

        when(neighbourhoodRepository.findByNeighbourhoodId(10)).thenReturn(Optional.of(neighbourhood));
        when(helpRequestRepository.findByNeighbourhoodAndRequestTypeAndStatus(neighbourhood, HelpRequest.RequestType.JOIN, HelpRequest.RequestStatus.OPEN))
                .thenReturn(List.of(request1, request2));

        List<HelpRequest> result = helpRequestService.getAllJoinCommunityRequests(10);

        assertEquals(2, result.size());
        verify(helpRequestRepository).findByNeighbourhoodAndRequestTypeAndStatus(neighbourhood, HelpRequest.RequestType.JOIN, HelpRequest.RequestStatus.OPEN);
    }

    @Test
    void testGetAllJoinCommunityRequests_neighbourhoodNotFound() {
        when(neighbourhoodRepository.findByNeighbourhoodId(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> helpRequestService.getAllJoinCommunityRequests(99));
        assertEquals("Neighbourhood not found", exception.getMessage());
    }

    @Test
    void testGetAllOpenCommunityRequests_success() {
        HelpRequest helpRequest = new HelpRequest();

        User user = new User();
        user.setId(1);
        helpRequest.setUser(user);

        helpRequest.setStatus(HelpRequest.RequestStatus.OPEN);
        helpRequest.setDescription("Community request");

        when(helpRequestRepository.findByStatus(HelpRequest.RequestStatus.OPEN)).thenReturn(List.of(helpRequest));

        List<HelpRequestDTO> result = helpRequestService.getAllOpenCommunityRequests();

        assertNotNull(result);
        assertEquals(1, result.get(0).getUserId());
    }

    @Test
    void testGetRequestsForAdmin_success() {
        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setNeighbourhoodId(10);

        HelpRequest request1 = new HelpRequest();
        request1.setNeighbourhood(neighbourhood);

        when(neighbourhoodRepository.findByNeighbourhoodId(10)).thenReturn(Optional.of(neighbourhood));
        when(helpRequestRepository.findByNeighbourhood(neighbourhood)).thenReturn(List.of(request1));

        List<HelpRequest> result = helpRequestService.getRequestsForAdmin(10);

        assertEquals(1, result.size());
        verify(helpRequestRepository).findByNeighbourhood(neighbourhood);
    }

    @Test
    void testGetRequestsForAdmin_neighbourhoodNotFound() {
        when(neighbourhoodRepository.findByNeighbourhoodId(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> helpRequestService.getRequestsForAdmin(99));
        assertEquals("Neighbourhood not found", exception.getMessage());
    }
}
