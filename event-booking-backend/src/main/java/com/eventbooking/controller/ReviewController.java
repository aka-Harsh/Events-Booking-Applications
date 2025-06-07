package com.eventbooking.controller;

import com.eventbooking.model.Event;
import com.eventbooking.model.Review;
import com.eventbooking.model.User;
import com.eventbooking.service.EventService;
import com.eventbooking.service.ReviewService;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    // Create new review
    // Replace your createReview method in ReviewController.java with this:
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> reviewRequest) {
        try {
            System.out.println("Received review request: " + reviewRequest); // Debug log

            // Add validation for required fields
            if (!reviewRequest.containsKey("userId") || !reviewRequest.containsKey("eventId") ||
                    !reviewRequest.containsKey("rating")) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required fields: userId, eventId, rating");
                return ResponseEntity.badRequest().body(response);
            }

            // Extract and validate data
            Object userIdObj = reviewRequest.get("userId");
            Object eventIdObj = reviewRequest.get("eventId");
            Object ratingObj = reviewRequest.get("rating");

            System.out.println("UserId: " + userIdObj + " (type: " + userIdObj.getClass().getSimpleName() + ")");
            System.out.println("EventId: " + eventIdObj + " (type: " + eventIdObj.getClass().getSimpleName() + ")");
            System.out.println("Rating: " + ratingObj + " (type: " + ratingObj.getClass().getSimpleName() + ")");

            Long userId;
            Long eventId;
            Integer rating;

            try {
                userId = Long.valueOf(userIdObj.toString());
                eventId = Long.valueOf(eventIdObj.toString());
                rating = Integer.valueOf(ratingObj.toString());
            } catch (NumberFormatException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid number format for userId, eventId, or rating");
                return ResponseEntity.badRequest().body(response);
            }

            String comment = reviewRequest.get("comment") != null ? reviewRequest.get("comment").toString() : "";

            // Validate rating range
            if (rating < 1 || rating > 5) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Rating must be between 1 and 5");
                return ResponseEntity.badRequest().body(response);
            }

            // Check user exists
            Optional<User> userOpt = userService.findById(userId);
            if (!userOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found with ID: " + userId);
                return ResponseEntity.badRequest().body(response);
            }

            // Check event exists
            Optional<Event> eventOpt = eventService.getEventById(eventId);
            if (!eventOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Event not found with ID: " + eventId);
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            Event event = eventOpt.get();

            System.out.println("Creating review for user: " + user.getName() + " and event: " + event.getName());

            // Create review
            Review review = reviewService.createReview(user, event, rating, comment);

            System.out.println("Review created successfully with ID: " + review.getId());

            // Return simplified review response
            Map<String, Object> reviewResponse = new HashMap<>();
            reviewResponse.put("id", review.getId());
            reviewResponse.put("rating", review.getRating());
            reviewResponse.put("comment", review.getComment());
            reviewResponse.put("reviewDate", review.getReviewDate());

            // Add user info
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getName());
            reviewResponse.put("user", userInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review created successfully");
            response.put("review", reviewResponse);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            System.out.println("Business logic error: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get all reviews (Admin only)
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        try {
            List<Review> reviews = reviewService.getAllReviews();

            // Create simple response objects to avoid circular references
            List<Map<String, Object>> reviewResponses = reviews.stream()
                    .map(review -> {
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("id", review.getId());
                        reviewMap.put("rating", review.getRating());
                        reviewMap.put("comment", review.getComment());
                        reviewMap.put("reviewDate", review.getReviewDate());

                        // Add user info without circular reference
                        if (review.getUser() != null) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("id", review.getUser().getId());
                            userMap.put("name", review.getUser().getName());
                            reviewMap.put("user", userMap);
                        }

                        // Add event info without circular reference
                        if (review.getEvent() != null) {
                            Map<String, Object> eventMap = new HashMap<>();
                            eventMap.put("id", review.getEvent().getId());
                            eventMap.put("name", review.getEvent().getName());
                            reviewMap.put("event", eventMap);
                        }

                        return reviewMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reviewResponses);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        Optional<Review> reviewOpt = reviewService.getReviewById(id);
        if (reviewOpt.isPresent()) {
            return ResponseEntity.ok(reviewOpt.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Review not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Update review
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody Map<String, Object> reviewRequest) {
        try {
            Integer rating = Integer.valueOf(reviewRequest.get("rating").toString());
            String comment = reviewRequest.get("comment") != null ? reviewRequest.get("comment").toString() : "";

            Review updatedReview = reviewService.updateReview(id, rating, comment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review updated successfully");
            response.put("review", updatedReview);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Replace your getReviewsByEvent method with this:
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getReviewsByEvent(@PathVariable Long eventId) {
        try {
            List<Review> reviews = reviewService.getReviewsByEvent(eventId);

            // Create simple response objects to avoid circular references
            List<Map<String, Object>> reviewResponses = reviews.stream()
                    .map(review -> {
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("id", review.getId());
                        reviewMap.put("rating", review.getRating());
                        reviewMap.put("comment", review.getComment());
                        reviewMap.put("reviewDate", review.getReviewDate());

                        // Add user info without circular reference
                        if (review.getUser() != null) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("id", review.getUser().getId());
                            userMap.put("name", review.getUser().getName());
                            reviewMap.put("user", userMap);
                        }

                        return reviewMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reviewResponses);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get reviews by user
    // Replace your getReviewsByUser method with this:
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUser(userId);

            // Create simple response objects to avoid circular references
            List<Map<String, Object>> reviewResponses = reviews.stream()
                    .map(review -> {
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("id", review.getId());
                        reviewMap.put("rating", review.getRating());
                        reviewMap.put("comment", review.getComment());
                        reviewMap.put("reviewDate", review.getReviewDate());

                        // Add event info without circular reference
                        if (review.getEvent() != null) {
                            Map<String, Object> eventMap = new HashMap<>();
                            eventMap.put("id", review.getEvent().getId());
                            eventMap.put("name", review.getEvent().getName());
                            eventMap.put("date", review.getEvent().getDate());
                            eventMap.put("time", review.getEvent().getTime());
                            eventMap.put("location", review.getEvent().getLocation());
                            reviewMap.put("event", eventMap);
                        }

                        return reviewMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reviewResponses);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error loading user reviews: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get reviews by rating
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Review>> getReviewsByRating(@PathVariable Integer rating) {
        List<Review> reviews = reviewService.getReviewsByRating(rating);
        return ResponseEntity.ok(reviews);
    }

    // Check if user has reviewed event
    @GetMapping("/check")
    public ResponseEntity<?> checkUserReview(@RequestParam Long userId, @RequestParam Long eventId) {
        boolean hasReviewed = reviewService.hasUserReviewedEvent(userId, eventId);
        Map<String, Object> response = new HashMap<>();
        response.put("hasReviewed", hasReviewed);

        if (hasReviewed) {
            Optional<Review> reviewOpt = reviewService.getUserReviewForEvent(userId, eventId);
            if (reviewOpt.isPresent()) {
                response.put("review", reviewOpt.get());
            }
        }

        return ResponseEntity.ok(response);
    }

    // Get user's review for specific event
    @GetMapping("/user/{userId}/event/{eventId}")
    public ResponseEntity<?> getUserReviewForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<Review> reviewOpt = reviewService.getUserReviewForEvent(userId, eventId);
        if (reviewOpt.isPresent()) {
            return ResponseEntity.ok(reviewOpt.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Review not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Get average rating for event
    @GetMapping("/event/{eventId}/average")
    public ResponseEntity<?> getAverageRating(@PathVariable Long eventId) {
        Double avgRating = reviewService.getAverageRatingForEvent(eventId);
        Integer reviewCount = reviewService.getReviewCountForEvent(eventId);

        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", avgRating);
        response.put("reviewCount", reviewCount);
        return ResponseEntity.ok(response);
    }

    // Get rating distribution for event
    @GetMapping("/event/{eventId}/distribution")
    public ResponseEntity<?> getRatingDistribution(@PathVariable Long eventId) {
        ReviewService.RatingDistribution distribution = reviewService.getRatingDistribution(eventId);

        Map<String, Object> response = new HashMap<>();
        response.put("distribution", distribution.getDistribution());
        response.put("totalReviews", distribution.getTotalReviews());
        response.put("averageRating", distribution.getAverageRating());

        // Add percentage breakdown
        Map<String, Double> percentages = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            percentages.put(i + "_star", distribution.getPercentageForRating(i));
        }
        response.put("percentages", percentages);

        return ResponseEntity.ok(response);
    }

    // Get event review summary
    @GetMapping("/event/{eventId}/summary")
    public ResponseEntity<?> getEventReviewSummary(@PathVariable Long eventId) {
        ReviewService.ReviewSummary summary = reviewService.getEventReviewSummary(eventId);

        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", summary.getAverageRating());
        response.put("totalReviews", summary.getTotalReviews());
        response.put("ratingDistribution", summary.getRatingDistribution());
        response.put("recentReviews", summary.getRecentReviews());

        return ResponseEntity.ok(response);
    }

    // Get high rated reviews (4+ stars)
    @GetMapping("/high-rated")
    public ResponseEntity<List<Review>> getHighRatedReviews() {
        List<Review> reviews = reviewService.getHighRatedReviews();
        return ResponseEntity.ok(reviews);
    }

    // Get recent reviews
    @GetMapping("/recent")
    public ResponseEntity<List<Review>> getRecentReviews() {
        List<Review> reviews = reviewService.getRecentReviews();
        return ResponseEntity.ok(reviews);
    }

    // Get review statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getReviewStats() {
        List<com.eventbooking.model.Review> allReviews = reviewService.getAllReviews();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", allReviews.size());

        // Calculate average rating across all reviews
        double totalRating = allReviews.stream()
                .mapToInt(com.eventbooking.model.Review::getRating)
                .sum();
        double overallAverage = allReviews.isEmpty() ? 0.0 : totalRating / allReviews.size();
        stats.put("overallAverageRating", Math.round(overallAverage * 10.0) / 10.0);

        // Rating distribution
        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            final int rating = i; // Make it effectively final
            long count = allReviews.stream()
                    .filter(r -> r.getRating() == rating)
                    .count();
            ratingCounts.put(i, count);
        }
        stats.put("ratingDistribution", ratingCounts);

        return ResponseEntity.ok(stats);
    }
}