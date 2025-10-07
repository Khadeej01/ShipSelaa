package com.shipselaa.controller;


import com.shipselaa.model.Livreur;
import com.shipselaa.service.DemandeService;
import com.shipselaa.service.LivreurService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/livreurs")
@CrossOrigin(origins = "*")
public class LivreurController {

    @Autowired
    private LivreurService livreurService;

    @Autowired
    private DemandeService demandeService;

    @GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs(
            @RequestParam(value = "disponible", required = false) Boolean disponible) {
        List<Livreur> livreurs;
        if (disponible != null) {
            livreurs = livreurService.getAvailableLivreurs();
        } else {
            livreurs = livreurService.getAllLivreurs();
        }
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable Long id) {
        Optional<Livreur> livreur = livreurService.getLivreurById(id);
        if (livreur.isPresent()) {
            return ResponseEntity.ok(livreur.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        try {
            Livreur createdLivreur = livreurService.createLivreur(livreur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivreur);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livreur> updateLivreur(@PathVariable Long id, @RequestBody Livreur livreur) {
        try {
            Livreur updatedLivreur = livreurService.updateLivreur(id, livreur);
            return ResponseEntity.ok(updatedLivreur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        try {
            livreurService.deleteLivreur(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
