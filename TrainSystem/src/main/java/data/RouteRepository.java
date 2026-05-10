package data;

import model.Route;
import java.util.ArrayList;
import java.util.List;

public class RouteRepository {
    private List<Route> routes = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
    }

    public List<Route> getAllRoutes() {
        return routes;
    }
}