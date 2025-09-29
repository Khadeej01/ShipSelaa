package com.shipselaa.controller;

import com.shipselaa.model.Demande;
import com.shipselaa.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        if (demande.isPresent()) {
            return ResponseEntity.ok(demande.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande, @RequestParam Long managerId) {
        try {
            Demande createdDemande = demandeService.createDemandeByManager(managerId, demande);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDemande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @RequestBody Demande demande) {
        try {
            Demande updatedDemande = demandeService.updateDemande(id, demande);
            return ResponseEntity.ok(updatedDemande);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        try {
            demandeService.deleteDemande(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Demande>> getDemandesByManagerId(@PathVariable Long managerId) {
        List<Demande> demandes = demandeService.getDemandesByManagerId(managerId);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<Demande>> getDemandesByLivreurId(@PathVariable Long livreurId) {
        List<Demande> demandes = demandeService.getDemandesByLivreurId(livreurId);
        return ResponseEntity.ok(demandes);
    }

    @PostMapping("/{demandeId}/assign-livreur")
    public ResponseEntity<Demande> assignLivreurToDemande(
            @PathVariable Long demandeId, 
            @RequestParam Long livreurId, 
            @RequestParam Long managerId) {
        try {
            Demande updatedDemande = demandeService.assignLivreurToDemande(demandeId, livreurId, managerId);
            return ResponseEntity.ok(updatedDemande);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<Demande>> getUnassignedDemandes() {
        List<Demande> demandes = demandeService.getUnassignedDemandes();
        return ResponseEntity.ok(demandes);
    }
}
