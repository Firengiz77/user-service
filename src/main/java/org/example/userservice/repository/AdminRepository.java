package org.example.userservice.repository;

import org.example.userservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);
    Admin findByUsername(String username);

    Admin findByEmail(String email);
}
