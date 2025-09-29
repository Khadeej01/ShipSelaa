package com.shipselaa.repository;

import com.shipselaa.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

}