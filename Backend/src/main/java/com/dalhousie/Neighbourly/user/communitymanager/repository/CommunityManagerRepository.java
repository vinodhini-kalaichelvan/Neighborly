package com.dalhousie.Neighbourly.user.communitymanager.repository;

import com.dalhousie.Neighbourly.user.communitymanager.model.CommunityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityManagerRepository extends JpaRepository<CommunityManager, Integer> {
    List<CommunityManager> findByNeighbourhoodId(int neighbourhoodId);
}
