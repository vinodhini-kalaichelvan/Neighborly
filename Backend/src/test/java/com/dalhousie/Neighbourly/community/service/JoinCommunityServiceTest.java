package com.dalhousie.Neighbourly.community.service;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.repository.HelpRequestRepository;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JoinCommunityServiceTest {

    @Mock
    private HelpRequestService helpRequestService;

    @Mock
    private HelpRequestRepository helpRequestRepository;

    @Mock
    private UserRepository userRepository;


    private JoinCommunityService joinCommunityService;

    @BeforeEach
    void setUp() {
        joinCommunityService = new JoinCommunityService(helpRequestService, helpRequestRepository, userRepository);
    }

    @Test
    void testStoreJoinRequest_returnsCommunityResponse() {
        HelpRequestDTO dto = new HelpRequestDTO();
        CommunityResponse expectedResponse = new CommunityResponse(1, 10, null);

        when(helpRequestService.storeJoinRequest(dto)).thenReturn(expectedResponse);

        CommunityResponse result = joinCommunityService.storeJoinRequest(dto);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(10, result.getNeighbourhoodId());
    }

    @Test
    void testApproveJoinRequest_success() {
        int requestId = 101;
        int userId = 1;
        int neighbourhoodId = 20;

        User user = new User();
        user.setId(userId);

        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setNeighbourhoodId(neighbourhoodId);

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequestId(requestId);
        helpRequest.setUser(user);
        helpRequest.setNeighbourhood(neighbourhood);
        helpRequest.setRequestType(HelpRequest.RequestType.JOIN);

        when(helpRequestRepository.findById(requestId)).thenReturn(Optional.of(helpRequest));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(helpRequestRepository.save(any(HelpRequest.class))).thenReturn(helpRequest);

        CustomResponseBody<CommunityResponse> result = joinCommunityService.approveJoinRequest(requestId);

        assertEquals(CustomResponseBody.Result.SUCCESS, result.result());
        assertNotNull(result.data());

        CommunityResponse response = result.data();
        assertEquals(userId, response.getUserId());
        assertEquals(neighbourhoodId, response.getNeighbourhoodId());
        assertEquals(HelpRequest.RequestStatus.APPROVED, response.getStatus());
    }

    @Test
    void testApproveJoinRequest_requestNotFound() {
        when(helpRequestRepository.findById(1)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = joinCommunityService.approveJoinRequest(1);

        assertEquals(CustomResponseBody.Result.FAILURE, result.result());
        assertNull(result.data());
        assertEquals("Join request not found", result.message());
    }

    @Test
    void testApproveJoinRequest_userNotFound() {
        HelpRequest request = new HelpRequest();
        User user = new User();
        user.setId(101);
        request.setUser(user);

        when(helpRequestRepository.findById(1)).thenReturn(Optional.of(request));
        when(userRepository.findById(101)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = joinCommunityService.approveJoinRequest(1);

        assertEquals(CustomResponseBody.Result.FAILURE, result.result());
        assertNull(result.data());
        assertEquals("User not found", result.message());
    }

    @Test
    void testDenyJoinRequest_success() {
        User user = new User();
        user.setId(1);

        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setNeighbourhoodId(99);

        HelpRequest request = new HelpRequest();
        request.setUser(user);
        request.setNeighbourhood(neighbourhood);

        when(helpRequestRepository.findById(1)).thenReturn(Optional.of(request));

        CustomResponseBody<CommunityResponse> result = joinCommunityService.denyJoinRequest(1);

        assertEquals(CustomResponseBody.Result.SUCCESS, result.result());
        assertNotNull(result.data());
        assertEquals("User denied and request status updated", result.message());

        CommunityResponse response = result.data();
        assertEquals(1, response.getUserId());
        assertEquals(99, response.getNeighbourhoodId());

        verify(helpRequestRepository).save(request);
    }

    @Test
    void testDenyJoinRequest_requestNotFound() {
        when(helpRequestRepository.findById(1)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = joinCommunityService.denyJoinRequest(1);

        assertEquals(CustomResponseBody.Result.FAILURE, result.result());
        assertNull(result.data());
        assertEquals("Join request not found", result.message());
    }
}
