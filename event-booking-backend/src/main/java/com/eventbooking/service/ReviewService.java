package com.eventbooking.service;

import com.eventbooking.model.Event;
import com.eventbooking.model.Review;
import com.eventbooking.model.User;
import com.eventbooking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingService bookingService;

    // Create new review
    // Replace your createReview method in ReviewService.java with this:
    public Review createReview(User user, Event event, Integer rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Check if user has already reviewed this event
        if (reviewRepository.hasUserReviewedEvent(user.getId(), event.getId())) {
            throw new RuntimeException("You have already reviewed this event");
        }

        // TEMPORARILY COMMENTED OUT - Remove booking requirement for testing
        // Check if user has booked this event
        // if (!bookingService.hasUserBookedEvent(user.getId(), event.getId())) {
        //     throw new RuntimeException("You can only review events you have booked");
        // }

        Review review = new Review(user, event, rating, comment);
        return reviewRepository.save(review);
    }
    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get review by ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Get reviews by event
    public List<Review> getReviewsByEvent(Long eventId) {
        return reviewRepository.findByEventIdOrderByReviewDateDesc(eventId);
    }

    // Get reviews by user
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    // Get reviews by rating
    public List<Review> getReviewsByRating(Integer rating) {
        return reviewRepository.findByRating(rating);
    }

    // Update review
    public Review updateReview(Long reviewId, Integer rating, String comment) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (!reviewOpt.isPresent()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();

        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    // Delete review
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    // Check if user has reviewed event
    public boolean hasUserReviewedEvent(Long userId, Long eventId) {
        return reviewRepository.hasUserReviewedEvent(userId, eventId);
    }

    // Get user's review for specific event
    public Optional<Review> getUserReviewForEvent(Long userId, Long eventId) {
        return reviewRepository.findByUserIdAndEventId(userId, eventId);
    }

    // Get average rating for event
    public Double getAverageRatingForEvent(Long eventId) {
        Double avgRating = reviewRepository.getAverageRatingByEventId(eventId);
        return avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0;
    }

    // Get review count for event
    public Integer getReviewCountForEvent(Long eventId) {
        Integer count = reviewRepository.getReviewCountByEventId(eventId);
        return count != null ? count : 0;
    }

    // Get high-rated reviews (4 stars and above)
    public List<Review> getHighRatedReviews() {
        return reviewRepository.findHighRatedReviews(4);
    }

    // Get recent reviews
    public List<Review> getRecentReviews() {
        return reviewRepository.findRecentReviews();
    }

    // Get rating distribution for an event
    public RatingDistribution getRatingDistribution(Long eventId) {
        List<Review> reviews = reviewRepository.findByEventId(eventId);

        int[] distribution = new int[6]; // Index 0 unused, 1-5 for ratings
        int totalReviews = reviews.size();

        for (Review review : reviews) {
            distribution[review.getRating()]++;
        }

        return new RatingDistribution(distribution, totalReviews, getAverageRatingForEvent(eventId));
    }

    // Get event review summary
    public ReviewSummary getEventReviewSummary(Long eventId) {
        Double avgRating = getAverageRatingForEvent(eventId);
        Integer reviewCount = getReviewCountForEvent(eventId);
        RatingDistribution distribution = getRatingDistribution(eventId);
        List<Review> recentReviews = reviewRepository.findByEventIdOrderByReviewDateDesc(eventId);

        // Get only top 5 recent reviews for summary
        List<Review> topRecentReviews = recentReviews.size() > 5 ?
                recentReviews.subList(0, 5) : recentReviews;

        return new ReviewSummary(avgRating, reviewCount, distribution, topRecentReviews);
    }

    // Inner class for rating distribution
    public static class RatingDistribution {
        private final int[] distribution; // Array index represents rating (1-5)
        private final int totalReviews;
        private final double averageRating;

        public RatingDistribution(int[] distribution, int totalReviews, double averageRating) {
            this.distribution = distribution;
            this.totalReviews = totalReviews;
            this.averageRating = averageRating;
        }

        public int[] getDistribution() {
            return distribution;
        }

        public int getTotalReviews() {
            return totalReviews;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public int getCountForRating(int rating) {
            if (rating >= 1 && rating <= 5) {
                return distribution[rating];
            }
            return 0;
        }

        public double getPercentageForRating(int rating) {
            if (totalReviews == 0) return 0.0;
            return (double) getCountForRating(rating) / totalReviews * 100;
        }
    }

    // Inner class for review summary
    public static class ReviewSummary {
        private final double averageRating;
        private final int totalReviews;
        private final RatingDistribution ratingDistribution;
        private final List<Review> recentReviews;

        public ReviewSummary(double averageRating, int totalReviews,
                             RatingDistribution ratingDistribution, List<Review> recentReviews) {
            this.averageRating = averageRating;
            this.totalReviews = totalReviews;
            this.ratingDistribution = ratingDistribution;
            this.recentReviews = recentReviews;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public int getTotalReviews() {
            return totalReviews;
        }

        public RatingDistribution getRatingDistribution() {
            return ratingDistribution;
        }

        public List<Review> getRecentReviews() {
            return recentReviews;
        }
    }
}