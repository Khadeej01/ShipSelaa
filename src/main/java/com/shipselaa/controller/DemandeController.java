package com.shipselaa.controller;

import com.shipselaa.model.Demande;
import com.shipselaa.model.StatusDemande;
import com.shipselaa.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeService.getAllDemandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        return demande.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


}
