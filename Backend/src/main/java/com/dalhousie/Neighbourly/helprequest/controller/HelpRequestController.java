package com.dalhousie.Neighbourly.helprequest.controller;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.community.service.JoinCommunityService;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help-requests")
@RequiredArgsConstructor
@Slf4j
public class HelpRequestController {

    private final HelpRequestService helpRequestService;
    private final JoinCommunityService joinCommunityService;
    @GetMapping("/{neighbourhoodId}")
    public ResponseEntity<List<HelpRequest>> getJoinRequests(@PathVariable int neighbourhoodId) {
        log.info("Entered getJoinRequests");
        List<HelpRequest> requests = helpRequestService.getAllJoinCommunityRequests(neighbourhoodId);
        return ResponseEntity.ok(requests);
    }



}
