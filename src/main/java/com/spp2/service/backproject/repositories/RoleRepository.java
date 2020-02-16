package com.spp2.service.backproject.repositories;

import com.spp2.service.backproject.entities.ERole;
import com.spp2.service.backproject.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
