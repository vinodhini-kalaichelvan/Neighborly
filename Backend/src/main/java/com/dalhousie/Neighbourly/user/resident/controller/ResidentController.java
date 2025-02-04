package com.dalhousie.Neighbourly.user.resident.controller;




import com.dalhousie.Neighbourly.user.resident.model.Resident;
import com.dalhousie.Neighbourly.user.resident.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping
    public List<Resident> getAllResidents() {
        return residentService.getAllResidents();
    }

    @GetMapping("/{id}")
    public Optional<Resident> getResidentById(@PathVariable int id) {
        return residentService.getResidentById(id);
    }

    @GetMapping("/neighbourhood/{neighbourhoodId}")
    public List<Resident> getResidentsByNeighbourhood(@PathVariable int neighbourhoodId) {
        return residentService.getResidentsByNeighbourhood(neighbourhoodId);
    }

    @PostMapping
    public Resident createResident(@RequestBody Resident resident) {
        return residentService.saveResident(resident);
    }

    @DeleteMapping("/{id}")
    public void deleteResident(@PathVariable int id) {
        residentService.deleteResident(id);
    }
}
