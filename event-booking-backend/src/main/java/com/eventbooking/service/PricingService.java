package com.eventbooking.service;

import com.eventbooking.model.Event;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    // Constants for price adjustments
    private static final double FIRST_THRESHOLD = 50.0;  // 50% sold
    private static final double SECOND_THRESHOLD = 80.0; // 80% sold
    private static final double FIRST_INCREASE = 0.10;   // 10% increase
    private static final double SECOND_INCREASE = 0.20;  // 20% increase

    // Calculate current price based on booking percentage
    public BigDecimal calculateCurrentPrice(Event event) {
        if (event == null || event.getBasePrice() == null) {
            throw new IllegalArgumentException("Event and base price cannot be null");
        }

        double soldPercentage = event.getSoldPercentage();
        BigDecimal basePrice = event.getBasePrice();
        BigDecimal currentPrice = basePrice;

        // Apply dynamic pricing based on sold percentage
        if (soldPercentage >= SECOND_THRESHOLD) {
            // 80% or more sold - apply 20% increase
            currentPrice = basePrice.multiply(BigDecimal.valueOf(1 + SECOND_INCREASE));
        } else if (soldPercentage >= FIRST_THRESHOLD) {
            // 50% or more sold - apply 10% increase
            currentPrice = basePrice.multiply(BigDecimal.valueOf(1 + FIRST_INCREASE));
        }
        // If less than 50% sold, keep base price

        // Round to 2 decimal places
        return currentPrice.setScale(2, RoundingMode.HALF_UP);
    }

    // Calculate price increase percentage
    public double getPriceIncreasePercentage(Event event) {
        double soldPercentage = event.getSoldPercentage();

        if (soldPercentage >= SECOND_THRESHOLD) {
            return SECOND_INCREASE * 100; // Return as percentage
        } else if (soldPercentage >= FIRST_THRESHOLD) {
            return FIRST_INCREASE * 100; // Return as percentage
        }

        return 0.0; // No increase
    }

    // Get price increase amount
    public BigDecimal getPriceIncreaseAmount(Event event) {
        BigDecimal currentPrice = calculateCurrentPrice(event);
        return currentPrice.subtract(event.getBasePrice());
    }

    // Check if price increase is applicable
    public boolean isPriceIncreaseApplicable(Event event) {
        return event.getSoldPercentage() >= FIRST_THRESHOLD;
    }

    // Get next price tier information
    public PriceTierInfo getNextPriceTierInfo(Event event) {
        double soldPercentage = event.getSoldPercentage();

        if (soldPercentage < FIRST_THRESHOLD) {
            return new PriceTierInfo(
                    FIRST_THRESHOLD,
                    FIRST_INCREASE * 100,
                    event.getBasePrice().multiply(BigDecimal.valueOf(1 + FIRST_INCREASE))
            );
        } else if (soldPercentage < SECOND_THRESHOLD) {
            return new PriceTierInfo(
                    SECOND_THRESHOLD,
                    SECOND_INCREASE * 100,
                    event.getBasePrice().multiply(BigDecimal.valueOf(1 + SECOND_INCREASE))
            );
        }

        return null; // Already at highest tier
    }

    // Calculate total cost for multiple tickets
    public BigDecimal calculateTotalCost(Event event, Integer numberOfTickets) {
        if (numberOfTickets == null || numberOfTickets <= 0) {
            throw new IllegalArgumentException("Number of tickets must be positive");
        }

        BigDecimal currentPrice = calculateCurrentPrice(event);
        return currentPrice.multiply(BigDecimal.valueOf(numberOfTickets));
    }

    // Get pricing summary for an event
    public PricingSummary getPricingSummary(Event event) {
        return new PricingSummary(
                event.getBasePrice(),
                calculateCurrentPrice(event),
                getPriceIncreasePercentage(event),
                getPriceIncreaseAmount(event),
                event.getSoldPercentage(),
                isPriceIncreaseApplicable(event)
        );
    }

    // Inner class for price tier information
    public static class PriceTierInfo {
        private final double thresholdPercentage;
        private final double increasePercentage;
        private final BigDecimal newPrice;

        public PriceTierInfo(double thresholdPercentage, double increasePercentage, BigDecimal newPrice) {
            this.thresholdPercentage = thresholdPercentage;
            this.increasePercentage = increasePercentage;
            this.newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);
        }

        public double getThresholdPercentage() {
            return thresholdPercentage;
        }

        public double getIncreasePercentage() {
            return increasePercentage;
        }

        public BigDecimal getNewPrice() {
            return newPrice;
        }
    }

    // Inner class for pricing summary
    public static class PricingSummary {
        private final BigDecimal basePrice;
        private final BigDecimal currentPrice;
        private final double increasePercentage;
        private final BigDecimal increaseAmount;
        private final double soldPercentage;
        private final boolean priceIncreased;

        public PricingSummary(BigDecimal basePrice, BigDecimal currentPrice,
                              double increasePercentage, BigDecimal increaseAmount,
                              double soldPercentage, boolean priceIncreased) {
            this.basePrice = basePrice;
            this.currentPrice = currentPrice;
            this.increasePercentage = increasePercentage;
            this.increaseAmount = increaseAmount;
            this.soldPercentage = soldPercentage;
            this.priceIncreased = priceIncreased;
        }

        public BigDecimal getBasePrice() {
            return basePrice;
        }

        public BigDecimal getCurrentPrice() {
            return currentPrice;
        }

        public double getIncreasePercentage() {
            return increasePercentage;
        }

        public BigDecimal getIncreaseAmount() {
            return increaseAmount;
        }

        public double getSoldPercentage() {
            return soldPercentage;
        }

        public boolean isPriceIncreased() {
            return priceIncreased;
        }
    }
}