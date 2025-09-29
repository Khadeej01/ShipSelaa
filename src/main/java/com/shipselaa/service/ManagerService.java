package com.shipselaa.service;

import com.shipselaa.model.Manager;
import com.shipselaa.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(Long id) {
        return managerRepository.findById(id);
    }

    public Manager getManagerByEmail(String email) {
        return managerRepository.findByEmail(email);
    }

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }
}