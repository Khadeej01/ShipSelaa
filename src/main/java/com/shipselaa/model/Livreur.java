package com.shipselaa.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Livreur extends User {
    
    private boolean disponible;
    
    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demande> demandes;
}

