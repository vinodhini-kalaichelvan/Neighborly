package com.dalhousie.Neighbourly.community.service;




import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.helprequest.dto.HelpRequestDTO;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinCommunityService {
    private final HelpRequestService helpRequestService;
    public CommunityResponse createHelpRequest(HelpRequestDTO dto) {
        return helpRequestService.createHelpRequest(dto);
    }
}

