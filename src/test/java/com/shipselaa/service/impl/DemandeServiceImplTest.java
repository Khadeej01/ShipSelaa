package com.shipselaa.service.impl;

import com.shipselaa.model.*;
import com.shipselaa.repository.DemandeRepository;
import com.shipselaa.repository.LivreurRepository;
import com.shipselaa.repository.ManagerRepository;
import com.shipselaa.service.DemandeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DemandeServiceImplTest {

    @Autowired
    private DemandeService demandeService;

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
    void testGetAllDemandes() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When
        List<Demande> result = demandeService.getAllDemandes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedDemande.getId(), result.get(0).getId());
        assertEquals("Paris", result.get(0).getLieuDepart());
        assertEquals("Lyon", result.get(0).getLieuArrivee());
    }

    @Test
    void testGetDemandeById_ExistingId() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When
        Optional<Demande> result = demandeService.getDemandeById(savedDemande.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedDemande.getId(), result.get().getId());
        assertEquals("Paris", result.get().getLieuDepart());
        assertEquals("Lyon", result.get().getLieuArrivee());
    }

    @Test
    void testGetDemandeById_NonExistingId() {
        // When
        Optional<Demande> result = demandeService.getDemandeById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateDemandeByManager_Success() {
        // Given
        Demande newDemande = new Demande();
        newDemande.setLieuDepart("Marseille");
        newDemande.setLieuArrivee("Nice");

        // When
        Demande result = demandeService.createDemandeByManager(testManager.getId(), newDemande);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Marseille", result.getLieuDepart());
        assertEquals("Nice", result.getLieuArrivee());
        assertEquals(StatusDemande.CREATED, result.getStatut());
        assertEquals(testManager.getId(), result.getManager().getId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void testCreateDemandeByManager_NonExistingManager() {
        // Given
        Demande newDemande = new Demande();
        newDemande.setLieuDepart("Marseille");
        newDemande.setLieuArrivee("Nice");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.createDemandeByManager(999L, newDemande));
        
        assertTrue(exception.getMessage().contains("Manager not found"));
    }

    @Test
    void testUpdateDemande_Success() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);
        
        Demande updateData = new Demande();
        updateData.setLieuDepart("Toulouse");
        updateData.setLieuArrivee("Bordeaux");
        updateData.setStatut(StatusDemande.ASSIGNED);

        // When
        Demande result = demandeService.updateDemande(savedDemande.getId(), updateData);

        // Then
        assertNotNull(result);
        assertEquals(savedDemande.getId(), result.getId());
        assertEquals("Toulouse", result.getLieuDepart());
        assertEquals("Bordeaux", result.getLieuArrivee());
        assertEquals(StatusDemande.ASSIGNED, result.getStatut());
    }

    @Test
    void testUpdateDemande_NonExistingId() {
        // Given
        Demande updateData = new Demande();
        updateData.setLieuDepart("Toulouse");
        updateData.setLieuArrivee("Bordeaux");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.updateDemande(999L, updateData));
        
        assertTrue(exception.getMessage().contains("Demande not found"));
    }

    @Test
    void testDeleteDemande_Success() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);
        Long demandeId = savedDemande.getId();

        // When
        demandeService.deleteDemande(demandeId);

        // Then
        Optional<Demande> result = demandeRepository.findById(demandeId);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteDemande_NonExistingId() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.deleteDemande(999L));
        
        assertTrue(exception.getMessage().contains("Demande not found"));
    }

    @Test
    void testGetDemandesByManagerId() {
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
        demande2 = demandeRepository.save(demande2);

        // When
        List<Demande> result = demandeService.getDemandesByManagerId(testManager.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(demande1.getId(), result.get(0).getId());
        assertEquals(testManager.getId(), result.get(0).getManager().getId());
    }

    @Test
    void testGetDemandesByLivreurId() {
        // Given
        testDemande.setLivreur(testLivreur);
        Demande savedDemande = demandeRepository.save(testDemande);

        // When
        List<Demande> result = demandeService.getDemandesByLivreurId(testLivreur.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedDemande.getId(), result.get(0).getId());
        assertEquals(testLivreur.getId(), result.get(0).getLivreur().getId());
    }

    @Test
    void testAssignLivreurToDemande_Success() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When
        Demande result = demandeService.assignLivreurToDemande(
            savedDemande.getId(), 
            testLivreur.getId(), 
            testManager.getId()
        );

        // Then
        assertNotNull(result);
        assertEquals(savedDemande.getId(), result.getId());
        assertEquals(testLivreur.getId(), result.getLivreur().getId());
        assertEquals(StatusDemande.ASSIGNED, result.getStatut());
        assertNotNull(result.getAssignedAt());
        
        // Verify livreur availability is updated
        Livreur updatedLivreur = livreurRepository.findById(testLivreur.getId()).orElse(null);
        assertNotNull(updatedLivreur);
        assertFalse(updatedLivreur.isDisponible());
    }

    @Test
    void testAssignLivreurToDemande_NonExistingDemande() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.assignLivreurToDemande(999L, testLivreur.getId(), testManager.getId()));
        
        assertTrue(exception.getMessage().contains("Demande not found"));
    }

    @Test
    void testAssignLivreurToDemande_NonExistingLivreur() {
        // Given
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.assignLivreurToDemande(savedDemande.getId(), 999L, testManager.getId()));
        
        assertTrue(exception.getMessage().contains("Livreur not found"));
    }

    @Test
    void testAssignLivreurToDemande_WrongManager() {
        // Given
        Manager anotherManager = new Manager();
        anotherManager.setNom("Wrong Manager");
        anotherManager.setEmail("wrong@test.com");
        anotherManager.setPassword("password");
        Manager savedAnotherManager = managerRepository.save(anotherManager);

        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.assignLivreurToDemande(
                savedDemande.getId(), 
                testLivreur.getId(), 
                savedAnotherManager.getId()
            ));
        
        assertTrue(exception.getMessage().contains("Manager does not own this demande"));
    }

    @Test
    void testAssignLivreurToDemande_UnavailableLivreur() {
        // Given
        testLivreur.setDisponible(false);
        livreurRepository.save(testLivreur);
        Demande savedDemande = demandeRepository.save(testDemande);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> demandeService.assignLivreurToDemande(
                savedDemande.getId(), 
                testLivreur.getId(), 
                testManager.getId()
            ));
        
        assertTrue(exception.getMessage().contains("Livreur is not available"));
    }

    @Test
    void testGetUnassignedDemandes() {
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

        // When
        List<Demande> result = demandeService.getUnassignedDemandes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(unassignedDemande.getId(), result.get(0).getId());
        assertNull(result.get(0).getLivreur());
    }

    @Test
    void testGetDemandesByManagerId_EmptyResult() {
        // When
        List<Demande> result = demandeService.getDemandesByManagerId(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDemandesByLivreurId_EmptyResult() {
        // When
        List<Demande> result = demandeService.getDemandesByLivreurId(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUnassignedDemandes_EmptyResult() {
        // Given - no unassigned demandes
        testDemande.setLivreur(testLivreur);
        demandeRepository.save(testDemande);

        // When
        List<Demande> result = demandeService.getUnassignedDemandes();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}