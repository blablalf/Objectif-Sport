package com.example.objectifsport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.activities.DetailedActivity;
import com.example.objectifsport.model.activities.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActivityAdapter extends ArrayAdapter<Activity> {

    Context context;

    public ActivityAdapter(Context context, ArrayList<Activity> activities) {
        super(context, 0, activities);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Activity activity = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item, parent, false);
        }

        // Lookup view for data population
        TextView activityDescription = convertView.findViewById(R.id.activity_description);
        TextView sportName = convertView.findViewById(R.id.sport_name);
        TextView creationDate = convertView.findViewById(R.id.creation_date);
        TextView activityStatus = convertView.findViewById(R.id.activity_status);


        // Populate the data into the template view using the data object
        assert activity != null;
        activityDescription.setText(activity.getActivityDescription());
        sportName.setText(activity.getSport().getName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(activity.getCreationDate()));

        activityStatus.setText((activity.isAchieved()) ? "✅":"❌");

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        convertView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.remove_activity))
                    .setMessage(context.getResources().getString(R.string.remove_activity_msg))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        DataManager dataManager = DataManager.getInstance();
                        dataManager.removeActivity(activity);
                        notifyDataSetChanged();
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
    public Activity getItem(int position) {
        return super.getItem(position);
    }

}
