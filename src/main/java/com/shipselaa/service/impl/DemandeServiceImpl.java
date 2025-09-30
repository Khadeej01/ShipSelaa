package com.shipselaa.service.impl;

import com.shipselaa.model.Demande;
import com.shipselaa.model.Livreur;
import com.shipselaa.model.Manager;
import com.shipselaa.model.StatusDemande;
import com.shipselaa.repository.DemandeRepository;
import com.shipselaa.repository.LivreurRepository;
import com.shipselaa.repository.ManagerRepository;
import com.shipselaa.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final LivreurRepository livreurRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public DemandeServiceImpl(DemandeRepository demandeRepository, 
                             LivreurRepository livreurRepository,
                             ManagerRepository managerRepository) {
        this.demandeRepository = demandeRepository;
        this.livreurRepository = livreurRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }

    @Override
    public Demande createDemandeByManager(Long managerId, Demande demande) {
        Optional<Manager> managerOpt = managerRepository.findById(managerId);
        if (managerOpt.isEmpty()) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }
        
        Manager manager = managerOpt.get();
        demande.setManager(manager);
        demande.setStatut(StatusDemande.CREATED);
        demande.setCreatedAt(LocalDateTime.now());
        
        return demandeRepository.save(demande);
    }

    @Override
    public Demande updateDemande(Long id, Demande demande) {
        Optional<Demande> existingDemandeOpt = demandeRepository.findById(id);
        if (existingDemandeOpt.isEmpty()) {
            throw new RuntimeException("Demande not found with id: " + id);
        }
        
        Demande existingDemande = existingDemandeOpt.get();
        
        // Update only the allowed fields
        existingDemande.setLieuDepart(demande.getLieuDepart());
        existingDemande.setLieuArrivee(demande.getLieuArrivee());
        existingDemande.setStatut(demande.getStatut());
        
        return demandeRepository.save(existingDemande);
    }

    @Override
    public void deleteDemande(Long id) {
        if (!demandeRepository.existsById(id)) {
            throw new RuntimeException("Demande not found with id: " + id);
        }
        demandeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getDemandesByManagerId(Long managerId) {
        return demandeRepository.findByManagerId(managerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getDemandesByLivreurId(Long livreurId) {
        return demandeRepository.findByLivreurId(livreurId);
    }

    @Override
    public Demande assignLivreurToDemande(Long demandeId, Long livreurId, Long managerId) {

        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        if (demandeOpt.isEmpty()) {
            throw new RuntimeException("Demande not found with id: " + demandeId);
        }
        
        Demande demande = demandeOpt.get();
        

        if (!demande.getManager().getId().equals(managerId)) {
            throw new RuntimeException("Manager does not own this demande");
        }
        

        Optional<Livreur> livreurOpt = livreurRepository.findById(livreurId);
        if (livreurOpt.isEmpty()) {
            throw new RuntimeException("Livreur not found with id: " + livreurId);
        }
        
        Livreur livreur = livreurOpt.get();
        
        if (!livreur.isDisponible()) {
            throw new RuntimeException("Livreur is not available");
        }
        

        demande.setLivreur(livreur);
        demande.setStatut(StatusDemande.ASSIGNED);
        demande.setAssignedAt(LocalDateTime.now());
        

        livreur.setDisponible(false);
        livreurRepository.save(livreur);
        
        return demandeRepository.save(demande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getUnassignedDemandes() {
        return demandeRepository.findByLivreurIsNull();
    }
}