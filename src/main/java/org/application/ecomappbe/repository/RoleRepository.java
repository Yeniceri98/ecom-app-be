package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.AppRole;
import org.application.ecomappbe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole roleName);
}
