package com.dalhousie.Neighbourly.user.resident.service;


import com.dalhousie.Neighbourly.user.resident.model.Resident;
import com.dalhousie.Neighbourly.user.resident.repository.ResidentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResidentService {

   private final ResidentRepository residentRepository;

    public Resident saveResident(Resident resident) {
        return residentRepository.save(resident);
    }

    public void deleteResident(int id) {
        residentRepository.deleteById(id);
    }

    public boolean isResidentPresent(String emailId){
        return residentRepository.findByEmail(emailId).isPresent();
    }

    public Optional<Resident> findResidentByEmail(String email){
        return residentRepository.findByEmail(email);
    }
}
