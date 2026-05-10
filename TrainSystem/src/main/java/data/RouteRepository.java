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

    public void removeRoute(String routeId) {
        routes.removeIf(route -> route.getRouteId().equals(routeId));
    }

    public void modifyRoute(Route updatedRoute) {
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getRouteId().equals(updatedRoute.getRouteId())) {
                routes.set(i, updatedRoute);
                return;
            }
        }
    }
}