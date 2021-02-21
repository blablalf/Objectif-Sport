package com.example.objectifsport.model.goals;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

import java.util.Calendar;
import java.util.UUID;

public class Goal {

    private Calendar creationDate, startDate, endDate;
    private UUID sportId;
    private String description;

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    private boolean achieved;

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Sport getSport() {
        return DataManager.getSport(sportId);
    }

    public Goal(Sport sport, Calendar startDate, Calendar endDate) {
        sportId = sport.getId();
        this.startDate = startDate;
        this.endDate = endDate;
        achieved = false;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }
}
