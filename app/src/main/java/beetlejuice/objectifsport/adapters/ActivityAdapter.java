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

import androidx.annotation.Nullable;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.activities.DetailedActivityActivity;
import beetlejuice.objectifsport.activities.MainActivity;
import beetlejuice.objectifsport.model.activities.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The type Activity adapter.
 */
public class ActivityAdapter extends ArrayAdapter<Activity> {

    /**
     * The Context.
     */
    Context context;

    /**
     * Instantiates a new Activity adapter.
     *
     * @param context    the context
     * @param activities the activities
     */
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

        // Start the DetailedActivity activity page
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivityActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        // Remove an activity
        convertView.setOnLongClickListener(v -> {
            ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(30);
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.remove_activity))
                    .setMessage(context.getResources().getString(R.string.remove_activity_msg))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        DataManager.removeActivity(activity);
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

    @Nullable
    @Override
    public Activity getItem(int position) {
        return super.getItem(position);
    }

}
