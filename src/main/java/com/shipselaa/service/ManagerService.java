package com.shipselaa.service;

import com.shipselaa.model.Manager;
import java.util.List;
import java.util.Optional;

public interface ManagerService {

    List<Manager> getAllManagers();

    Optional<Manager> getManagerById(Long id);

    Manager createManager(Manager manager);

    Manager updateManager(Long id, Manager manager);

    void deleteManager(Long id);
}