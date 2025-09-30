package com.shipselaa.service.impl;

import com.shipselaa.model.Manager;
import com.shipselaa.repository.ManagerRepository;
import com.shipselaa.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Manager> getManagerById(Long id) {
        return managerRepository.findById(id);
    }

    @Override
    public Manager createManager(Manager manager) {

        Manager existingManager = managerRepository.findByEmail(manager.getEmail());
        if (existingManager != null) {
            throw new RuntimeException("Manager with email " + manager.getEmail() + " already exists");
        }
        
        return managerRepository.save(manager);
    }

    @Override
    public Manager updateManager(Long id, Manager manager) {
        Optional<Manager> existingManagerOpt = managerRepository.findById(id);
        if (existingManagerOpt.isEmpty()) {
            throw new RuntimeException("Manager not found with id: " + id);
        }
        
        Manager existingManager = existingManagerOpt.get();
        

        existingManager.setNom(manager.getNom());
        existingManager.setEmail(manager.getEmail());
        existingManager.setPassword(manager.getPassword());
        
        return managerRepository.save(existingManager);
    }

    @Override
    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new RuntimeException("Manager not found with id: " + id);
        }
        managerRepository.deleteById(id);
    }
}