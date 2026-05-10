package data;

import model.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class StationRepository {
    private Map<String, Station> stations = new HashMap<>();

    public void addStation(Station station) {
        stations.put(station.getId(), station);
    }

    public Station getStation(String id) {
        return stations.get(id);
    }

    public Collection<Station> getAllStations() {
        return new ArrayList<>(stations.values());
    }

    public void removeStation(String id) {
        stations.remove(id);
    }

    public void modifyStation(Station station) {
        stations.put(station.getId(), station);
    }
}