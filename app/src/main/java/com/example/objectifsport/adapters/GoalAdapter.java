package com.example.objectifsport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.activities.DetailedActivity;
import com.example.objectifsport.activities.MainActivity;
import com.example.objectifsport.model.goals.Goal;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GoalAdapter extends ArrayAdapter<Goal> {

    private final Context context;

    public GoalAdapter(@NonNull Context context, @NonNull List<Goal> objects) {
        super(context, 0, objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Goal goal = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.goal_item, parent, false);
        }

        // Lookup view for data population
        TextView goalDescription = convertView.findViewById(R.id.goal_description);
        TextView sportName = convertView.findViewById(R.id.sport_name);
        TextView creationDate = convertView.findViewById(R.id.creation_date);
        TextView goalStatus = convertView.findViewById(R.id.goal_status);

        // Populate the data into the template view using the data object
        assert goal != null;
        goalDescription.setText(goal.getDescription());
        sportName.setText(goal.getSport().getName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(goal.getCreationDate().getTime()));

        goalStatus.setText((goal.isAchieved()) ? "✅":"❌");

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        convertView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.remove_goal))
                    .setMessage(context.getResources().getString(R.string.remove_goal_msg))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        DataManager.removeGoal(goal);
                        notifyDataSetChanged();
                        ((MainActivity) context).getMainFragmentPageAdapter()
                                .getMySportsFragment()
                                .getSportAdapter()
                                .notifyDataSetChanged();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return false;
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
