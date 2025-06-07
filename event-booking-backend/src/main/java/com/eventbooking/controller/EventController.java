package com.eventbooking.controller;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventType;
import com.eventbooking.service.EventService;
import com.eventbooking.service.PricingService;
import com.eventbooking.service.RecommendationService;
import com.eventbooking.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.eventbooking.dto.DTOMapper;
import com.eventbooking.dto.EventDTO;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private DTOMapper dtoMapper;


    @Autowired
    private EventService eventService;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ReviewService reviewService;

    // Create new event (Admin only)
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event) {
        try {
            System.out.println("Received event data: " + event.toString()); // Debug log
            Event createdEvent = eventService.createEvent(event);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Event created successfully");
            response.put("event", createdEvent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Event creation error: " + e.getMessage()); // Debug log
            e.printStackTrace(); // Print full stack trace
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventDTO> eventDTOs = events.stream()
                .map(dtoMapper::toEventDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("event", event);
            response.put("pricingSummary", pricingService.getPricingSummary(event));
            response.put("averageRating", reviewService.getAverageRatingForEvent(id));
            response.put("reviewCount", reviewService.getReviewCountForEvent(id));
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Event not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Update event (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        try {
            Optional<Event> eventOpt = eventService.getEventById(id);
            if (!eventOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Event not found");
                return ResponseEntity.notFound().build();
            }

            Event existingEvent = eventOpt.get();
            existingEvent.setName(eventDetails.getName());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setType(eventDetails.getType());
            existingEvent.setTags(eventDetails.getTags());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setTime(eventDetails.getTime());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setTotalTickets(eventDetails.getTotalTickets());
            existingEvent.setBasePrice(eventDetails.getBasePrice());

            if (eventDetails.getEventImage() != null) {
                existingEvent.setEventImage(eventDetails.getEventImage());
            }

            Event updatedEvent = eventService.updateEvent(existingEvent);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Event updated successfully");
            response.put("event", updatedEvent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete event (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            if (!eventService.getEventById(id).isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Event not found");
                return ResponseEntity.notFound().build();
            }

            eventService.deleteEvent(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Event deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete event: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get events by type
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getEventsByType(@PathVariable String type) {
        try {
            EventType eventType = EventType.valueOf(type.toUpperCase());
            List<Event> events = eventService.getEventsByType(eventType);
            return ResponseEntity.ok(events);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid event type");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get upcoming events
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    // Search events by name
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String query) {
        List<Event> events = eventService.searchEventsByName(query);
        return ResponseEntity.ok(events);
    }

    // Search events by location
    @GetMapping("/location")
    public ResponseEntity<List<Event>> searchEventsByLocation(@RequestParam String location) {
        List<Event> events = eventService.searchEventsByLocation(location);
        return ResponseEntity.ok(events);
    }

    // Get events with available tickets
    @GetMapping("/available")
    public ResponseEntity<List<Event>> getEventsWithAvailableTickets() {
        List<Event> events = eventService.getEventsWithAvailableTickets();
        return ResponseEntity.ok(events);
    }

    // Get popular events
    @GetMapping("/popular")
    public ResponseEntity<List<Event>> getPopularEvents(@RequestParam(defaultValue = "50") double percentage) {
        List<Event> events = eventService.getPopularEvents(percentage);
        return ResponseEntity.ok(events);
    }

    // Get events by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getEventsByDate(@PathVariable String date) {
        try {
            LocalDate eventDate = LocalDate.parse(date);
            List<Event> events = eventService.getEventsByDate(eventDate);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get events by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Event> events = eventService.getEventsByDateRange(start, end);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Search events by tags
    @GetMapping("/tags")
    public ResponseEntity<List<Event>> searchEventsByTag(@RequestParam String tag) {
        List<Event> events = eventService.searchEventsByTag(tag);
        return ResponseEntity.ok(events);
    }

    // Get event recommendations
    @GetMapping("/{id}/recommendations")
    public ResponseEntity<?> getEventRecommendations(@PathVariable Long id, @RequestParam(defaultValue = "3") int limit) {
        List<Event> recommendations = recommendationService.getRecommendedEvents(id, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", recommendations);
        response.put("count", recommendations.size());
        return ResponseEntity.ok(response);
    }

    // Get similar events by type
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Event>> getSimilarEvents(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        List<Event> similarEvents = recommendationService.getSimilarEventsByType(id, limit);
        return ResponseEntity.ok(similarEvents);
    }

    // Get events in same location
    @GetMapping("/{id}/nearby")
    public ResponseEntity<List<Event>> getNearbyEvents(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        List<Event> nearbyEvents = recommendationService.getEventsInSameLocation(id, limit);
        return ResponseEntity.ok(nearbyEvents);
    }

    // Get event pricing details
    @GetMapping("/{id}/pricing")
    public ResponseEntity<?> getEventPricing(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            PricingService.PricingSummary pricingSummary = pricingService.getPricingSummary(event);
            PricingService.PriceTierInfo nextTier = pricingService.getNextPriceTierInfo(event);

            Map<String, Object> response = new HashMap<>();
            response.put("pricingSummary", pricingSummary);
            response.put("nextTier", nextTier);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Event not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Get event statistics
    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getEventStats(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalTickets", event.getTotalTickets());
            stats.put("ticketsSold", event.getTicketsSold());
            stats.put("availableTickets", event.getAvailableTickets());
            stats.put("soldPercentage", event.getSoldPercentage());
            stats.put("totalRevenue", eventService.getTotalRevenueByEvent(id));
            stats.put("averageRating", reviewService.getAverageRatingForEvent(id));
            stats.put("reviewCount", reviewService.getReviewCountForEvent(id));
            return ResponseEntity.ok(stats);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Event not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Get trending events
    @GetMapping("/trending")
    public ResponseEntity<List<Event>> getTrendingEvents(@RequestParam(defaultValue = "10") int limit) {
        List<Event> trendingEvents = recommendationService.getTrendingEvents(limit);
        return ResponseEntity.ok(trendingEvents);
    }

    // Get total events count
    @GetMapping("/count")
    public ResponseEntity<?> getTotalEventsCount() {
        Long count = eventService.getTotalEventsCount();
        Map<String, Object> response = new HashMap<>();
        response.put("totalEvents", count);
        return ResponseEntity.ok(response);
    }
}