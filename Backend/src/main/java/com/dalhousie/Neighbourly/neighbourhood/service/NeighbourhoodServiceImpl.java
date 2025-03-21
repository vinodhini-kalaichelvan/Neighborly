package com.dalhousie.Neighbourly.neighbourhood.service;

import org.springframework.stereotype.Service;

import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NeighbourhoodServiceImpl implements NeighbourhoodService {

    private final NeighbourhoodRepository neighbourhoodRepository;
    
    @Override
    public boolean isNeighbourhoodExist(int neighbourhoodId) {
        return neighbourhoodRepository.findByNeighbourhoodId(neighbourhoodId).isPresent();
    }
    
}
