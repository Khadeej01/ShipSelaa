package com.shipselaa.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lieuDepart;
    private String lieuArrivee;
    
    @Enumerated(EnumType.STRING)
    private StatusDemande statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;
}
