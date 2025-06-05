package com.eventbooking.dto;

import com.eventbooking.model.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private UserDTO user;
    private EventDTO event;
    private Integer ticketsBooked;
    private BigDecimal totalAmount;
    private String qrCode;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private String bookingReference;

    // Constructors
    public BookingDTO() {}

    public BookingDTO(Long id, UserDTO user, EventDTO event, Integer ticketsBooked,
                      BigDecimal totalAmount, String qrCode, LocalDateTime bookingDate,
                      BookingStatus status, String bookingReference) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.ticketsBooked = ticketsBooked;
        this.totalAmount = totalAmount;
        this.qrCode = qrCode;
        this.bookingDate = bookingDate;
        this.status = status;
        this.bookingReference = bookingReference;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public EventDTO getEvent() { return event; }
    public void setEvent(EventDTO event) { this.event = event; }

    public Integer getTicketsBooked() { return ticketsBooked; }
    public void setTicketsBooked(Integer ticketsBooked) { this.ticketsBooked = ticketsBooked; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
}