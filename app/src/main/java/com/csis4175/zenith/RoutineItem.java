package com.csis4175.zenith;

public class RoutineItem {
    private String name;
    private String details;
    private String time;

    public RoutineItem(String name, String details, String time) {
        this.name = name;
        this.details = details;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getTime() {
        return time;
    }
}
