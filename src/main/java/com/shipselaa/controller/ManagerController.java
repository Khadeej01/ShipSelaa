package com.shipselaa.controller;

import com.shipselaa.model.Demande;
import com.shipselaa.model.Manager;
import com.shipselaa.service.DemandeService;
import com.shipselaa.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private DemandeService demandeService;


    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long id) {
        Optional<Manager> manager = managerService.getManagerById(id);
        return manager.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }




    @PostMapping("/{managerId}/demandes")
    public ResponseEntity<Demande> createDemande(@PathVariable Long managerId, @RequestBody Demande demande) {
        Demande createdDemande = demandeService.createDemande(demande, managerId);
        if (createdDemande != null) {
            return ResponseEntity.ok(createdDemande);
        }
        return ResponseEntity.badRequest().build();
    }


}
