package com.eventbooking.dto;

import com.eventbooking.model.EventType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private EventType type;
    private String tags;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private Integer totalTickets;
    private Integer ticketsSold;
    private BigDecimal basePrice;
    private BigDecimal currentPrice;
    private String eventImage;
    private Integer availableTickets;
    private Double soldPercentage;

    // Constructors
    public EventDTO() {}

    public EventDTO(Long id, String name, String description, EventType type, String tags,
                    LocalDate date, LocalTime time, String location, Integer totalTickets,
                    Integer ticketsSold, BigDecimal basePrice, BigDecimal currentPrice, String eventImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.tags = tags;
        this.date = date;
        this.time = time;
        this.location = location;
        this.totalTickets = totalTickets;
        this.ticketsSold = ticketsSold;
        this.basePrice = basePrice;
        this.currentPrice = currentPrice;
        this.eventImage = eventImage;
        this.availableTickets = totalTickets - ticketsSold;
        this.soldPercentage = totalTickets > 0 ? (double) ticketsSold / totalTickets * 100 : 0.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getTotalTickets() { return totalTickets; }
    public void setTotalTickets(Integer totalTickets) { this.totalTickets = totalTickets; }

    public Integer getTicketsSold() { return ticketsSold; }
    public void setTicketsSold(Integer ticketsSold) { this.ticketsSold = ticketsSold; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public String getEventImage() { return eventImage; }
    public void setEventImage(String eventImage) { this.eventImage = eventImage; }

    public Integer getAvailableTickets() { return availableTickets; }
    public void setAvailableTickets(Integer availableTickets) { this.availableTickets = availableTickets; }

    public Double getSoldPercentage() { return soldPercentage; }
    public void setSoldPercentage(Double soldPercentage) { this.soldPercentage = soldPercentage; }
}