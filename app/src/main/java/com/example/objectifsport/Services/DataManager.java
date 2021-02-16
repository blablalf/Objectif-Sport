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

import static android.content.Context.MODE_PRIVATE;

public class DataManager {

    static SharedPreferences userData;
    private static ArrayList<Sport> sports;
    private static ArrayList<Activity> activities;
    private static ArrayList<Goal> goals;

    // first instantiation to get context
    public DataManager(Context context) {
        userData = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        load();
    }

    // private constructor to make new instances
    private DataManager(SharedPreferences userData) {
        DataManager.userData = userData;
    }

    // others instantiations to get sharedActivity without context
    public static DataManager getInstance() {
        return new DataManager(userData);
    }

    public void load(){
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

    public ArrayList<Sport> getSports() {
        return sports;
    }

    public static ArrayList<Activity> getActivities() {
        return activities;
    }

    public void removeSport(int position) {
        sports.remove(position);
        save();
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
        save();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        save();
    }

    public static SharedPreferences getUserData() {
        return userData;
    }

    public static ArrayList<Goal> getGoals() {
        return goals;
    }

    public void addSport(Sport sport) {
        sports.add(sport);
        save();
    }
}
