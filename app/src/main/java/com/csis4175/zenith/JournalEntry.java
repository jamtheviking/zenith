package com.csis4175.zenith;

public class JournalEntry {
    private String mood;
    private String description;
    private String timestamp;

    public JournalEntry(String mood, String description, String timestamp) {
        this.mood = mood;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getMood() {
        return mood;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
