package com.dalhousie.Neighbourly.user.resident.repository;




import com.dalhousie.Neighbourly.user.resident.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    Optional<Resident> findByEmail(String email);
}
