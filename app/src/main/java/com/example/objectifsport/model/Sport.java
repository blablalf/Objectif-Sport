package com.example.objectifsport.model;

import java.util.UUID;

public class Sport {

    private final String name;
    private final UUID id;
    private final int authorizedGoals; // 0 for all goals | 1 for time goal | 2 for distance goal

    public Sport(String name) {
        this.name = name;
        authorizedGoals = 0;
        id = UUID.randomUUID();
    }

    public Sport(String name, int authorizedGoals) {
        this.name = name;
        this.authorizedGoals = authorizedGoals;
        id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public int getAuthorizedGoals() {
        return authorizedGoals;
    }

    public UUID getId() {
        return id;
    }
}
