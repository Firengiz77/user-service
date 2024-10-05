package org.example.userservice.repository;

import org.example.userservice.enums.Role;
import org.example.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByRole(Role role);

}
