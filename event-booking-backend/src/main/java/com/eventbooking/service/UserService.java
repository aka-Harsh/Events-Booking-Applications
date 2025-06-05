package com.eventbooking.service;

import com.eventbooking.model.Role;
import com.eventbooking.model.User;
import com.eventbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // Set default profile image
        if (user.getProfileImage() == null || user.getProfileImage().isEmpty()) {
            user.setProfileImage("img1");
        }

        return userRepository.save(user);
    }

    // Authenticate user (simple password check)
    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get users by role
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    // Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Search users by name
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    // Get total user count
    public Long getTotalUserCount() {
        return userRepository.countTotalUsers();
    }

    // Get user count by role
    public Long getUserCountByRole(Role role) {
        return userRepository.countUsersByRole(role);
    }

    // Create admin user (helper method)
    public User createAdminUser(String name, String email, String password) {
        User admin = new User(name, email, password, Role.ADMIN);
        admin.setProfileImage("img3");
        return registerUser(admin);
    }

    // Validate user data
    private void validateUserData(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
}