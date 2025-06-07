package com.eventbooking.controller;

import com.eventbooking.model.Review;
import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;
import com.eventbooking.model.Role;
import com.eventbooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventbooking.dto.DTOMapper;
import com.eventbooking.dto.BookingDTO;
import com.eventbooking.dto.ReviewDTO;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewService reviewService;

    // Get admin dashboard statistics
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> dashboardData = new HashMap<>();

        // User statistics
        Map<String, Object> userStats = new HashMap<>();
        userStats.put("totalUsers", userService.getTotalUserCount());
        userStats.put("totalAdmins", userService.getUserCountByRole(Role.ADMIN));
        userStats.put("totalRegularUsers", userService.getUserCountByRole(Role.USER));
        dashboardData.put("userStats", userStats);

        // Event statistics
        Map<String, Object> eventStats = new HashMap<>();
        eventStats.put("totalEvents", eventService.getTotalEventsCount());
        eventStats.put("upcomingEvents", eventService.getUpcomingEvents().size());
        eventStats.put("eventsWithAvailableTickets", eventService.getEventsWithAvailableTickets().size());
        dashboardData.put("eventStats", eventStats);

        // Booking statistics
        Map<String, Object> bookingStats = new HashMap<>();
        List<Booking> allBookings = bookingService.getAllBookings();
        bookingStats.put("totalBookings", allBookings.size());
        bookingStats.put("confirmedBookings", allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count());
        bookingStats.put("cancelledBookings", allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED).count());
        bookingStats.put("completedBookings", allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());
        bookingStats.put("totalRevenue", bookingService.getTotalRevenue());
        dashboardData.put("bookingStats", bookingStats);

        // Review statistics
        Map<String, Object> reviewStats = new HashMap<>();
        List<Review> allReviews = reviewService.getAllReviews();
        reviewStats.put("totalReviews", allReviews.size());

        // Calculate overall average rating
        double totalRating = allReviews.stream()
                .mapToInt(r -> r.getRating())
                .sum();
        double overallAverage = allReviews.isEmpty() ? 0.0 : totalRating / allReviews.size();
        reviewStats.put("overallAverageRating", Math.round(overallAverage * 10.0) / 10.0);

        // High rated reviews count (4+ stars)
        long highRatedCount = allReviews.stream()
                .filter(r -> r.getRating() >= 4)
                .count();
        reviewStats.put("highRatedReviews", highRatedCount);
        dashboardData.put("reviewStats", reviewStats);

        // Recent activity - Convert to DTOs
        List<BookingDTO> recentBookingDTOs = bookingService.getRecentBookings().stream()
                .limit(5)
                .map(dtoMapper::toBookingDTO)
                .collect(Collectors.toList());
        dashboardData.put("recentBookings", recentBookingDTOs);

        List<ReviewDTO> recentReviewDTOs = reviewService.getRecentReviews().stream()
                .limit(5)
                .map(dtoMapper::toReviewDTO)
                .collect(Collectors.toList());
        dashboardData.put("recentReviews", recentReviewDTOs);

        return ResponseEntity.ok(dashboardData);
    }

    // Get revenue analytics
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueAnalytics() {
        Map<String, Object> revenueData = new HashMap<>();

        // Total revenue
        BigDecimal totalRevenue = bookingService.getTotalRevenue();
        revenueData.put("totalRevenue", totalRevenue);

        // Revenue by event (top 10)
        List<com.eventbooking.model.Event> allEvents = eventService.getAllEvents();
        List<Map<String, Object>> topEventsByRevenue = allEvents.stream()
                .map(event -> {
                    Map<String, Object> eventRevenue = new HashMap<>();
                    eventRevenue.put("event", event);
                    eventRevenue.put("revenue", eventService.getTotalRevenueByEvent(event.getId()));
                    return eventRevenue;
                })
                .sorted((a, b) -> {
                    Double revenueA = (Double) a.get("revenue");
                    Double revenueB = (Double) b.get("revenue");
                    return Double.compare(revenueB, revenueA);
                })
                .limit(10)
                .toList();

        revenueData.put("topEventsByRevenue", topEventsByRevenue);

        return ResponseEntity.ok(revenueData);
    }

    // Get event analytics
    @GetMapping("/events/analytics")
    public ResponseEntity<?> getEventAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        List<com.eventbooking.model.Event> allEvents = eventService.getAllEvents();

        // Most popular events (by booking percentage)
        List<Map<String, Object>> popularEvents = allEvents.stream()
                .map(event -> {
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("event", event);
                    eventData.put("soldPercentage", event.getSoldPercentage());
                    eventData.put("revenue", eventService.getTotalRevenueByEvent(event.getId()));
                    eventData.put("averageRating", reviewService.getAverageRatingForEvent(event.getId()));
                    return eventData;
                })
                .sorted((a, b) -> {
                    Double percentageA = (Double) a.get("soldPercentage");
                    Double percentageB = (Double) b.get("soldPercentage");
                    return Double.compare(percentageB, percentageA);
                })
                .limit(10)
                .toList();

        analytics.put("popularEvents", popularEvents);

        // Events by type distribution
        Map<String, Long> eventsByType = new HashMap<>();
        allEvents.forEach(event -> {
            String type = event.getType().toString();
            eventsByType.put(type, eventsByType.getOrDefault(type, 0L) + 1);
        });
        analytics.put("eventsByType", eventsByType);

        // Average ticket sales per event
        double avgTicketsSold = allEvents.stream()
                .mapToInt(e -> e.getTicketsSold())
                .average()
                .orElse(0.0);
        analytics.put("averageTicketsSoldPerEvent", Math.round(avgTicketsSold * 100.0) / 100.0);

        return ResponseEntity.ok(analytics);
    }

    // Get user analytics
    @GetMapping("/users/analytics")
    public ResponseEntity<?> getUserAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        List<com.eventbooking.model.User> allUsers = userService.getAllUsers();

        // Top users by bookings
        List<Map<String, Object>> topUsersByBookings = allUsers.stream()
                .map(user -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("user", user);
                    userData.put("totalBookings", bookingService.getBookingsByUser(user.getId()).size());
                    userData.put("totalSpent", bookingService.getRevenueByUser(user.getId()));
                    userData.put("totalReviews", reviewService.getReviewsByUser(user.getId()).size());
                    return userData;
                })
                .sorted((a, b) -> {
                    Integer bookingsA = (Integer) a.get("totalBookings");
                    Integer bookingsB = (Integer) b.get("totalBookings");
                    return Integer.compare(bookingsB, bookingsA);
                })
                .limit(10)
                .toList();

        analytics.put("topUsersByBookings", topUsersByBookings);

        // Top users by spending
        List<Map<String, Object>> topUsersBySpending = allUsers.stream()
                .map(user -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("user", user);
                    userData.put("totalSpent", bookingService.getRevenueByUser(user.getId()));
                    userData.put("totalBookings", bookingService.getBookingsByUser(user.getId()).size());
                    return userData;
                })
                .sorted((a, b) -> {
                    BigDecimal spentA = (BigDecimal) a.get("totalSpent");
                    BigDecimal spentB = (BigDecimal) b.get("totalSpent");
                    return spentB.compareTo(spentA);
                })
                .limit(10)
                .toList();

        analytics.put("topUsersBySpending", topUsersBySpending);

        return ResponseEntity.ok(analytics);
    }

    // Get booking analytics
    @GetMapping("/bookings/analytics")
    public ResponseEntity<?> getBookingAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        List<com.eventbooking.model.Booking> allBookings = bookingService.getAllBookings();

        // Booking status distribution
        Map<String, Long> bookingsByStatus = new HashMap<>();
        allBookings.forEach(booking -> {
            String status = booking.getStatus().toString();
            bookingsByStatus.put(status, bookingsByStatus.getOrDefault(status, 0L) + 1);
        });
        analytics.put("bookingsByStatus", bookingsByStatus);

        // Average booking value
        double avgBookingValue = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .mapToDouble(b -> b.getTotalAmount().doubleValue())
                .average()
                .orElse(0.0);
        analytics.put("averageBookingValue", Math.round(avgBookingValue * 100.0) / 100.0);

        // Total tickets sold
        int totalTicketsSold = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .mapToInt(b -> b.getTicketsBooked())
                .sum();
        analytics.put("totalTicketsSold", totalTicketsSold);

        return ResponseEntity.ok(analytics);
    }

    // Get review analytics
    @GetMapping("/reviews/analytics")
    public ResponseEntity<?> getReviewAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        List<com.eventbooking.model.Review> allReviews = reviewService.getAllReviews();

        // Rating distribution
        Map<Integer, Long> ratingDistribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            final int rating = i; // Make it effectively final
            long count = allReviews.stream()
                    .filter(r -> r.getRating() == rating)
                    .count();
            ratingDistribution.put(i, count);
        }
        analytics.put("ratingDistribution", ratingDistribution);

        // Events with highest ratings
        List<com.eventbooking.model.Event> allEvents = eventService.getAllEvents();
        List<Map<String, Object>> topRatedEvents = allEvents.stream()
                .map(event -> {
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("event", event);
                    eventData.put("averageRating", reviewService.getAverageRatingForEvent(event.getId()));
                    eventData.put("reviewCount", reviewService.getReviewCountForEvent(event.getId()));
                    return eventData;
                })
                .filter(data -> (Integer) data.get("reviewCount") > 0)
                .sorted((a, b) -> {
                    Double ratingA = (Double) a.get("averageRating");
                    Double ratingB = (Double) b.get("averageRating");
                    return Double.compare(ratingB, ratingA);
                })
                .limit(10)
                .toList();

        analytics.put("topRatedEvents", topRatedEvents);

        return ResponseEntity.ok(analytics);
    }

    // Get system health status
    @GetMapping("/health")
    public ResponseEntity<?> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();

        try {
            // Check database connectivity by counting records
            Long userCount = userService.getTotalUserCount();
            Long eventCount = eventService.getTotalEventsCount();

            health.put("status", "HEALTHY");
            health.put("database", "CONNECTED");
            health.put("userCount", userCount);
            health.put("eventCount", eventCount);
            health.put("timestamp", java.time.LocalDateTime.now());

        } catch (Exception e) {
            health.put("status", "UNHEALTHY");
            health.put("error", e.getMessage());
            health.put("timestamp", java.time.LocalDateTime.now());
        }

        return ResponseEntity.ok(health);
    }
}