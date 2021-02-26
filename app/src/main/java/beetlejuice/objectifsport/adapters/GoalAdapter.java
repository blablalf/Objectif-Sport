package beetlejuice.objectifsport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.activities.DetailedGoalActivity;
import beetlejuice.objectifsport.activities.MainActivity;
import beetlejuice.objectifsport.model.goals.Goal;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        creationDate.setText(formatter.format(goal.getCreationDate()));

        // Get the status and set the good icon according to its status
        goalStatus.setText((goal.isAchieved()) ? "✅":
                (goal.hasDeadlineDate() && new Date().after(goal.getDeadlineDate()))?"❌":"❗️");

        // Start DetailedGoalActivity activity
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedGoalActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        // Remove a goal
        convertView.setOnLongClickListener(v -> {
            ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(30);
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.remove_goal))
                    .setMessage(context.getResources().getString(R.string.remove_goal_msg))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        DataManager.removeGoal(goal);
                        notifyDataSetChanged();
                        MainFragmentPageAdapter mFPA = ((MainActivity) context).getMainFragmentPageAdapter();
                        if (mFPA.getMySportsFragment() != null)
                            mFPA.getMySportsFragment().getSportAdapter().notifyDataSetChanged();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return false;
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
