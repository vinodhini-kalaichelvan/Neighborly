package com.dalhousie.Neighbourly.user.resident.service;


import com.dalhousie.Neighbourly.user.resident.model.Resident;
import com.dalhousie.Neighbourly.user.resident.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Optional<Resident> getResidentById(int id) {
        return residentRepository.findById(id);
    }

    public List<Resident> getResidentsByNeighbourhood(int neighbourhoodId) {
        return residentRepository.findByNeighbourhoodId(neighbourhoodId);
    }

    public Resident saveResident(Resident resident) {
        return residentRepository.save(resident);
    }

    public void deleteResident(int id) {
        residentRepository.deleteById(id);
    }
}
