package com.eventbooking.repository;

import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by user ID
    List<Booking> findByUserId(Long userId);

    // Find bookings by event ID
    List<Booking> findByEventId(Long eventId);

    // Find booking by booking reference
    Optional<Booking> findByBookingReference(String bookingReference);

    // Find booking by QR code
    Optional<Booking> findByQrCode(String qrCode);

    // Find bookings by status
    List<Booking> findByStatus(BookingStatus status);

    // Find bookings by user and status
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    // Find bookings by event and status
    List<Booking> findByEventIdAndStatus(Long eventId, BookingStatus status);

    // Find bookings by date range
    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Get total bookings count for an event
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.event.id = :eventId AND b.status = 'CONFIRMED'")
    Integer countConfirmedBookingsByEventId(@Param("eventId") Long eventId);

    // Get total tickets sold for an event
    @Query("SELECT SUM(b.ticketsBooked) FROM Booking b WHERE b.event.id = :eventId AND b.status = 'CONFIRMED'")
    Integer getTotalTicketsSoldByEventId(@Param("eventId") Long eventId);

    // Get total revenue
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal getTotalRevenue();

    // Get total revenue by user
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.user.id = :userId AND b.status = 'CONFIRMED'")
    BigDecimal getTotalRevenueByUserId(@Param("userId") Long userId);

    // Get recent bookings
    @Query("SELECT b FROM Booking b ORDER BY b.bookingDate DESC")
    List<Booking> findRecentBookings();

    // Check if user has booked a specific event
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.user.id = :userId AND b.event.id = :eventId AND b.status = 'CONFIRMED'")
    boolean hasUserBookedEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);
}