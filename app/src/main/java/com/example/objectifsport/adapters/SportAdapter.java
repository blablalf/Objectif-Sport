package com.example.objectifsport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;
import com.example.objectifsport.model.goals.Goal;

import java.util.ArrayList;

public class SportAdapter extends ArrayAdapter<Sport> {

    Context context;

    public SportAdapter(Context context, ArrayList<Sport> announcements) {
        super(context, 0, announcements);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Sport sport = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sport_item, parent, false);
        }

        // Lookup view for data population
        TextView sportName = convertView.findViewById(R.id.sport_name);
        TextView activitiesAmount = convertView.findViewById(R.id.activities_amount);
        TextView goalsInProgressAmount = convertView.findViewById(R.id.goals_in_progress_amount);
        TextView goalsDoneAmount = convertView.findViewById(R.id.goals_done_amount);


        // Populate the data into the template view using the data object
        assert sport != null;
        sportName.setText(sport.getName());

        int activitiesAmountValue = 0;
        for (Activity activity : DataManager.getActivities())
            if (sport == activity.getSport()) activitiesAmountValue++;

        int goalsAmountValue = 0;
        for (Goal goal : DataManager.getGoals())
            if (sport == goal.getSport()) goalsAmountValue++;

        int achievedGoalsAmountValue = 0;
        for (Goal goal : DataManager.getGoals())
            if (sport == goal.getSport() && goal.isAchieved()) achievedGoalsAmountValue++;

        activitiesAmount.setText(String.valueOf(activitiesAmountValue));
        goalsInProgressAmount.setText(String.valueOf(goalsAmountValue));
        goalsDoneAmount.setText(String.valueOf(achievedGoalsAmountValue));

        convertView.setOnClickListener(v -> {
            //Intent intent = new Intent(context, DetailSportActivity.class);
            //context.startActivity(intent);
        });

        convertView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Do you want to remove this sport ?");
            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());

            builder.setPositiveButton("REMOVE", (dialog, which) -> {
                DataManager dataManager = DataManager.getInstance();
                dataManager.removeSport(position);
                notifyDataSetChanged();
            });

            builder.show();
            return false;
        });

        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public Sport getItem(int position) {
        return super.getItem(position);
    }

}

