package model;
import java.time.LocalDateTime;

public class Route {
    private String routeId;
    private Train train;
    private Station departureStation;
    private Station arrivalStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public Route(String routeId, Train train, Station departureStation, Station arrivalStation, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.routeId = routeId;
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getRouteId() { return routeId; }
    public Train getTrain() { return train; }
    public Station getDepartureStation() { return departureStation; }
    public Station getArrivalStation() { return arrivalStation; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
}
