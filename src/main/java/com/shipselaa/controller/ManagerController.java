package com.shipselaa.controller;

import com.shipselaa.service.DemandeService;
import com.shipselaa.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private DemandeService demandeService;












}
