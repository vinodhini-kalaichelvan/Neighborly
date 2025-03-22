package com.dalhousie.Neighbourly.community.service;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.repository.HelpRequestRepository;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCommunityServiceTest {

    @Mock
    private HelpRequestService helpRequestService;

    @Mock
    private HelpRequestRepository helpRequestRepository;

    @Mock
    private NeighbourhoodRepository neighbourhoodRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateCommunityService createCommunityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreCreateRequest_returnsCommunityResponse() {
        HelpRequestDTO dto = new HelpRequestDTO();
        CommunityResponse response = new CommunityResponse(1, 2, HelpRequest.RequestStatus.APPROVED);

        when(helpRequestService.storeCreateRequest(dto)).thenReturn(response);

        CommunityResponse result = createCommunityService.storeCreateRequest(dto);
        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(2, result.getNeighbourhoodId());
    }

    @Test
    void testApproveCreateRequest_success() {
        int requestId = 101;

        User sharedUser = new User();
        sharedUser.setId(1);

        HelpRequest request = new HelpRequest();
        request.setRequestId(requestId);
        request.setRequestType(HelpRequest.RequestType.CREATE);
        request.setDescription("location: Halifax with pincode: B3H1Y2");
        request.setUser(sharedUser);

        when(helpRequestRepository.findByRequestId(requestId))
                .thenReturn(Optional.of(request));

        when(userRepository.findById(sharedUser.getId()))
                .thenReturn(Optional.of(sharedUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(sharedUser);

        when(neighbourhoodRepository.save(any()))
                .thenAnswer(invocation -> {
                    Neighbourhood n = invocation.getArgument(0);
                    n.setNeighbourhoodId(55);
                    return n;
                });

        when(helpRequestRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomResponseBody<CommunityResponse> result =
                createCommunityService.approveCreateRequest(requestId);

        assertNotNull(result);
        assertEquals(CustomResponseBody.Result.SUCCESS, result.result());

        ArgumentCaptor<HelpRequest> captor = ArgumentCaptor.forClass(HelpRequest.class);
        verify(helpRequestRepository).save(captor.capture());
        assertEquals(HelpRequest.RequestStatus.APPROVED, captor.getValue().getStatus());

        verify(userRepository).save(any(User.class));
        verify(neighbourhoodRepository).save(any(Neighbourhood.class));
    }

    @Test
    void testApproveCreateRequest_requestNotFound() {
        when(helpRequestRepository.findByRequestId(1)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = createCommunityService.approveCreateRequest(1);

        assertNotNull(result);
        assertInstanceOf(CustomResponseBody.class, result);
        assertNull(result.data());
    }

    @Test
    void testApproveCreateRequest_invalidRequestType() {
        HelpRequest request = new HelpRequest();
        request.setRequestType(HelpRequest.RequestType.JOIN);
        request.setDescription("location: X with pincode: 000000");
        request.setUser(new User());

        when(helpRequestRepository.findByRequestId(1)).thenReturn(Optional.of(request));

        CustomResponseBody<CommunityResponse> result = createCommunityService.approveCreateRequest(1);

        assertNotNull(result);
        assertInstanceOf(CustomResponseBody.class, result);
        assertNull(result.data());
    }

    @Test
    void testApproveCreateRequest_userNotFound() {
        User user = new User();
        user.setId(101);

        HelpRequest request = new HelpRequest();
        request.setRequestType(HelpRequest.RequestType.CREATE);
        request.setDescription("location: Y with pincode: 123456");
        request.setUser(user);

        when(helpRequestRepository.findByRequestId(1)).thenReturn(Optional.of(request));
        when(userRepository.findById(101)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = createCommunityService.approveCreateRequest(1);

        assertNotNull(result);
        assertInstanceOf(CustomResponseBody.class, result);
        assertNull(result.data());
    }

    @Test
    void testDenyCreateRequest_success() {
        User user = new User();
        user.setId(10);

        HelpRequest request = new HelpRequest();
        request.setRequestId(1);
        request.setDescription("location: Bedford with pincode: A1A1A1");
        request.setUser(user);

        when(helpRequestRepository.findByRequestId(1)).thenReturn(Optional.of(request));

        CustomResponseBody<CommunityResponse> result = createCommunityService.denyCreateRequest(1);

        assertNotNull(result);
        assertInstanceOf(CustomResponseBody.class, result);
        assertNotNull(result.data());

        CommunityResponse response = result.data();
        assertEquals(10, response.getUserId());
        assertEquals(0, response.getNeighbourhoodId());
    }

    @Test
    void testDenyCreateRequest_requestNotFound() {
        when(helpRequestRepository.findByRequestId(1)).thenReturn(Optional.empty());

        CustomResponseBody<CommunityResponse> result = createCommunityService.denyCreateRequest(1);

        assertNotNull(result);
        assertInstanceOf(CustomResponseBody.class, result);
        assertNull(result.data());
    }
}
