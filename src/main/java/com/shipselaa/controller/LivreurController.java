package com.shipselaa.controller;


import com.shipselaa.model.Livreur;
import com.shipselaa.service.DemandeService;
import com.shipselaa.service.LivreurService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/livreurs")
@CrossOrigin(origins = "*")
public class LivreurController {

    @Autowired
    private LivreurService livreurService;

    @Autowired
    private DemandeService demandeService;


    @GetMapping
    public List<Livreur> getAllLivreurs() {
        return livreurService.getAllLivreurs();
    }


}
