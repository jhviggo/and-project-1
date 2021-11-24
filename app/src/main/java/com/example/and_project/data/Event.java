package com.example.and_project.data;

import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.Map;

public class Event {
    private String id;
    private String title;
    private Date dateTime;
    private String room;
    private String description;
    private int imageId;

    public Event(String title, int imageId) {
        this.title = title;
        this.id = "1";
        this.dateTime = null;
        this.room = "C02.15";
        this.description = " I'm Batman. It was a dog. It was a big dog. The first time I stole so that I wouldn't starve, yes. I lost many assumptions about the simple nature of right and wrong.";
        this.imageId = imageId;
    }

    public Event(String id, Map<String, Object> values) {
        this.id = id;
        title = (String) values.get("title");
        dateTime = ((Timestamp) values.get("datetime")) != null
                ? ((Timestamp) values.get("datetime")).toDate()
                : null;
        room = (String) values.get("room");
        description = (String) values.get("description");
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

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String toString() {
        return "id = " + id
                + ", title = " + title
                + ", dateTime = " + dateTime.toString()
                + ", room = " + room;
    }
}
