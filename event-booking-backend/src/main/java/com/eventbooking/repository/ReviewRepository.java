package com.eventbooking.repository;

import com.eventbooking.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find reviews by event ID
    List<Review> findByEventId(Long eventId);

    // Find reviews by user ID
    List<Review> findByUserId(Long userId);

    // Find reviews by event ID ordered by date
    List<Review> findByEventIdOrderByReviewDateDesc(Long eventId);

    // Find reviews by rating
    List<Review> findByRating(Integer rating);

    // Find reviews by event and rating
    List<Review> findByEventIdAndRating(Long eventId, Integer rating);

    // Check if user has reviewed an event
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.id = :userId AND r.event.id = :eventId")
    boolean hasUserReviewedEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);

    // Find review by user and event
    Optional<Review> findByUserIdAndEventId(Long userId, Long eventId);

    // Get average rating for an event
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.event.id = :eventId")
    Double getAverageRatingByEventId(@Param("eventId") Long eventId);

    // Get rating count for an event
    @Query("SELECT COUNT(r) FROM Review r WHERE r.event.id = :eventId")
    Integer getReviewCountByEventId(@Param("eventId") Long eventId);

    // Get reviews with rating above threshold
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating ORDER BY r.reviewDate DESC")
    List<Review> findHighRatedReviews(@Param("minRating") Integer minRating);

    // Get recent reviews
    @Query("SELECT r FROM Review r ORDER BY r.reviewDate DESC")
    List<Review> findRecentReviews();
}