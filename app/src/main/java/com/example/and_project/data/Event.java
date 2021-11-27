package com.example.and_project.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event implements Serializable {
    private String id;
    private String title;
    private String dateTime;
    private String room;
    private String description;
    private String organizer;
    private boolean hasImage;
    private List<String> attendees;

    public Event(String id, Map<String, Object> values) {
        this.id = id;
        title = (String) values.get("title");
        dateTime = (String) values.get("isoDate");
        room = (String) values.get("room");
        description = ((String) values.getOrDefault("description", "")).replace("\\n", "\n");
        attendees = (List<String>) values.getOrDefault("attendees", new ArrayList<>());
        organizer = (String) values.get("organizer");
        hasImage = (Boolean) values.getOrDefault("hasImage", false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public String getOrganizer() {
        return organizer;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public String getPrettyDate() {
        String[] dateArray = dateTime.split("T")[0].split("-");
        String prettyDate = String.format("%s/%s %s", dateArray[2], dateArray[1], dateArray[0]);
        return prettyDate;
    }

    public String getPrettyTime() {
        return dateTime.split("T")[1];
    }

    public String toString() {
        return "id = " + id
                + ", title = " + title
                + ", dateTime = " + dateTime.toString()
                + ", room = " + room;
    }
}
