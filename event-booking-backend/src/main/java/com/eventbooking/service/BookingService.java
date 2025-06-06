package com.eventbooking.service;

import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;
import com.eventbooking.model.Event;
import com.eventbooking.model.User;
import com.eventbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private PricingService pricingService;

    // Create new booking
    @Transactional
    public Booking createBooking(User user, Long eventId, Integer ticketsRequested) {
        // Get event
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (!eventOpt.isPresent()) {
            throw new RuntimeException("Event not found");
        }

        Event event = eventOpt.get();

        // Check if enough tickets available
        if (event.getAvailableTickets() < ticketsRequested) {
            throw new RuntimeException("Not enough tickets available. Available: " + event.getAvailableTickets());
        }

        // Calculate total amount with current pricing
        BigDecimal currentPrice = pricingService.calculateCurrentPrice(event);
        BigDecimal totalAmount = currentPrice.multiply(BigDecimal.valueOf(ticketsRequested));

        // Create booking
        Booking booking = new Booking(user, event, ticketsRequested, totalAmount);

        // Generate QR code
        String qrCodeData = generateQRCodeData(booking);
        booking.setQrCode(qrCodeData);

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Update event tickets sold and current price
        event.setTicketsSold(event.getTicketsSold() + ticketsRequested);
        event.setCurrentPrice(pricingService.calculateCurrentPrice(event));
        eventService.updateEvent(event);

        return savedBooking;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Get bookings by user
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // Get bookings by event
    public List<Booking> getBookingsByEvent(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }

    // Get booking by reference
    public Optional<Booking> getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference);
    }

    // Get booking by QR code
    public Optional<Booking> getBookingByQRCode(String qrCode) {
        return bookingRepository.findByQrCode(qrCode);
    }

    // Cancel booking
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);

        // Update event tickets sold
        Event event = booking.getEvent();
        event.setTicketsSold(event.getTicketsSold() - booking.getTicketsBooked());
        event.setCurrentPrice(pricingService.calculateCurrentPrice(event));
        eventService.updateEvent(event);

        return bookingRepository.save(booking);
    }

    // Complete booking (mark as completed after event)
    public Booking completeBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    // Verify QR code for entry
    public boolean verifyQRCodeForEntry(String qrCode) {
        System.out.println("DEBUG: Verifying QR Code: " + qrCode);

        Optional<Booking> bookingOpt = bookingRepository.findByQrCode(qrCode);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            System.out.println("DEBUG: Found booking with status: " + booking.getStatus());
            return booking.getStatus() == BookingStatus.CONFIRMED;
        } else {
            System.out.println("DEBUG: No booking found for QR code");
            return false;
        }
    }

    // Get confirmed bookings by user
    public List<Booking> getConfirmedBookingsByUser(Long userId) {
        return bookingRepository.findByUserIdAndStatus(userId, BookingStatus.CONFIRMED);
    }

    // Get total revenue
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = bookingRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    // Get revenue by user
    public BigDecimal getRevenueByUser(Long userId) {
        BigDecimal revenue = bookingRepository.getTotalRevenueByUserId(userId);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    // Get recent bookings
    public List<Booking> getRecentBookings() {
        return bookingRepository.findRecentBookings();
    }

    // Check if user has booked event
    public boolean hasUserBookedEvent(Long userId, Long eventId) {
        return bookingRepository.hasUserBookedEvent(userId, eventId);
    }

    // Get bookings by date range
    public List<Booking> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByBookingDateBetween(startDate, endDate);
    }

    // Generate QR code data
    private String generateQRCodeData(Booking booking) {
        return String.format("BOOKING_%s_USER_%d_EVENT_%d_TICKETS_%d_REF_%s",
                booking.getId(),
                booking.getUser().getId(),
                booking.getEvent().getId(),
                booking.getTicketsBooked(),
                booking.getBookingReference());
    }

    // Generate QR code image as Base64 string
    public String generateQRCodeImage(String qrCodeData) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    // Validate booking data
    private void validateBookingData(User user, Event event, Integer ticketsRequested) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event is required");
        }
        if (ticketsRequested == null || ticketsRequested <= 0) {
            throw new IllegalArgumentException("Number of tickets must be positive");
        }
        if (ticketsRequested > event.getAvailableTickets()) {
            throw new IllegalArgumentException("Not enough tickets available");
        }
    }
}