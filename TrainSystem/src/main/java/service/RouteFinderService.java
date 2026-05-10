package service;

import exception.NoRouteFoundException;
import model.Route;
import model.Station;
import data.RouteRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RouteFinderService {
    private RouteRepository routeRepository;

    public RouteFinderService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<List<Route>> findPossibleRoutes(Station start, Station end) throws NoRouteFoundException {
        List<List<Route>> validItineraries = new ArrayList<>();
        List<Route> allRoutes = routeRepository.getAllRoutes();
        Queue<List<Route>> queue = new LinkedList<>();

        for (Route route : allRoutes) {
            if (route.getDepartureStation().getId().equals(start.getId())) {
                List<Route> initialPath = new ArrayList<>();
                initialPath.add(route);
                queue.add(initialPath);
            }
        }

        while (!queue.isEmpty()) {
            List<Route> currentPath = queue.poll();
            Route lastRoute = currentPath.get(currentPath.size() - 1);

            if (lastRoute.getArrivalStation().getId().equals(end.getId())) {
                validItineraries.add(new ArrayList<>(currentPath));
                continue;
            }

            for (Route nextRoute : allRoutes) {
                if (nextRoute.getDepartureStation().getId().equals(lastRoute.getArrivalStation().getId()) &&
                        nextRoute.getDepartureTime().isAfter(lastRoute.getArrivalTime()) &&
                        !stationAlreadyVisited(currentPath, nextRoute.getArrivalStation())) {

                    List<Route> newPath = new ArrayList<>(currentPath);
                    newPath.add(nextRoute);
                    queue.add(newPath);
                }
            }
        }

        if (validItineraries.isEmpty()) {
            throw new NoRouteFoundException("No possible link found between " + start.getName() + " and " + end.getName());
        }

        return validItineraries;
    }

    private boolean stationAlreadyVisited(List<Route> path, Station station) {
        for (Route r : path) {
            if (r.getDepartureStation().getId().equals(station.getId()) ||
                    r.getArrivalStation().getId().equals(station.getId())) {
                return true;
            }
        }
        return false;
    }
}