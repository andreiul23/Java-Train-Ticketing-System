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

    public Booking bookTicket(String customerEmail, Route route, int numberOfTickets) throws OverbookedException {
        int currentlyBooked = 0;

        for (Booking existingBooking : bookingRepository.getAllBookings()) {
            if (existingBooking.getRoute().getRouteId().equals(route.getRouteId())) {
                currentlyBooked += existingBooking.getNumberOfTickets();
            }
        }

        int availableCapacity = route.getTrain().getCapacity() - currentlyBooked;

        if (numberOfTickets > availableCapacity) {
            throw new OverbookedException("Cannot book " + numberOfTickets + " tickets. Only " + availableCapacity + " seats available on this route.");
        }

        String bookingId = UUID.randomUUID().toString();
        Booking newBooking = new Booking(bookingId, customerEmail, route, numberOfTickets);

        bookingRepository.addBooking(newBooking);
        emailService.sendBookingConfirmation(customerEmail, route.getRouteId(), numberOfTickets);

        return newBooking;
    }
}