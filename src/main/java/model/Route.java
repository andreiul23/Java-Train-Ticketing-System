package model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class Route {
    private String routeId;
    private Train train;
    private Station departureStation;
    private Station arrivalStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double basePrice;
    private Queue<String> waitlist;

    public Route(String routeId, Train train, Station departureStation, Station arrivalStation, LocalDateTime departureTime, LocalDateTime arrivalTime, double basePrice) {
        this.routeId = routeId;
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.waitlist = new LinkedList<>();
    }

    public String getRouteId() { return routeId; }
    public Train getTrain() { return train; }
    public Station getDepartureStation() { return departureStation; }
    public Station getArrivalStation() { return arrivalStation; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getBasePrice() { return basePrice; }
    public Queue<String> getWaitlist() { return waitlist; }

    public void addToWaitlist(String email) {
        waitlist.add(email);
    }

    public String popFromWaitlist() {
        return waitlist.poll();
    }
}