package com.shipselaa.service;

import com.shipselaa.model.Demande;
import java.util.List;
import java.util.Optional;

public interface DemandeService {

    List<Demande> getAllDemandes();

    Optional<Demande> getDemandeById(Long id);


    Demande createDemandeByManager(Long managerId, Demande demande);

    Demande updateDemande(Long id, Demande demande);

    void deleteDemande(Long id);


    List<Demande> getDemandesByManagerId(Long managerId);


    List<Demande> getDemandesByLivreurId(Long livreurId);


    Demande assignLivreurToDemande(Long demandeId, Long livreurId, Long managerId);


    List<Demande> getUnassignedDemandes();
}