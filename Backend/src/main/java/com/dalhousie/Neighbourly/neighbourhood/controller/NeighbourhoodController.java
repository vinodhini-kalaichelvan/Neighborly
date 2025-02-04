package com.dalhousie.Neighbourly.neighbourhood.controller;


import com.dalhousie.Neighbourly.neighbourhood.model.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.service.NeighbourhoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/neighbourhoods")
public class NeighbourhoodController {

    @Autowired
    private NeighbourhoodService neighbourhoodService;

    @GetMapping
    public List<Neighbourhood> getAllNeighbourhoods() {
        return neighbourhoodService.getAllNeighbourhoods();
    }

    @GetMapping("/{id}")
    public Optional<Neighbourhood> getNeighbourhoodById(@PathVariable int id) {
        return neighbourhoodService.getNeighbourhoodById(id);
    }

    @PostMapping
    public Neighbourhood createNeighbourhood(@RequestBody Neighbourhood neighbourhood) {
        return neighbourhoodService.saveNeighbourhood(neighbourhood);
    }

    @DeleteMapping("/{id}")
    public void deleteNeighbourhood(@PathVariable int id) {
        neighbourhoodService.deleteNeighbourhood(id);
    }
}

