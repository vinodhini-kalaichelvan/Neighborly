package com.dalhousie.Neighbourly.user.resident.controller;




import com.dalhousie.Neighbourly.user.resident.model.Resident;
import com.dalhousie.Neighbourly.user.resident.service.ResidentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/residents")
@RequiredArgsConstructor
@Slf4j
public class ResidentController {
    
    private final ResidentService residentService;
    
}
