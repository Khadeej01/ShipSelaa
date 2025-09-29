package com.shipselaa.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) {
            statut = StatusDemande.CREATED;
        }
    }
}
