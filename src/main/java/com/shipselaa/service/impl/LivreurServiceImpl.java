package com.shipselaa.service.impl;

import com.shipselaa.model.Livreur;
import com.shipselaa.repository.LivreurRepository;
import com.shipselaa.service.LivreurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;

    @Autowired
    public LivreurServiceImpl(LivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livreur> getLivreurById(Long id) {
        return livreurRepository.findById(id);
    }

    @Override
    public Livreur createLivreur(Livreur livreur) {

        Livreur existingLivreur = livreurRepository.findByEmail(livreur.getEmail());
        if (existingLivreur != null) {
            throw new RuntimeException("Livreur with email " + livreur.getEmail() + " already exists");
        }
        

        livreur.setDisponible(true);
        
        return livreurRepository.save(livreur);
    }

    @Override
    public Livreur updateLivreur(Long id, Livreur livreur) {
        Optional<Livreur> existingLivreurOpt = livreurRepository.findById(id);
        if (existingLivreurOpt.isEmpty()) {
            throw new RuntimeException("Livreur not found with id: " + id);
        }
        
        Livreur existingLivreur = existingLivreurOpt.get();
        

        existingLivreur.setNom(livreur.getNom());
        existingLivreur.setEmail(livreur.getEmail());
        existingLivreur.setPassword(livreur.getPassword());
        existingLivreur.setDisponible(livreur.isDisponible());
        
        return livreurRepository.save(existingLivreur);
    }

    @Override
    public void deleteLivreur(Long id) {
        if (!livreurRepository.existsById(id)) {
            throw new RuntimeException("Livreur not found with id: " + id);
        }
        livreurRepository.deleteById(id);
    }
}