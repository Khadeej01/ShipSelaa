package com.shipselaa.service;

import com.shipselaa.model.Demande;
import com.shipselaa.model.Manager;
import com.shipselaa.model.Livreur;
import com.shipselaa.model.StatusDemande;
import com.shipselaa.repository.DemandeRepository;
import com.shipselaa.repository.ManagerRepository;
import com.shipselaa.repository.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }

    public List<Demande> getDemandesByManager(Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        return manager.map(demandeRepository::findByManager).orElse(List.of());
    }

    public List<Demande> getDemandesByLivreur(Long livreurId) {
        Optional<Livreur> livreur = livreurRepository.findById(livreurId);
        return livreur.map(demandeRepository::findByLivreur).orElse(List.of());
    }

    public List<Demande> getDemandesByStatus(StatusDemande statut) {
        return demandeRepository.findByStatut(statut);
    }

    public Demande createDemande(Demande demande, Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isPresent()) {
            demande.setManager(manager.get());
            demande.setStatut(StatusDemande.EN_COURS);
            return demandeRepository.save(demande);
        }
        return null;
    }

    public Demande updateDemande(Long id, Demande demandeDetails) {
        Optional<Demande> demandeOpt = demandeRepository.findById(id);
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            demande.setLieuDepart(demandeDetails.getLieuDepart());
            demande.setLieuArrivee(demandeDetails.getLieuArrivee());
            return demandeRepository.save(demande);
        }
        return null;
    }

    public Demande assignLivreur(Long demandeId, Long livreurId) {
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        Optional<Livreur> livreurOpt = livreurRepository.findById(livreurId);
        
        if (demandeOpt.isPresent() && livreurOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            Livreur livreur = livreurOpt.get();
            
            if (livreur.isDisponible()) {
                demande.setLivreur(livreur);
                livreur.setDisponible(false);
                livreurRepository.save(livreur);
                return demandeRepository.save(demande);
            }
        }
        return null;
    }

    public Demande updateStatus(Long demandeId, StatusDemande newStatus) {
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            demande.setStatut(newStatus);
            
            // If order is completed, make livreur available again
            if (newStatus == StatusDemande.LIVRE && demande.getLivreur() != null) {
                Livreur livreur = demande.getLivreur();
                livreur.setDisponible(true);
                livreurRepository.save(livreur);
            }
            
            return demandeRepository.save(demande);
        }
        return null;
    }

    public boolean deleteDemande(Long id) {
        if (demandeRepository.existsById(id)) {
            demandeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}