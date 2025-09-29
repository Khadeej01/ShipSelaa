package com.shipselaa.repository;

import com.shipselaa.model.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Livreur findByEmail(String email);
    List<Livreur> findByDisponible(boolean disponible);
}