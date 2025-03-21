package com.dalhousie.Neighbourly.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.parking.entity.ParkingSpace;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {
   Optional<ParkingSpace> findByParkingSpaceId(int parkingSpaceId);
   List<ParkingSpace> findByNeighbourhood(Neighbourhood neighbourhood);
}
