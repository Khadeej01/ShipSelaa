package com.shipselaa.service;

import com.shipselaa.model.Livreur;
import java.util.List;
import java.util.Optional;

public interface LivreurService {

    List<Livreur> getAllLivreurs();

    Optional<Livreur> getLivreurById(Long id);

    Livreur createLivreur(Livreur livreur);

    Livreur updateLivreur(Long id, Livreur livreur);

    void deleteLivreur(Long id);
}