package com.shipselaa.dto;

import com.shipselaa.model.StatusDemande;
import lombok.Data;

@Data
public class DemandeDTO {
    private Long id;
    private String lieuDepart;
    private String lieuArrivee;
    private StatusDemande statut;
    private Long managerId;
    private String managerName;
    private Long livreurId;
    private String livreurName;
}