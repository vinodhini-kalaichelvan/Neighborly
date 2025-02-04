package com.dalhousie.Neighbourly.user.communitymanager.controller;

import com.dalhousie.Neighbourly.user.communitymanager.model.CommunityManager;
import com.dalhousie.Neighbourly.user.communitymanager.service.CommunityManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/community-managers")
public class CommunityManagerController {

    @Autowired
    private CommunityManagerService communityManagerService;

    @GetMapping
    public List<CommunityManager> getAllCommunityManagers() {
        return communityManagerService.getAllCommunityManagers();
    }

    @GetMapping("/{id}")
    public Optional<CommunityManager> getCommunityManagerById(@PathVariable int id) {
        return communityManagerService.getCommunityManagerById(id);
    }

    @GetMapping("/neighbourhood/{neighbourhoodId}")
    public List<CommunityManager> getManagersByNeighbourhood(@PathVariable int neighbourhoodId) {
        return communityManagerService.getManagersByNeighbourhood(neighbourhoodId);
    }

    @PostMapping
    public CommunityManager createCommunityManager(@RequestBody CommunityManager communityManager) {
        return communityManagerService.saveCommunityManager(communityManager);
    }

    @DeleteMapping("/{id}")
    public void deleteCommunityManager(@PathVariable int id) {
        communityManagerService.deleteCommunityManager(id);
    }
}
