package com.eventbooking.service;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventType;
import com.eventbooking.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Create new event
    public Event createEvent(Event event) {
        // Set default image if not provided
        if (event.getEventImage() == null || event.getEventImage().isEmpty()) {
            event.setEventImage("img2");
        }

        // Set current price same as base price initially
        if (event.getCurrentPrice() == null) {
            event.setCurrentPrice(event.getBasePrice());
        }

        // Initialize tickets sold to 0
        if (event.getTicketsSold() == null) {
            event.setTicketsSold(0);
        }

        validateEventData(event);
        return eventRepository.save(event);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Update event
    public Event updateEvent(Event event) {
        validateEventData(event);
        return eventRepository.save(event);
    }

    // Delete event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // Get events by type
    public List<Event> getEventsByType(EventType type) {
        return eventRepository.findByType(type);
    }

    // Get events by date
    public List<Event> getEventsByDate(LocalDate date) {
        return eventRepository.findByDate(date);
    }

    // Get events by date range
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByDateBetween(startDate, endDate);
    }

    // Get upcoming events
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateGreaterThanEqualOrderByDateAsc(LocalDate.now());
    }

    // Search events by name
    public List<Event> searchEventsByName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }

    // Search events by location
    public List<Event> searchEventsByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    // Get events with available tickets
    public List<Event> getEventsWithAvailableTickets() {
        return eventRepository.findEventsWithAvailableTickets();
    }

    // Search events by tags
    public List<Event> searchEventsByTag(String tag) {
        return eventRepository.findByTagsContaining(tag);
    }

    // Get popular events
    public List<Event> getPopularEvents(double percentage) {
        return eventRepository.findPopularEvents(percentage);
    }

    // Get total revenue for an event
    public Double getTotalRevenueByEvent(Long eventId) {
        Double revenue = eventRepository.getTotalRevenueByEventId(eventId);
        return revenue != null ? revenue : 0.0;
    }

    // Get total events count
    public Long getTotalEventsCount() {
        return eventRepository.countTotalEvents();
    }

    // Check if event has available tickets
    public boolean hasAvailableTickets(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            return event.getAvailableTickets() > 0;
        }
        return false;
    }

    // Get available tickets count
    public Integer getAvailableTickets(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            return eventOpt.get().getAvailableTickets();
        }
        return 0;
    }

    // Update tickets sold (called after booking)
    public void updateTicketsSold(Long eventId, Integer additionalTickets) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            event.setTicketsSold(event.getTicketsSold() + additionalTickets);
            eventRepository.save(event);
        }
    }

    // Get event with complete details (including bookings and reviews)
    public Optional<Event> getEventWithDetails(Long id) {
        return eventRepository.findById(id);
    }

    // Validate event data
    private void validateEventData(Event event) {
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (event.getDate() == null) {
            throw new IllegalArgumentException("Event date is required");
        }
        if (event.getTime() == null) {
            throw new IllegalArgumentException("Event time is required");
        }
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Event location is required");
        }
        if (event.getTotalTickets() == null || event.getTotalTickets() <= 0) {
            throw new IllegalArgumentException("Total tickets must be positive");
        }
        if (event.getBasePrice() == null || event.getBasePrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        if (event.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date cannot be in the past");
        }
    }
}