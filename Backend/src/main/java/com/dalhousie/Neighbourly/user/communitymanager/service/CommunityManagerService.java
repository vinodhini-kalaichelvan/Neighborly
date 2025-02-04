package com.dalhousie.Neighbourly.user.communitymanager.service;



import com.dalhousie.Neighbourly.user.communitymanager.model.CommunityManager;
import com.dalhousie.Neighbourly.user.communitymanager.repository.CommunityManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityManagerService {

    @Autowired
    private CommunityManagerRepository communityManagerRepository;

    public List<CommunityManager> getAllCommunityManagers() {
        return communityManagerRepository.findAll();
    }

    public Optional<CommunityManager> getCommunityManagerById(int id) {
        return communityManagerRepository.findById(id);
    }

    public List<CommunityManager> getManagersByNeighbourhood(int neighbourhoodId) {
        return communityManagerRepository.findByNeighbourhoodId(neighbourhoodId);
    }

    public CommunityManager saveCommunityManager(CommunityManager communityManager) {
        return communityManagerRepository.save(communityManager);
    }

    public void deleteCommunityManager(int id) {
        communityManagerRepository.deleteById(id);
    }
}
