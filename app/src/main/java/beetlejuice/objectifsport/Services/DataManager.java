package beetlejuice.objectifsport.Services;

import android.content.Context;
import android.content.SharedPreferences;

import beetlejuice.objectifsport.model.Sport;
import beetlejuice.objectifsport.model.activities.Activity;
import beetlejuice.objectifsport.model.goals.Goal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * The type Data manager.
 */
public class DataManager {

    /**
     * The User data.
     */
    static SharedPreferences userData;
    private static ArrayList<Sport> sports;
    private static ArrayList<Activity> activities;
    private static ArrayList<Goal> goals;

    /**
     * Gets sport.
     *
     * @param sportId the sport id
     * @return the sport
     */
    public static Sport getSport(UUID sportId) {
        for (Sport sport : sports)
            if (sport.getId().equals(sportId)) return sport;
        return new Sport("error");
    }

    /**
     * Load.
     *
     * @param context the context
     */
// Load all data from SharedPreferences
    public static void load(Context context){
        userData = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = userData.getString("sports", "");
        if (!json.equals("")){
            Type type = new TypeToken< ArrayList < Sport >>() {}.getType();
            sports = gson.fromJson(json, type);
        } else {
            sports = new ArrayList<>();
        }

        json = userData.getString("activities", "");
        if (!json.equals("")){
            Type type = new TypeToken< ArrayList < Activity >>() {}.getType();
            activities = gson.fromJson(json, type);
        } else {
            activities = new ArrayList<>();
        }

        json = userData.getString("goals", "");
        if (!json.equals("")){
            Type type = new TypeToken< ArrayList < Goal >>() {}.getType();
            goals = gson.fromJson(json, type);
        } else {
            goals = new ArrayList<>();
        }
    }

    /**
     * Save.
     */
// Save all data into SharedPreferences
    public static void save(){
        Gson gson = new Gson();
        String sportsJson = gson.toJson(sports);
        String activitiesJson = gson.toJson(activities);
        String goalsJson = gson.toJson(goals);
        userData.edit()
                .putString("sports", sportsJson)
                .putString("activities", activitiesJson)
                .putString("goals", goalsJson)
                .apply();
    }

    /**
     * Gets sports.
     *
     * @return the sports
     */
    public static ArrayList<Sport> getSports() {
        return sports;
    }

    /**
     * Gets activities.
     *
     * @return the activities
     */
    public static ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * Remove sport.
     *
     * @param sport the sport
     */
    public static void removeSport(Sport sport) {
        Iterator<Goal> goalIterator = goals.iterator();
        while (goalIterator.hasNext())
            if (goalIterator.next().getSport() == sport) goalIterator.remove();
        Iterator<Activity> activityIterator = activities.iterator();
        while (activityIterator.hasNext())
            if (activityIterator.next().getSport() == sport) activityIterator.remove();
        sports.remove(sport);
        save();
    }

    /**
     * Remove activity.
     *
     * @param activity the activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
        save();
    }

    /**
     * Add activity.
     *
     * @param activity the activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
        save();
    }

    /**
     * Gets goals.
     *
     * @return the goals
     */
    public static ArrayList<Goal> getGoals() {
        return goals;
    }

    /**
     * Add sport.
     *
     * @param sport the sport
     */
    public static void addSport(Sport sport) {
        sports.add(sport);
        save();
    }

    /**
     * Remove goal.
     *
     * @param goal the goal
     */
    public static void removeGoal(Goal goal) {
        goals.remove(goal);
        save();
    }

    /**
     * Add goal.
     *
     * @param goal the goal
     */
    public static void addGoal(Goal goal) {
        goals.add(goal);
        save();
    }
}
