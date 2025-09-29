package com.shipselaa.service;

import com.shipselaa.model.Livreur;
import com.shipselaa.repository.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    public Optional<Livreur> getLivreurById(Long id) {
        return livreurRepository.findById(id);
    }

    public Livreur getLivreurByEmail(String email) {
        return livreurRepository.findByEmail(email);
    }

    public List<Livreur> getAvailableLivreurs() {
        return livreurRepository.findByDisponible(true);
    }

    public Livreur saveLivreur(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    public Livreur updateAvailability(Long id, boolean disponible) {
        Optional<Livreur> livreurOpt = livreurRepository.findById(id);
        if (livreurOpt.isPresent()) {
            Livreur livreur = livreurOpt.get();
            livreur.setDisponible(disponible);
            return livreurRepository.save(livreur);
        }
        return null;
    }

    public void deleteLivreur(Long id) {
        livreurRepository.deleteById(id);
    }
}