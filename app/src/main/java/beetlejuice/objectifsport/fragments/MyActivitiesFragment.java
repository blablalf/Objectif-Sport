package beetlejuice.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.adapters.ActivityAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The type My activities fragment.
 */
public class MyActivitiesFragment extends Fragment {

    private ActivityAdapter activityAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * New instance my activities fragment.
     *
     * @return the my activities fragment
     */
    public static MyActivitiesFragment newInstance() {
        return new MyActivitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate
        View view = inflater.inflate(R.layout.my_activities_fragment, container, false);

        // initialize adapter with model and context
        activityAdapter = new ActivityAdapter(view.getContext(), DataManager.getActivities());

        // get ListView and set it the adapter
        ListView listView = view.findViewById(R.id.activities_list);
        listView.setAdapter(activityAdapter);

        // get FloatingActionButton and populate it
        FloatingActionButton addActivityButton = view.findViewById(R.id.add_activity);
        addActivityButton.setOnClickListener(v -> {
            if (DataManager.getSports().isEmpty()) {
                Toast toast = Toast. makeText(view.getContext(),
                        view.getContext().getResources().getString(R.string.create_sport_before),
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                AddActivityDialogFragment addActivityDialogFragment = AddActivityDialogFragment.newInstance();
                assert getFragmentManager() != null;
                addActivityDialogFragment.show(getFragmentManager(), "fragment_add_activity");
            }
        });

        return view;
    }

    /**
     * Gets activity adapter.
     *
     * @return the activity adapter
     */
// useful to notify
    public ActivityAdapter getActivityAdapter() {
        return activityAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        activityAdapter.notifyDataSetChanged();
    }
}
