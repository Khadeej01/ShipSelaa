package com.shipselaa.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {
    
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demande> demandes;
}
