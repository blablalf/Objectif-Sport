package com.example.objectifsport.model.goals;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

import java.util.Calendar;
import java.util.UUID;

public abstract class Goal {

    private String type; // Could be time, distance or timeDistance
    private Calendar startDate, endDate;
    private UUID sportId;

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    private boolean achieved;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        achieved = false;
    }

}
