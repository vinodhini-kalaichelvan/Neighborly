package com.dalhousie.Neighbourly.helprequest.repository;

import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Integer> {

    List<HelpRequest> findByNeighbourhood(Neighbourhood neighbourhood);
}
