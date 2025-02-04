package com.dalhousie.Neighbourly.neighbourhood.service;



import com.dalhousie.Neighbourly.neighbourhood.model.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NeighbourhoodService {

    @Autowired
    private NeighbourhoodRepository neighbourhoodRepository;

    public List<Neighbourhood> getAllNeighbourhoods() {
        return neighbourhoodRepository.findAll();
    }

    public Optional<Neighbourhood> getNeighbourhoodById(int id) {
        return neighbourhoodRepository.findById(id);
    }

    public Neighbourhood saveNeighbourhood(Neighbourhood neighbourhood) {
        return neighbourhoodRepository.save(neighbourhood);
    }

    public void deleteNeighbourhood(int id) {
        neighbourhoodRepository.deleteById(id);
    }
}

