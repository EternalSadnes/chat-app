package com.eternal.chatapp.repository;

import java.util.Optional;

import com.eternal.chatapp.model.Role;
import com.eternal.chatapp.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRole userRole);
}
