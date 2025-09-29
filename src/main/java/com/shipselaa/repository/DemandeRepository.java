package com.shipselaa.repository;

import com.shipselaa.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

    List<Demande> findByManagerId(Long managerId);
    
    List<Demande> findByLivreurId(Long livreurId);
    

    List<Demande> findByLivreurIsNull();

}