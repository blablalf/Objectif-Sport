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
import beetlejuice.objectifsport.adapters.GoalAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyGoalsFragment extends Fragment {

    private GoalAdapter goalAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MyGoalsFragment newInstance() {
        return new MyGoalsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate
        View view = inflater.inflate(R.layout.my_goal_fragment, container, false);

        // define adapter by an instance with the model and context
        goalAdapter = new GoalAdapter(view.getContext(), DataManager.getGoals());
        ListView listView = view.findViewById(R.id.goals_list);
        listView.setAdapter(goalAdapter);

        // get FloatingActionButton and populate it
        FloatingActionButton addGoalButton = view.findViewById(R.id.add_goal);
        addGoalButton.setOnClickListener(v -> {
            if (DataManager.getSports().isEmpty()) {
                Toast toast = Toast. makeText(view.getContext(),
                        view.getContext().getResources().getString(R.string.create_sport_before),
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // TODO
                AddGoalDialogFragment addGoalDialogFragment = AddGoalDialogFragment.newInstance();
                assert getFragmentManager() != null;
                addGoalDialogFragment.show(getFragmentManager(), "fragment_add_goal");
            }
        });

        return view;
    }

    // useful to notify
    public GoalAdapter getGoalAdapter() {
        return goalAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        goalAdapter.notifyDataSetChanged();
    }
}
