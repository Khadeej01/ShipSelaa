package com.shipselaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipselaa.model.*;
import com.shipselaa.repository.DemandeRepository;
import com.shipselaa.repository.LivreurRepository;
import com.shipselaa.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class DemandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    private Manager testManager;
    private Livreur testLivreur;
    private Demande testDemande;

    @BeforeEach
    void setUp() {
        // Clean up existing data
        demandeRepository.deleteAll();
        livreurRepository.deleteAll();
        managerRepository.deleteAll();

        // Create test manager
        testManager = new Manager();
        testManager.setNom("Test Manager");
        testManager.setEmail("manager@test.com");
        testManager.setPassword("password");
        testManager = managerRepository.save(testManager);

        // Create test livreur
        testLivreur = new Livreur();
        testLivreur.setNom("Test Livreur");
        testLivreur.setEmail("livreur@test.com");
        testLivreur.setPassword("password");
        testLivreur.setDisponible(true);
        testLivreur = livreurRepository.save(testLivreur);

        // Create test demande
        testDemande = new Demande();
        testDemande.setLieuDepart("Paris");
        testDemande.setLieuArrivee("Lyon");
        testDemande.setStatut(StatusDemande.CREATED);
        testDemande.setManager(testManager);
        testDemande.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllDemandes() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(get("/api/demandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(savedDemande.getId().intValue())))
                .andExpect(jsonPath("$[0].lieuDepart", is("Paris")))
                .andExpect(jsonPath("$[0].lieuArrivee", is("Lyon")))
                .andExpect(jsonPath("$[0].statut", is("CREATED")));
    }

    @Test
    void testGetDemandeById_ExistingId() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(get("/api/demandes/{id}", savedDemande.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedDemande.getId().intValue())))
                .andExpect(jsonPath("$.lieuDepart", is("Paris")))
                .andExpect(jsonPath("$.lieuArrivee", is("Lyon")))
                .andExpect(jsonPath("$.statut", is("CREATED")));
    }

    @Test
    void testGetDemandeById_NonExistingId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/demandes/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDemande_Success() throws Exception {
        // Given
        Demande newDemande = new Demande();
        newDemande.setLieuDepart("Marseille");
        newDemande.setLieuArrivee("Nice");

        String requestBody = objectMapper.writeValueAsString(newDemande);

        // When & Then
        mockMvc.perform(post("/api/demandes")
                        .param("managerId", testManager.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lieuDepart", is("Marseille")))
                .andExpect(jsonPath("$.lieuArrivee", is("Nice")))
                .andExpect(jsonPath("$.statut", is("CREATED")))
                .andExpect(jsonPath("$.manager.id", is(testManager.getId().intValue())));
    }

    @Test
    void testCreateDemande_NonExistingManager() throws Exception {
        // Given
        Demande newDemande = new Demande();
        newDemande.setLieuDepart("Marseille");
        newDemande.setLieuArrivee("Nice");

        String requestBody = objectMapper.writeValueAsString(newDemande);

        // When & Then
        mockMvc.perform(post("/api/demandes")
                        .param("managerId", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateDemande_Success() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);
        
        Demande updateData = new Demande();
        updateData.setLieuDepart("Toulouse");
        updateData.setLieuArrivee("Bordeaux");
        updateData.setStatut(StatusDemande.ASSIGNED);

        String requestBody = objectMapper.writeValueAsString(updateData);

        // When & Then
        mockMvc.perform(put("/api/demandes/{id}", savedDemande.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedDemande.getId().intValue())))
                .andExpect(jsonPath("$.lieuDepart", is("Toulouse")))
                .andExpect(jsonPath("$.lieuArrivee", is("Bordeaux")))
                .andExpect(jsonPath("$.statut", is("ASSIGNED")));
    }

    @Test
    void testUpdateDemande_NonExistingId() throws Exception {
        // Given
        Demande updateData = new Demande();
        updateData.setLieuDepart("Toulouse");
        updateData.setLieuArrivee("Bordeaux");

        String requestBody = objectMapper.writeValueAsString(updateData);

        // When & Then
        mockMvc.perform(put("/api/demandes/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDemande_Success() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(delete("/api/demandes/{id}", savedDemande.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/demandes/{id}", savedDemande.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDemande_NonExistingId() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/demandes/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDemandesByManagerId() throws Exception {
        // Given
        Manager anotherManager = new Manager();
        anotherManager.setNom("Another Manager");
        anotherManager.setEmail("manager2@test.com");
        anotherManager.setPassword("password");
        Manager savedAnotherManager = managerRepository.save(anotherManager);

        Demande demande1 = demandeRepository.save(testDemande);
        
        Demande demande2 = new Demande();
        demande2.setLieuDepart("Lille");
        demande2.setLieuArrivee("Strasbourg");
        demande2.setStatut(StatusDemande.CREATED);
        demande2.setManager(savedAnotherManager);
        demande2.setCreatedAt(LocalDateTime.now());
        demandeRepository.save(demande2);

        // When & Then
        mockMvc.perform(get("/api/demandes/manager/{managerId}", testManager.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(demande1.getId().intValue())))
                .andExpect(jsonPath("$[0].manager.id", is(testManager.getId().intValue())));
    }

    @Test
    void testGetDemandesByLivreurId() throws Exception {
        // Given
        testDemande.setLivreur(testLivreur);
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(get("/api/demandes/livreur/{livreurId}", testLivreur.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(savedDemande.getId().intValue())))
                .andExpect(jsonPath("$[0].livreur.id", is(testLivreur.getId().intValue())));
    }

    @Test
    void testAssignLivreurToDemande_Success() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(post("/api/demandes/{demandeId}/assign-livreur", savedDemande.getId())
                        .param("livreurId", testLivreur.getId().toString())
                        .param("managerId", testManager.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedDemande.getId().intValue())))
                .andExpect(jsonPath("$.livreur.id", is(testLivreur.getId().intValue())))
                .andExpect(jsonPath("$.statut", is("ASSIGNED")));
    }

    @Test
    void testAssignLivreurToDemande_NonExistingDemande() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/demandes/{demandeId}/assign-livreur", 999)
                        .param("livreurId", testLivreur.getId().toString())
                        .param("managerId", testManager.getId().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAssignLivreurToDemande_NonExistingLivreur() throws Exception {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(post("/api/demandes/{demandeId}/assign-livreur", savedDemande.getId())
                        .param("livreurId", "999")
                        .param("managerId", testManager.getId().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUnassignedDemandes() throws Exception {
        // Given
        Demande unassignedDemande = demandeRepository.save(testDemande);
        
        Demande assignedDemande = new Demande();
        assignedDemande.setLieuDepart("Nancy");
        assignedDemande.setLieuArrivee("Metz");
        assignedDemande.setStatut(StatusDemande.ASSIGNED);
        assignedDemande.setManager(testManager);
        assignedDemande.setLivreur(testLivreur);
        assignedDemande.setCreatedAt(LocalDateTime.now());
        demandeRepository.save(assignedDemande);

        // When & Then
        mockMvc.perform(get("/api/demandes/unassigned"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(unassignedDemande.getId().intValue())))
                .andExpect(jsonPath("$[0].livreur").doesNotExist());
    }

    @Test
    void testGetAllDemandes_EmptyResult() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/demandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetDemandesByManagerId_EmptyResult() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/demandes/manager/{managerId}", 999))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetDemandesByLivreurId_EmptyResult() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/demandes/livreur/{livreurId}", 999))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetUnassignedDemandes_EmptyResult() throws Exception {
        // Given - no unassigned demandes
        testDemande.setLivreur(testLivreur);
        demandeRepository.save(testDemande);

        // When & Then
        mockMvc.perform(get("/api/demandes/unassigned"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}