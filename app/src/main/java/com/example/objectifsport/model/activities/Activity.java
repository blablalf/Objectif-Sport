package com.example.objectifsport.model.activities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.objectifsport.model.Sport;
import com.example.objectifsport.utils.DateTypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(foreignKeys = @ForeignKey(entity = Sport.class,
        parentColumns = "id",
        childColumns = "sportId"))
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long sportId;

    // General part
    @TypeConverters(DateTypeConverter.class)
    private Date creationDate;
    private String activityDescription; // something like "skipping rope + pumps" for Boxing sport
    private boolean isAchieved;

    public Activity(long sportId, String activityDescription) {
        this.sportId = sportId;
        this.activityDescription = activityDescription;
        creationDate = new Date();
        isAchieved = false;
        activityTime = 0;
    }

    @Ignore
    public Activity(String activityDescription) {
        this.activityDescription = activityDescription;
        creationDate = new Date();
        isAchieved = false;
        activityTime = 0;
    }

    // Time part
    private long activityTime;

    public String getFormattedActivityTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss.SSS", Locale.getDefault());
        return formatter.format(activityTime);
    }

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long timestamp) {
        this.activityTime = timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public void setStartCoordinateX(double startCoordinateX) {
        this.startCoordinateX = startCoordinateX;
    }

    public void setStartCoordinateY(double startCoordinateY) {
        this.startCoordinateY = startCoordinateY;
    }

    public void setEndCoordinateX(double endCoordinateX) {
        this.endCoordinateX = endCoordinateX;
    }

    public void setEndCoordinateY(double endCoordinateY) {
        this.endCoordinateY = endCoordinateY;
    }

    // Distance part
    private double startCoordinateX, startCoordinateY, endCoordinateX, endCoordinateY, completedDistance;

    public double getStartCoordinateX() {
        return startCoordinateX;
    }

    public double getStartCoordinateY() {
        return startCoordinateY;
    }

    public double getEndCoordinateX() {
        return endCoordinateX;
    }

    public double getEndCoordinateY() {
        return endCoordinateY;
    }

    public void setStartCoordinate(double startCoordinateX, double startCoordinateY) {
        this.startCoordinateX = startCoordinateX;
        this.startCoordinateY = startCoordinateY;
    }

    public void setEndCoordinate(double endCoordinateX, double endCoordinateY) {
        this.endCoordinateX = endCoordinateX;
        this.endCoordinateY = endCoordinateY;
    }

    public double getCompletedDistance() {
        return completedDistance;
    }

    public void setCompletedDistance(double completedDistance) {
        this.completedDistance += completedDistance;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }

    public long getId() {
        return id;
    }

    public long getSportId() {
        return sportId;
    }

    public void setSportId(long sportId) {
        this.sportId = sportId;
    }
}
