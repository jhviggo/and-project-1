package com.example.and_project.data;

import java.util.Map;

public class User {
    private String name;
    private String uid;
    private String description;
    private String email;

    public User(Map<String, Object> values) {
        name = (String) values.get("name");
        uid = (String) values.get("uid");
        description = ((String) values.getOrDefault("description", "")).replace("\\n", "\n");
        email = (String) values.get("email");
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }
}
