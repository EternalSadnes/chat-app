package com.eternal.chatapp.repository;

import java.util.Optional;

import com.eternal.chatapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
