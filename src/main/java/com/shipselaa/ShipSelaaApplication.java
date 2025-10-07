package com.shipselaa;

import com.shipselaa.model.Manager;
import com.shipselaa.model.Livreur;
import com.shipselaa.repository.ManagerRepository;
import com.shipselaa.repository.LivreurRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShipSelaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipSelaaApplication.class, args);
    }

    @Bean
    public org.springframework.boot.ApplicationRunner initDefaultData(ManagerRepository managerRepository, LivreurRepository livreurRepository) {
        return args -> {
            if (managerRepository.count() == 0) {
                Manager manager = new Manager();
                manager.setNom("Default Manager");
                manager.setEmail("manager@shipselaa.local");
                manager.setPassword("admin");
                managerRepository.save(manager);
            }

            if (livreurRepository.count() == 0) {
                Livreur l1 = new Livreur(); l1.setNom("Alice"); l1.setEmail("alice@shipselaa.local"); l1.setPassword("pwd"); l1.setDisponible(true);
                Livreur l2 = new Livreur(); l2.setNom("Bob"); l2.setEmail("bob@shipselaa.local"); l2.setPassword("pwd"); l2.setDisponible(true);
                Livreur l3 = new Livreur(); l3.setNom("Charlie"); l3.setEmail("charlie@shipselaa.local"); l3.setPassword("pwd"); l3.setDisponible(true);
                Livreur l4 = new Livreur(); l4.setNom("Diana"); l4.setEmail("diana@shipselaa.local"); l4.setPassword("pwd"); l4.setDisponible(true);
                livreurRepository.save(l1);
                livreurRepository.save(l2);
                livreurRepository.save(l3);
                livreurRepository.save(l4);
            }
        };
    }
}
