package com.example.objectifsport.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Sport {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private final String name;
    private final int authorizedGoals; // 0 for all goals | 1 for time goal | 2 for distance goal

    @Ignore
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

    public int getAuthorizedGoals() {
        return authorizedGoals;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
