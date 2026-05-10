package model;

public class Booking {
    private String bookingId;
    private String customerEmail;
    private Route route;
    private int numberOfTickets;

    public Booking(String bookingId, String customerEmail, Route route, int numberOfTickets) {
        this.bookingId = bookingId;
        this.customerEmail = customerEmail;
        this.route = route;
        this.numberOfTickets = numberOfTickets;
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerEmail() { return customerEmail; }
    public Route getRoute() { return route; }
    public int getNumberOfTickets() { return numberOfTickets; }
}
