package data;

import model.Train;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class TrainRepository {
    private Map<String, Train> trains = new HashMap<>();

    public void addTrain(Train train) {
        trains.put(train.getId(), train);
    }

    public Train getTrain(String id) {
        return trains.get(id);
    }

    public Collection<Train> getAllTrains() {
        return trains.values();
    }

    public void removeTrain(String id) {
        trains.remove(id);
    }

    public void modifyTrain(Train train) {
        trains.put(train.getId(), train);
    }
}