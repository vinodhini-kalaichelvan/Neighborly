package com.dalhousie.Neighbourly.helprequest.controller;

import com.dalhousie.Neighbourly.community.entities.CommunityResponse;
import com.dalhousie.Neighbourly.community.service.JoinCommunityService;
import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.helprequest.service.HelpRequestService;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {

    private final HelpRequestService helpRequestService;
    private final JoinCommunityService joinCommunityService;
    @GetMapping("/{neighbourhoodId}")
    public ResponseEntity<List<HelpRequest>> getHelpRequests(@PathVariable int neighbourhoodId) {
        List<HelpRequest> requests = helpRequestService.getRequestsForCommunityManager(neighbourhoodId);
        return ResponseEntity.ok(requests);
    }


    @PostMapping("/approve/{requestId}")
    public ResponseEntity<CustomResponseBody<CommunityResponse>> approveJoinRequest(@PathVariable int requestId) {
        CustomResponseBody<CommunityResponse> response = joinCommunityService.approveJoinRequest(requestId);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/deny/{requestId}")
//    public ResponseEntity<CustomResponseBody<String>> denyRequest(@PathVariable int requestId) {
//        // Delete or reject logic here
//        return ResponseEntity.ok(new CustomResponseBody<>("SUCCESS", null, "Request denied"));
//    }

}
