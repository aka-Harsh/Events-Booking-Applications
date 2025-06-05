package com.eventbooking.dto;

import com.eventbooking.model.User;
import com.eventbooking.model.Event;
import com.eventbooking.model.Booking;
import com.eventbooking.model.Review;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedDate(),
                user.getProfileImage()
        );
    }

    public EventDTO toEventDTO(Event event) {
        if (event == null) return null;
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getType(),
                event.getTags(),
                event.getDate(),
                event.getTime(),
                event.getLocation(),
                event.getTotalTickets(),
                event.getTicketsSold(),
                event.getBasePrice(),
                event.getCurrentPrice(),
                event.getEventImage()
        );
    }

    public BookingDTO toBookingDTO(Booking booking) {
        if (booking == null) return null;
        return new BookingDTO(
                booking.getId(),
                toUserDTO(booking.getUser()),
                toEventDTO(booking.getEvent()),
                booking.getTicketsBooked(),
                booking.getTotalAmount(),
                booking.getQrCode(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getBookingReference()
        );
    }

    public ReviewDTO toReviewDTO(Review review) {
        if (review == null) return null;
        return new ReviewDTO(
                review.getId(),
                toUserDTO(review.getUser()),
                toEventDTO(review.getEvent()),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
        );
    }
}