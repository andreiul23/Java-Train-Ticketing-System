package service;

import model.Booking;
import model.Route;
import model.Station;
import model.Train;
import data.BookingRepository;
import data.RouteRepository;
import data.StationRepository;
import data.TrainRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private TrainRepository trainRepository;
    private StationRepository stationRepository;
    private RouteRepository routeRepository;
    private BookingRepository bookingRepository;
    private EmailService emailService;

    public AdminService(TrainRepository trainRepository, StationRepository stationRepository,
                        RouteRepository routeRepository, BookingRepository bookingRepository,
                        EmailService emailService) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.routeRepository = routeRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    // --- TRAIN MANAGEMENT ---
    public void addTrain(Train train) {
        trainRepository.addTrain(train);
    }

    public void removeTrain(String trainId) {
        trainRepository.removeTrain(trainId);
    }

    public void modifyTrain(Train train) {
        trainRepository.modifyTrain(train);
    }

    // --- STATION MANAGEMENT ---
    public void addStation(Station station) {
        stationRepository.addStation(station);
    }

    public void removeStation(String stationId) {
        stationRepository.removeStation(stationId);
    }

    public void modifyStation(Station station) {
        stationRepository.modifyStation(station);
    }

    // --- ROUTE MANAGEMENT ---
    public void addRoute(Route route) {
        routeRepository.addRoute(route);
    }

    public void removeRoute(String routeId) {
        routeRepository.removeRoute(routeId);
    }

    public void modifyRoute(Route route) {
        routeRepository.modifyRoute(route);
    }

    // --- BOOKING & NOTIFICATION MANAGEMENT ---
    public List<Booking> getBookingsForTrain(String trainId) {
        List<Booking> trainBookings = new ArrayList<>();
        for (Booking booking : bookingRepository.getAllBookings()) {
            if (booking.getRoute().getTrain().getId().equals(trainId)) {
                trainBookings.add(booking);
            }
        }
        return trainBookings;
    }

    public void reportTrainDelay(String trainId, int delayMinutes) {
        Train train = trainRepository.getTrain(trainId);
        if (train != null) {
            train.setDelayMinutes(delayMinutes);

            List<Booking> affectedBookings = getBookingsForTrain(trainId);
            for (Booking booking : affectedBookings) {
                emailService.sendDelayNotification(booking.getCustomerEmail(), train.getName(), delayMinutes);
            }
        }
    }
}