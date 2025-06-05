package com.eventbooking.dto;

import com.eventbooking.model.Role;
import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdDate;
    private String profileImage;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String name, String email, Role role, LocalDateTime createdDate, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdDate = createdDate;
        this.profileImage = profileImage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}