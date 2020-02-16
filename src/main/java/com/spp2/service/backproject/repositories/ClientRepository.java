package com.spp2.service.backproject.repositories;

import com.spp2.service.backproject.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String username);

    Boolean existsByUsername(String username);
}
