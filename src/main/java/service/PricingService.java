package service;

import model.Route;
import data.BookingRepository;
import model.Booking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PricingService {
    private BookingRepository bookingRepository;

    public PricingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public double calculateDynamicPrice(Route route) {
        double currentPrice = route.getBasePrice();
        int bookedSeats = 0;
        for (Booking b : bookingRepository.getAllBookings()) {
            if (b.getRoute().getRouteId().equals(route.getRouteId())) {
                bookedSeats += b.getNumberOfTickets();
            }
        }

        double capacityPercentage = (double) bookedSeats / route.getTrain().getCapacity();

        // If the train is more than 90% full, increase price by 50%
        if (capacityPercentage > 0.90) {
            currentPrice *= 1.50;
        }
        // If the train is more than 70% full, increase price by 30%
        else if (capacityPercentage > 0.70) {
            currentPrice *= 1.30;
        }

        long hoursUntilDeparture = ChronoUnit.HOURS.between(LocalDateTime.now(), route.getDepartureTime());
        // Last-minute booking (under 24 hours) costs 20% more
        if (hoursUntilDeparture < 24 && hoursUntilDeparture > 0) {
            currentPrice *= 1.20;
        }
        // Round to 2 decimal places for clean currency formatting
        return Math.round(currentPrice * 100.0) / 100.0;
    }
}