package com.eventbooking.repository;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find events by type
    List<Event> findByType(EventType type);

    // Find events by date
    List<Event> findByDate(LocalDate date);

    // Find events by date range
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Find events by location
    List<Event> findByLocationContainingIgnoreCase(String location);

    // Find events by name
    List<Event> findByNameContainingIgnoreCase(String name);

    // Find upcoming events
    List<Event> findByDateGreaterThanEqualOrderByDateAsc(LocalDate date);

    // Find events with available tickets
    @Query("SELECT e FROM Event e WHERE e.totalTickets > e.ticketsSold")
    List<Event> findEventsWithAvailableTickets();

    // Find events by tags containing
    @Query("SELECT e FROM Event e WHERE e.tags LIKE %:tag%")
    List<Event> findByTagsContaining(@Param("tag") String tag);

    // Find popular events (high booking rate)
    @Query("SELECT e FROM Event e WHERE (e.ticketsSold * 100.0 / e.totalTickets) >= :percentage ORDER BY e.ticketsSold DESC")
    List<Event> findPopularEvents(@Param("percentage") double percentage);

    // Get total revenue for an event
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.event.id = :eventId AND b.status = 'CONFIRMED'")
    Double getTotalRevenueByEventId(@Param("eventId") Long eventId);

    // Count total events
    @Query("SELECT COUNT(e) FROM Event e")
    Long countTotalEvents();
}