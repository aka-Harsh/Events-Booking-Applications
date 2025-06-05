package com.eventbooking.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private UserDTO user;
    private EventDTO event;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

    // Constructors
    public ReviewDTO() {}

    public ReviewDTO(Long id, UserDTO user, EventDTO event, Integer rating,
                     String comment, LocalDateTime reviewDate) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public EventDTO getEvent() { return event; }
    public void setEvent(EventDTO event) { this.event = event; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
}