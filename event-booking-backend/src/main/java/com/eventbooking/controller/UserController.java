package com.eventbooking.controller;

import com.eventbooking.model.Role;
import com.eventbooking.model.User;
import com.eventbooking.service.UserService;
import com.eventbooking.dto.DTOMapper;
import com.eventbooking.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            UserDTO userDTO = dtoMapper.toUserDTO(registeredUser);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("user", userDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Login user - FIXED VERSION
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || password == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email and password are required");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<User> userOpt = userService.authenticateUser(email, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserDTO userDTO = dtoMapper.toUserDTO(user); // Convert to DTO
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", userDTO); // Use DTO instead of Entity
                response.put("role", user.getRole().toString());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get all users (Admin only)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(dtoMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            UserDTO userDTO = dtoMapper.toUserDTO(userOpt.get());
            return ResponseEntity.ok(userDTO);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (!userOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }

            User existingUser = userOpt.get();
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());

            // Only update password if provided
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                existingUser.setPassword(userDetails.getPassword());
            }

            // Only update role if provided
            if (userDetails.getRole() != null) {
                existingUser.setRole(userDetails.getRole());
            }

            // Update profile image if provided
            if (userDetails.getProfileImage() != null) {
                existingUser.setProfileImage(userDetails.getProfileImage());
            }

            User updatedUser = userService.updateUser(existingUser);
            UserDTO userDTO = dtoMapper.toUserDTO(updatedUser);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("user", userDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userService.findById(id).isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }

            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<User> users = userService.getUsersByRole(userRole);
            List<UserDTO> userDTOs = users.stream()
                    .map(dtoMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        List<UserDTO> userDTOs = users.stream()
                .map(dtoMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Check if email exists
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Get user statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUserCount());
        stats.put("totalAdmins", userService.getUserCountByRole(Role.ADMIN));
        stats.put("totalRegularUsers", userService.getUserCountByRole(Role.USER));
        return ResponseEntity.ok(stats);
    }

    // Create admin user (for testing)
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> adminData) {
        try {
            String name = adminData.get("name");
            String email = adminData.get("email");
            String password = adminData.get("password");

            User admin = userService.createAdminUser(name, email, password);
            UserDTO adminDTO = dtoMapper.toUserDTO(admin);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Admin user created successfully");
            response.put("admin", adminDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}