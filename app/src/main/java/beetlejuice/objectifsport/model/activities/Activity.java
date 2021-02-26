package beetlejuice.objectifsport.model.activities;

import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.model.Sport;
import com.mapbox.geojson.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * The type Activity.
 */
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

    /**
     * Instantiates a new Activity.
     *
     * @param sport               the sport
     * @param activityDescription the activity description
     */
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

    /**
     * Gets formatted activity time.
     *
     * @return the formatted activity time
     */
    public String getFormattedActivityTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        return formatter.format(activityTime - 3600000L);
    }

    /**
     * Gets activity time.
     *
     * @return the activity time
     */
    public long getActivityTime() {
        return activityTime;
    }

    /**
     * Sets activity time.
     *
     * @param timestamp the timestamp
     */
    public void setActivityTime(long timestamp) {
        activityTime = timestamp;
    }

    /**
     * Gets trajectories.
     *
     * @return the trajectories
     */
    public ArrayList<ArrayList<Point>> getTrajectories() {
        return trajectories;
    }

    /**
     * Gets completed distance.
     *
     * @return the completed distance
     */
    public double getCompletedDistance() {
        return completedDistance;
    }

    /**
     * Sets completed distance.
     *
     * @param completedDistance the completed distance
     */
    public void setCompletedDistance(double completedDistance) {
        this.completedDistance = completedDistance;
    }

    /**
     * Gets activity description.
     *
     * @return the activity description
     */
    public String getActivityDescription() {
        return activityDescription;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Is achieved boolean.
     *
     * @return the boolean
     */
    public boolean isAchieved() {
        return isAchieved;
    }

    /**
     * Sets achieved.
     *
     * @param achieved the achieved
     */
    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }

    /**
     * Gets sport.
     *
     * @return the sport
     */
    public Sport getSport() {
        return DataManager.getSport(sportId);
    }

}
