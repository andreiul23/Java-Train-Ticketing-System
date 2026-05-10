package presentation;

import exception.NoRouteFoundException;
import exception.OverbookedException;
import model.Route;
import model.Station;
import model.Train;
import data.BookingRepository;
import data.RouteRepository;
import data.StationRepository;
import data.TrainRepository;
import service.AdminService;
import service.BookingService;
import service.EmailService;
import service.RouteFinderService;
import service.PricingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StationRepository stationRepo = new StationRepository();
        TrainRepository trainRepo = new TrainRepository();
        RouteRepository routeRepo = new RouteRepository();
        BookingRepository bookingRepo = new BookingRepository();

        EmailService emailService = new EmailService();
        RouteFinderService routeFinderService = new RouteFinderService(routeRepo);
        BookingService bookingService = new BookingService(bookingRepo, emailService);
        AdminService adminService = new AdminService(trainRepo, stationRepo, routeRepo, bookingRepo, emailService);
        PricingService pricingService = new PricingService(bookingRepo);

        loadMockData(adminService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("  WELCOME TO THE TRAIN TICKETING SYSTEM  ");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Find Route & Book Ticket");
            System.out.println("2. Cancel a Ticket (Triggers Waitlist)");
            System.out.println("3. Report Train Delay (Admin)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCustomerBooking(scanner, routeFinderService, bookingService, pricingService, stationRepo);
                    break;
                case "2":
                    handleCancellation(scanner, bookingService);
                    break;
                case "3":
                    handleAdminDelay(scanner, adminService);
                    break;
                case "4":
                    running = false;
                    System.out.println("Shutting down... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void handleCustomerBooking(Scanner scanner, RouteFinderService routeFinderService,
                                              BookingService bookingService, PricingService pricingService,
                                              StationRepository stationRepo) {
        System.out.print("\nEnter Departure Station ID (e.g., S1): ");
        String startId = scanner.nextLine();
        System.out.print("Enter Arrival Station ID (e.g., S2): ");
        String endId = scanner.nextLine();

        Station start = stationRepo.getStation(startId);
        Station end = stationRepo.getStation(endId);

        if (start == null || end == null) {
            System.out.println("Error: Invalid Station IDs entered.");
            return;
        }

        try {
            List<List<Route>> itineraries = routeFinderService.findPossibleRoutes(start, end);
            System.out.println("\n--- Available Routes ---");
            Route selectedRoute = itineraries.get(0).get(0);

            System.out.println("Found Route: " + selectedRoute.getRouteId() + " on Train " + selectedRoute.getTrain().getName());

            double currentTicketPrice = pricingService.calculateDynamicPrice(selectedRoute);
            System.out.println("Current dynamic price per ticket: $" + currentTicketPrice);

            System.out.print("Enter your email address: ");
            String email = scanner.nextLine();
            System.out.print("How many tickets do you want to book? ");
            int tickets = Integer.parseInt(scanner.nextLine());

            bookingService.bookTicket(email, selectedRoute, tickets);

            double totalCost = currentTicketPrice * tickets;
            System.out.println("Booking successful! Total charged: $" + totalCost);

        } catch (NoRouteFoundException e) {
            System.out.println("\n[ERROR]: " + e.getMessage());
        } catch (OverbookedException e) {
            // NEW: Waitlist UI Logic
            System.out.println("\n[ERROR]: Train is full! " + e.getMessage());
            System.out.print("Would you like to join the waitlist? (Y/N): ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("Y")) {
                System.out.print("Enter email for the waitlist: ");
                String waitlistEmail = scanner.nextLine();

                // We need to fetch the route again safely to add them to the queue
                try {
                    Route route = routeFinderService.findPossibleRoutes(start, end).get(0).get(0);
                    route.addToWaitlist(waitlistEmail);
                    System.out.println("Added to waitlist! You will be notified if a seat opens.");
                } catch (Exception ex) {
                    System.out.println("Error joining waitlist.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR]: Please enter a valid number for tickets.");
        }
    }

    private static void handleCancellation(Scanner scanner, BookingService bookingService) {
        System.out.print("\nEnter the Booking ID you wish to cancel: ");
        String bookingId = scanner.nextLine();
        bookingService.cancelBooking(bookingId);
    }

    private static void handleAdminDelay(Scanner scanner, AdminService adminService) {
        System.out.print("\nEnter Train ID to report delay (e.g., T1): ");
        String trainId = scanner.nextLine();
        System.out.print("Enter delay in minutes: ");

        try {
            int delay = Integer.parseInt(scanner.nextLine());
            adminService.reportTrainDelay(trainId, delay);
            System.out.println("Delay reported successfully. Notifications sent to affected customers.");
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR]: Please enter a valid number for minutes.");
        }
    }

    private static void loadMockData(AdminService adminService) {
        Station s1 = new Station("S1", "Bucuresti Nord");
        Station s2 = new Station("S2", "Cluj-Napoca");
        adminService.addStation(s1);
        adminService.addStation(s2);

        // Intentionally setting capacity to 2 so you can easily test the waitlist!
        Train t1 = new Train("T1", "InterCity Express", 2);
        adminService.addTrain(t1);

        LocalDateTime departure = LocalDateTime.now().withHour(10).withMinute(0);
        LocalDateTime arrival = LocalDateTime.now().withHour(18).withMinute(0);
        Route r1 = new Route("R1", t1, s1, s2, departure, arrival, 50.0);
        adminService.addRoute(r1);
    }
}