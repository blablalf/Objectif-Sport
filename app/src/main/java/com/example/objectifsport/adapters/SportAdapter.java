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
import com.example.objectifsport.activities.MainActivity;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;
import com.example.objectifsport.model.goals.Goal;

import java.util.ArrayList;

public class SportAdapter extends ArrayAdapter<Sport> {

    private final Context context;

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
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.remove_sport))
                    .setMessage(context.getResources().getString(R.string.remove_sport_msg))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        System.out.println("Sport_value : " + sport.getName());
                        System.out.println("Sport_position : " + DataManager.getSports());
                        DataManager.removeSport(sport);
                        notifyDataSetChanged();
                        MainFragmentPageAdapter mFPA = ((MainActivity) context).getMainFragmentPageAdapter();
                        if (mFPA.getMyActivitiesFragment() != null)
                            mFPA.getMyActivitiesFragment().getActivityAdapter().notifyDataSetChanged();
                        if (mFPA.getMyGoalsFragment() != null)
                            mFPA.getMyGoalsFragment().getGoalAdapter().notifyDataSetChanged();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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

