package com.dalhousie.Neighbourly.neighbourhood.repository;




import com.dalhousie.Neighbourly.neighbourhood.model.Neighbourhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourhoodRepository extends JpaRepository<Neighbourhood, Integer> {
}
