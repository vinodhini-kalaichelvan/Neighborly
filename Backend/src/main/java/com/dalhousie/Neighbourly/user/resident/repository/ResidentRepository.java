package com.dalhousie.Neighbourly.user.resident.repository;




import com.dalhousie.Neighbourly.user.resident.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    List<Resident> findByNeighbourhoodId(int neighbourhoodId);
}
