package com.eventbooking.repository;

import com.eventbooking.model.Role;
import com.eventbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by email and password (for simple authentication)
    Optional<User> findByEmailAndPassword(String email, String password);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find users by role
    List<User> findByRole(Role role);

    // Find users by name containing (for search)
    List<User> findByNameContainingIgnoreCase(String name);

    // Count total users
    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUsers();

    // Count users by role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countUsersByRole(@Param("role") Role role);
}