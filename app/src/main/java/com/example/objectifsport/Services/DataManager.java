package com.example.objectifsport.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DataManager {

    static SharedPreferences userData;
    private static ArrayList<Sport> sports;

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
    }

    public void save(){
        Gson gson = new Gson();
        String json = gson.toJson(sports);
        userData.edit().putString("sports", json).apply();
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

    public ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<>();
        for (Sport sport : getSports()) {
            activities.addAll(sport.getActivities());
        }
        return activities;
    }

    public void removeSport(int position) {
        sports.remove(position);
        save();
    }

    public void removeActivity(Activity activity) {
        for (Sport sport : getSports()) {
            if (sport.getName().equals(activity.getSportName())) sport.removeActivity(activity);
        }
        save();
    }
}
