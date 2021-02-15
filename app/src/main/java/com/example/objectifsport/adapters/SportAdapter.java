package com.example.objectifsport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

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
        LinearLayout sportView = convertView.findViewById(R.id.sport_view);


        // Populate the data into the template view using the data object
        sportName.setText(sport.getName());
        activitiesAmount.setText(String.valueOf(sport.getActivities().size()));
        goalsInProgressAmount.setText(String.valueOf(sport.getGoals().size()));
        goalsDoneAmount.setText(String.valueOf(sport.getAchievedGoals().size()));

        sportView.setOnClickListener(v -> {
            //Intent intent = new Intent(context, DetailSportActivity.class);
            //context.startActivity(intent);
        });

        sportView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Do you want to remove this sport ?");
            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());

            builder.setPositiveButton("REMOVE", (dialog, which) -> {
                DataManager dataManager = DataManager.getInstance();
                dataManager.getSports().remove(position);
                dataManager.save();
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

