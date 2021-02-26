package beetlejuice.objectifsport.model.activities;

import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.model.Sport;
import com.mapbox.geojson.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Activity {

    // General part
    private final UUID sportId;
    private final Date creationDate;
    private final String activityDescription; // something like "skipping rope + pumps" for Boxing sport
    private boolean isAchieved;

    // Time part
    private long activityTime;

    // Distance part
    private double completedDistance;
    private ArrayList<ArrayList<Point>> trajectories;

    public Activity(Sport sport, String activityDescription) {
        sportId = sport.getId();
        this.activityDescription = activityDescription;
        creationDate = new Date();
        isAchieved = false;
        activityTime = 0;
        completedDistance = 0;
        if (sport.getAuthorizedGoals() != 1) { // time only
            trajectories = new ArrayList<>();
        }
    }

    public String getFormattedActivityTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        return formatter.format(activityTime - 3600000L);
    }

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long timestamp) {
        activityTime = timestamp;
    }

    public ArrayList<ArrayList<Point>> getTrajectories() {
        return trajectories;
    }

    public double getCompletedDistance() {
        return completedDistance;
    }

    public void setCompletedDistance(double completedDistance) {
        this.completedDistance = completedDistance;
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

    public Sport getSport() {
        return DataManager.getSport(sportId);
    }

}
