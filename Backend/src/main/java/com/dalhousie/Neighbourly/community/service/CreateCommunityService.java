package com.dalhousie.Neighbourly.community.service;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.repository.HelpRequestRepository;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.entity.UserType;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateCommunityService {
    private final HelpRequestService helpRequestService;
    private final HelpRequestRepository helpRequestRepository;
    private final NeighbourhoodRepository neighbourhoodRepository;
    private final UserRepository userRepository;


    public CommunityResponse storeCreateRequest(HelpRequestDTO dto) {
        return helpRequestService.storeCreateRequest(dto);
    }


    @Transactional
    public CustomResponseBody<CommunityResponse> approveCreateRequest(int requestId) {
        // Fetch request details
        Optional<HelpRequest> requestOptional = helpRequestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            return new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "Create request not found");
        }

        HelpRequest request = requestOptional.get();

        // Fetch user details
        Optional<User> userOptional = userRepository.findById(request.getUser().getId());
        if (userOptional.isEmpty()) {
            return new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "User not found");
        }

        User user = userOptional.get();

        // Create new neighbourhood
        Neighbourhood neighbourhood = new Neighbourhood();
        neighbourhood.setName(request.getDescription().split(": ")[1]); // Extract name from description
        neighbourhood.setLocation(request.getUser().getAddress()); // Assume address is used as location
        Neighbourhood savedNeighbourhood = neighbourhoodRepository.save(neighbourhood);

        // Assign user as community manager
        user.setUserType(UserType.COMMUNITY_MANAGER);
        user.setNeighbourhood_id(savedNeighbourhood.getNeighbourhoodId());
        userRepository.save(user);

        // Update request status to APPROVED
        request.setStatus(HelpRequest.RequestStatus.APPROVED);
        helpRequestRepository.save(request);

        // Create response
        CommunityResponse response = new CommunityResponse(request.getUser().getId(), request.getNeighbourhood().getNeighbourhoodId(), HelpRequest.RequestStatus.APPROVED);

        return new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Community successfully created");
    }

    @Transactional
    public CustomResponseBody<CommunityResponse> denyCreateRequest(int requestId) {
        // Fetch request details
        Optional<HelpRequest> requestOptional = helpRequestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            return new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, "Create request not found");
        }

        HelpRequest request = requestOptional.get();

        // Update request status to DECLINED
        request.setStatus(HelpRequest.RequestStatus.DECLINED);
        helpRequestRepository.save(request);

        // Create response
        CommunityResponse response = new CommunityResponse(request.getUser().getId(), request.getNeighbourhood().getNeighbourhoodId(), HelpRequest.RequestStatus.APPROVED);

        return new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Community creation request denied");
    }
}
