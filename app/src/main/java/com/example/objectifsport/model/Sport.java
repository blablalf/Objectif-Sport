package com.example.objectifsport.model;

public class Sport {

    private String name;
    private final int authorizedGoals; // 0 for all goals | 1 for time goal | 2 for distance goal

    public Sport(String name) {
        this.name = name;
        authorizedGoals = 0;
    }

    public Sport(String name, int authorizedGoals) {
        this.name = name;
        this.authorizedGoals = authorizedGoals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuthorizedGoals() {
        return authorizedGoals;
    }
}
