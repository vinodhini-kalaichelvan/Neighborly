package com.dalhousie.Neighbourly.helprequest.service;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.repository.HelpRequestRepository;
import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;
    private final NeighbourhoodRepository neighbourhoodRepository;

    public CommunityResponse createHelpRequest(HelpRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Neighbourhood neighbourhood = neighbourhoodRepository.findById(dto.getNeighbourhoodId())
                .orElseThrow(() -> new RuntimeException("Neighbourhood not found"));

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setUser(user);
        helpRequest.setNeighbourhood(neighbourhood);
        helpRequest.setRequestType(dto.getRequestType());
        helpRequest.setDescription(dto.getDescription());
        helpRequest.setStatus("OPEN");
        helpRequest.setCreatedAt(LocalDateTime.now());

        HelpRequest savedRequest = helpRequestRepository.save(helpRequest);

        // Convert HelpRequest to CommunityResponse before returning
        return new CommunityResponse(savedRequest.getUser().getId(), savedRequest.getNeighbourhood().getNeighbourhoodId(), savedRequest.getStatus());
    }




}
