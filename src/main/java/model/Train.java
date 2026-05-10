package model;

public class Train {
    private String id;
    private String name;
    private int capacity;
    private int delayMinutes;

    public Train(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.delayMinutes = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getDelayMinutes() { return delayMinutes; }

    public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }
}