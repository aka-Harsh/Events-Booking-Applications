package com.eventbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Positive(message = "Number of tickets must be positive")
    @Column(name = "tickets_booked", nullable = false)
    private Integer ticketsBooked;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "qr_code", length = 500)
    private String qrCode;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "booking_reference", unique = true)
    private String bookingReference;

    // Constructors
    public Booking() {
        this.bookingDate = LocalDateTime.now();
    }

    public Booking(User user, Event event, Integer ticketsBooked, BigDecimal totalAmount) {
        this();
        this.user = user;
        this.event = event;
        this.ticketsBooked = ticketsBooked;
        this.totalAmount = totalAmount;
        this.bookingReference = generateBookingReference();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(Integer ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    // Helper method to generate booking reference
    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", ticketsBooked=" + ticketsBooked +
                ", totalAmount=" + totalAmount +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                ", bookingReference='" + bookingReference + '\'' +
                '}';
    }
}