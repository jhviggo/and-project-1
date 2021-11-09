package com.example.and_project;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private String title;
    private LocalDateTime dateTime;
    private String room;
    private String description;
    private int imageId;

    Event(String title, int imageId) {
        this.title = title;
        this.id = 1;
        this.dateTime = LocalDateTime.now();
        this.room = "C02.15";
        this.description = " I'm Batman. It was a dog. It was a big dog. The first time I stole so that I wouldn't starve, yes. I lost many assumptions about the simple nature of right and wrong.";
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
