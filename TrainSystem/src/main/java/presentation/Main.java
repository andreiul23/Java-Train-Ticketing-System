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

        loadMockData(adminService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("  WELCOME TO THE TRAIN TICKETING SYSTEM  ");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Find Route & Book Ticket");
            System.out.println("2. Report Train Delay (Admin)");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCustomerBooking(scanner, routeFinderService, bookingService, stationRepo);
                    break;
                case "2":
                    handleAdminDelay(scanner, adminService);
                    break;
                case "3":
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
                                              BookingService bookingService, StationRepository stationRepo) {
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
            // Simplified for this example: Just picking the first available direct route
            Route selectedRoute = itineraries.get(0).get(0);

            System.out.println("Found Route: " + selectedRoute.getRouteId() + " on Train " + selectedRoute.getTrain().getName());

            System.out.print("Enter your email address: ");
            String email = scanner.nextLine();
            System.out.print("How many tickets do you want to book? ");
            int tickets = Integer.parseInt(scanner.nextLine());

            bookingService.bookTicket(email, selectedRoute, tickets);
            System.out.println("Booking successful!");

        } catch (NoRouteFoundException | OverbookedException e) {
            System.out.println("\n[ERROR]: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR]: Please enter a valid number for tickets.");
        }
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

        Station s1 = new Station("S1", "Bucharest North");
        Station s2 = new Station("S2", "Cluj-Napoca");
        adminService.addStation(s1);
        adminService.addStation(s2);


        Train t1 = new Train("T1", "InterCity Express", 50); // Capacity of 50
        adminService.addTrain(t1);

        LocalDateTime departure = LocalDateTime.now().withHour(10).withMinute(0);
        LocalDateTime arrival = LocalDateTime.now().withHour(18).withMinute(0);
        Route r1 = new Route("R1", t1, s1, s2, departure, arrival);
        adminService.addRoute(r1);
    }
}