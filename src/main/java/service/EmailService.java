package service;

public class EmailService {

    public void sendBookingConfirmation(String email, String routeId, int tickets) {
        System.out.println("==================================================");
        System.out.println("EMAIL SENT TO: " + email);
        System.out.println("SUBJECT: Booking Confirmation");
        System.out.println("BODY: You have successfully booked " + tickets + " ticket(s) for Route " + routeId + ".");
        System.out.println("==================================================");
    }

    public void sendDelayNotification(String email, String trainName, int delayMinutes) {
        System.out.println("==================================================");
        System.out.println("EMAIL SENT TO: " + email);
        System.out.println("SUBJECT: Train Delay Notification");
        System.out.println("BODY: We apologize, but your train (" + trainName + ") is delayed by " + delayMinutes + " minutes.");
        System.out.println("==================================================");
    }
}