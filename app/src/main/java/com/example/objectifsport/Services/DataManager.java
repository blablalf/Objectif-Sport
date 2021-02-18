package com.example.objectifsport.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;
import com.example.objectifsport.model.goals.Goal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class DataManager {

    static SharedPreferences userData;
    private static ArrayList<Sport> sports;
    private static ArrayList<Activity> activities;
    private static ArrayList<Goal> goals;

    public static Sport getSport(UUID sportId) {
        for (Sport sport : sports)
            if (sport.getId().equals(sportId)) return sport;
        return new Sport("error");
    }

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

    public static void generateFakeSports() {
        Sport tempSport = new Sport("Sport Name");
        for (int i = 0; i < 10 ; i++){
            sports.add(tempSport);
        }
    }

    public static ArrayList<Sport> getSports() {
        return sports;
    }

    public static ArrayList<Activity> getActivities() {
        return activities;
    }

    public static void removeSport(Sport sport) {
        Iterator<Activity> activityIterator = activities.iterator();
        while (activityIterator.hasNext())
            if (activityIterator.next().getSport() == sport) activityIterator.remove();
        sports.remove(sport);
        save();
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
        save();
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
        save();
    }

    public static ArrayList<Goal> getGoals() {
        return goals;
    }

    public static void addSport(Sport sport) {
        sports.add(sport);
        save();
    }

}
