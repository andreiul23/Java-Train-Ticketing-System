package service;

import exception.OverbookedException;
import model.Booking;
import model.Route;
import data.BookingRepository;

import java.util.UUID;

public class BookingService {
    private BookingRepository bookingRepository;
    private EmailService emailService;

    public BookingService(BookingRepository bookingRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    public synchronized Booking bookTicket(String customerEmail, Route route, int numberOfTickets) throws OverbookedException {
        int currentlyBooked = 0;

        for (Booking existingBooking : bookingRepository.getAllBookings()) {
            if (existingBooking.getRoute().getRouteId().equals(route.getRouteId())) {
                currentlyBooked += existingBooking.getNumberOfTickets();
            }
        }

        int availableCapacity = route.getTrain().getCapacity() - currentlyBooked;

        if (numberOfTickets > availableCapacity) {
            throw new OverbookedException("Only " + availableCapacity + " seats available.");
        }

        String bookingId = UUID.randomUUID().toString();
        Booking newBooking = new Booking(bookingId, customerEmail, route, numberOfTickets);

        bookingRepository.addBooking(newBooking);
        emailService.sendBookingConfirmation(customerEmail, route.getRouteId(), numberOfTickets);

        return newBooking;
    }

    public synchronized void cancelBooking(String bookingId) {
        Booking bookingToCancel = null;
        for (Booking b : bookingRepository.getAllBookings()) {
            if (b.getBookingId().equals(bookingId)) {
                bookingToCancel = b;
                break;
            }
        }

        if (bookingToCancel != null) {
            int freedSeats = bookingToCancel.getNumberOfTickets();
            bookingRepository.removeBooking(bookingId);
            System.out.println("Booking " + bookingId + " has been cancelled. Freed " + freedSeats + " seats.");

            Route route = bookingToCancel.getRoute();

            while (freedSeats > 0 && !route.getWaitlist().isEmpty()) {
                String nextInLine = route.popFromWaitlist();
                if (nextInLine != null) {
                    try {
                        bookTicket(nextInLine, route, 1);
                        System.out.println("Waitlist triggered! Auto-booked 1 seat for " + nextInLine);
                        freedSeats--;
                    } catch (OverbookedException e) {
                    }
                }
            }
        } else {
            System.out.println("Booking ID not found.");
        }
    }
}