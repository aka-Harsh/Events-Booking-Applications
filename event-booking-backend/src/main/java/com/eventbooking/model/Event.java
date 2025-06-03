package com.eventbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(name = "tags")
    private String tags; // Stored as comma-separated values

    @NotNull(message = "Event date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Event time is required")
    @Column(nullable = false)
    private LocalTime time;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Positive(message = "Total tickets must be positive")
    @Column(name = "total_tickets", nullable = false)
    private Integer totalTickets;

    @Column(name = "tickets_sold")
    private Integer ticketsSold = 0;

    @Positive(message = "Base price must be positive")
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "current_price", precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "event_image")
    private String eventImage = "img2"; // Default placeholder

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;


    // Constructors
    public Event() {
    }

    public Event(String name, String description, EventType type, String tags,
                 LocalDate date, LocalTime time, String location,
                 Integer totalTickets, BigDecimal basePrice) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.tags = tags;
        this.date = date;
        this.time = time;
        this.location = location;
        this.totalTickets = totalTickets;
        this.basePrice = basePrice;
        this.currentPrice = basePrice; // Initially same as base price
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(Integer totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Helper method to get available tickets
    public Integer getAvailableTickets() {
        return totalTickets - ticketsSold;
    }

    // Helper method to get sold percentage
    public double getSoldPercentage() {
        if (totalTickets == 0) return 0.0;
        return (double) ticketsSold / totalTickets * 100;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", totalTickets=" + totalTickets +
                ", ticketsSold=" + ticketsSold +
                ", currentPrice=" + currentPrice +
                '}';
    }
}