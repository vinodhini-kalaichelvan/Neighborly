package com.dalhousie.Neighbourly.helprequest.repository;

import com.dalhousie.Neighbourly.helprequest.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Integer> {
}
