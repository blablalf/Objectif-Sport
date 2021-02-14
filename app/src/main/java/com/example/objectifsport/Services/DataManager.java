package com.example.objectifsport.Services;

import com.example.objectifsport.model.Sport;

import java.util.ArrayList;

public class DataManager {

    private static final boolean isDemo = true;

    public DataManager() {
        if (!isDemo) {
            // TODO
        }
    }

    public ArrayList<Sport> getSports() {

        ArrayList<Sport> sports = new ArrayList<>();

        if (isDemo) {
            Sport tempSport = new Sport("Sport Name");
            sports.add(tempSport);
            sports.add(tempSport);
            sports.add(tempSport);
            sports.add(tempSport);
            sports.add(tempSport);
            sports.add(tempSport);
            sports.add(tempSport);
        } else {
            // TODO
        }


        return sports;
    }
}
