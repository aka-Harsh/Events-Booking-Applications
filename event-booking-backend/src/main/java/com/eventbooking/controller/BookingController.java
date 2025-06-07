package com.eventbooking.controller;

import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;
import com.eventbooking.model.User;
import com.eventbooking.service.BookingService;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    // Create new booking
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> bookingRequest) {
        try {
            Long userId = Long.valueOf(bookingRequest.get("userId").toString());
            Long eventId = Long.valueOf(bookingRequest.get("eventId").toString());
            Integer ticketsRequested = Integer.valueOf(bookingRequest.get("ticketsRequested").toString());

            Optional<User> userOpt = userService.findById(userId);
            if (!userOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            Booking booking = bookingService.createBooking(user, eventId, ticketsRequested);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking created successfully");
            response.put("booking", booking);
            response.put("qrCodeImage", bookingService.generateQRCodeImage(booking.getQrCode()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all bookings (Admin only)
    // Replace your getAllBookings method with this:
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = bookingService.getAllBookings();

            // Create simple response objects to avoid circular references
            List<Map<String, Object>> bookingResponses = bookings.stream()
                    .map(booking -> {
                        Map<String, Object> bookingMap = new HashMap<>();
                        bookingMap.put("id", booking.getId());
                        bookingMap.put("ticketsBooked", booking.getTicketsBooked());
                        bookingMap.put("totalAmount", booking.getTotalAmount());
                        bookingMap.put("qrCode", booking.getQrCode());
                        bookingMap.put("bookingDate", booking.getBookingDate());
                        bookingMap.put("status", booking.getStatus());
                        bookingMap.put("bookingReference", booking.getBookingReference());

                        // Add event info without circular reference
                        if (booking.getEvent() != null) {
                            Map<String, Object> eventMap = new HashMap<>();
                            eventMap.put("id", booking.getEvent().getId());
                            eventMap.put("name", booking.getEvent().getName());
                            eventMap.put("date", booking.getEvent().getDate());
                            eventMap.put("time", booking.getEvent().getTime());
                            eventMap.put("location", booking.getEvent().getLocation());
                            bookingMap.put("event", eventMap);
                        }

                        // Add user info without circular reference
                        if (booking.getUser() != null) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("id", booking.getUser().getId());
                            userMap.put("name", booking.getUser().getName());
                            userMap.put("email", booking.getUser().getEmail());
                            bookingMap.put("user", userMap);
                        }

                        return bookingMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(bookingResponses);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("booking", booking);
            response.put("qrCodeImage", bookingService.generateQRCodeImage(booking.getQrCode()));
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Booking not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Get bookings by user
    // Replace your getBookingsByUser method with this:
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUser(@PathVariable Long userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUser(userId);

            // Create simple response objects to avoid circular references
            List<Map<String, Object>> bookingResponses = bookings.stream()
                    .map(booking -> {
                        Map<String, Object> bookingMap = new HashMap<>();
                        bookingMap.put("id", booking.getId());
                        bookingMap.put("ticketsBooked", booking.getTicketsBooked());
                        bookingMap.put("totalAmount", booking.getTotalAmount());
                        bookingMap.put("qrCode", booking.getQrCode());
                        bookingMap.put("bookingDate", booking.getBookingDate());
                        bookingMap.put("status", booking.getStatus());
                        bookingMap.put("bookingReference", booking.getBookingReference());

                        // Add event info without circular reference
                        if (booking.getEvent() != null) {
                            Map<String, Object> eventMap = new HashMap<>();
                            eventMap.put("id", booking.getEvent().getId());
                            eventMap.put("name", booking.getEvent().getName());
                            eventMap.put("date", booking.getEvent().getDate());
                            eventMap.put("time", booking.getEvent().getTime());
                            eventMap.put("location", booking.getEvent().getLocation());
                            bookingMap.put("event", eventMap);
                        }

                        return bookingMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(bookingResponses);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get bookings by event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Booking>> getBookingsByEvent(@PathVariable Long eventId) {
        List<Booking> bookings = bookingService.getBookingsByEvent(eventId);
        return ResponseEntity.ok(bookings);
    }

    // Get booking by reference
    @GetMapping("/reference/{reference}")
    public ResponseEntity<?> getBookingByReference(@PathVariable String reference) {
        try {
            Optional<Booking> bookingOpt = bookingService.getBookingByReference(reference);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();

                // Create response without circular references
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("id", booking.getId());
                bookingMap.put("ticketsBooked", booking.getTicketsBooked());
                bookingMap.put("totalAmount", booking.getTotalAmount());
                bookingMap.put("qrCode", booking.getQrCode());
                bookingMap.put("bookingDate", booking.getBookingDate());
                bookingMap.put("status", booking.getStatus());
                bookingMap.put("bookingReference", booking.getBookingReference());

                // Add event info
                if (booking.getEvent() != null) {
                    Map<String, Object> eventMap = new HashMap<>();
                    eventMap.put("id", booking.getEvent().getId());
                    eventMap.put("name", booking.getEvent().getName());
                    eventMap.put("date", booking.getEvent().getDate());
                    eventMap.put("time", booking.getEvent().getTime());
                    eventMap.put("location", booking.getEvent().getLocation());
                    bookingMap.put("event", eventMap);
                }

                // Add user info
                if (booking.getUser() != null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", booking.getUser().getId());
                    userMap.put("name", booking.getUser().getName());
                    userMap.put("email", booking.getUser().getEmail());
                    bookingMap.put("user", userMap);
                }

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("booking", bookingMap);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Booking not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error finding booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // Cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            Booking cancelledBooking = bookingService.cancelBooking(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking cancelled successfully");
            response.put("booking", cancelledBooking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Complete booking
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id) {
        try {
            Booking completedBooking = bookingService.completeBooking(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking completed successfully");
            response.put("booking", completedBooking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Verify QR code for entry (Admin only)
    // Replace your verifyQRCode method with this:
    @PostMapping("/verify-qr")
    public ResponseEntity<?> verifyQRCode(@RequestBody Map<String, String> request) {
        try {
            String qrCode = request.get("qrCode");
            if (qrCode == null || qrCode.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "QR code is required");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isValid = bookingService.verifyQRCodeForEntry(qrCode);
            Optional<Booking> bookingOpt = bookingService.getBookingByQRCode(qrCode);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (isValid && bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();

                // Create booking response without circular references
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("id", booking.getId());
                bookingMap.put("ticketsBooked", booking.getTicketsBooked());
                bookingMap.put("totalAmount", booking.getTotalAmount());
                bookingMap.put("bookingDate", booking.getBookingDate());
                bookingMap.put("status", booking.getStatus());
                bookingMap.put("bookingReference", booking.getBookingReference());

                // Add event info
                if (booking.getEvent() != null) {
                    Map<String, Object> eventMap = new HashMap<>();
                    eventMap.put("id", booking.getEvent().getId());
                    eventMap.put("name", booking.getEvent().getName());
                    eventMap.put("date", booking.getEvent().getDate());
                    eventMap.put("time", booking.getEvent().getTime());
                    eventMap.put("location", booking.getEvent().getLocation());
                    bookingMap.put("event", eventMap);
                }

                // Add user info
                if (booking.getUser() != null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", booking.getUser().getId());
                    userMap.put("name", booking.getUser().getName());
                    userMap.put("email", booking.getUser().getEmail());
                    bookingMap.put("user", userMap);
                }

                response.put("booking", bookingMap);
                response.put("message", "Valid QR code - Entry allowed");
            } else {
                response.put("message", "Invalid QR code - Entry denied");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("valid", false);
            response.put("message", "QR verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get confirmed bookings by user
    @GetMapping("/user/{userId}/confirmed")
    public ResponseEntity<List<Booking>> getConfirmedBookingsByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getConfirmedBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    // Get recent bookings
    @GetMapping("/recent")
    public ResponseEntity<List<Booking>> getRecentBookings() {
        List<Booking> bookings = bookingService.getRecentBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Booking>> getBookingsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Booking> bookings = bookingService.getBookingsByDateRange(start, end);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Check if user has booked event
    @GetMapping("/check")
    public ResponseEntity<?> checkUserBooking(@RequestParam Long userId, @RequestParam Long eventId) {
        boolean hasBooked = bookingService.hasUserBookedEvent(userId, eventId);
        Map<String, Object> response = new HashMap<>();
        response.put("hasBooked", hasBooked);
        return ResponseEntity.ok(response);
    }

    // Get booking statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getBookingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", bookingService.getTotalRevenue());

        List<Booking> allBookings = bookingService.getAllBookings();
        long confirmedBookings = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .count();
        long cancelledBookings = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .count();
        long completedBookings = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .count();

        stats.put("totalBookings", allBookings.size());
        stats.put("confirmedBookings", confirmedBookings);
        stats.put("cancelledBookings", cancelledBookings);
        stats.put("completedBookings", completedBookings);

        return ResponseEntity.ok(stats);
    }

    // Get user booking statistics
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<?> getUserBookingStats(@PathVariable Long userId) {
        List<Booking> userBookings = bookingService.getBookingsByUser(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", userBookings.size());
        stats.put("totalSpent", bookingService.getRevenueByUser(userId));

        long confirmedBookings = userBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .count();
        long cancelledBookings = userBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .count();
        long completedBookings = userBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .count();

        stats.put("confirmedBookings", confirmedBookings);
        stats.put("cancelledBookings", cancelledBookings);
        stats.put("completedBookings", completedBookings);

        return ResponseEntity.ok(stats);
    }

    // Get QR code image for booking
    @GetMapping("/{id}/qr-image")
    public ResponseEntity<?> getBookingQRImage(@PathVariable Long id) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            String qrImage = bookingService.generateQRCodeImage(booking.getQrCode());
            Map<String, Object> response = new HashMap<>();
            response.put("qrCodeImage", qrImage);
            response.put("bookingReference", booking.getBookingReference());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Booking not found");
            return ResponseEntity.notFound().build();
        }
    }
}