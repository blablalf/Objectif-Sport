package beetlejuice.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.activities.MainActivity;
import beetlejuice.objectifsport.adapters.MainFragmentPageAdapter;
import beetlejuice.objectifsport.model.Sport;

import java.util.Objects;

/**
 * The type Add sport dialog fragment.
 */
public class AddSportDialogFragment extends DialogFragment {

    private EditText sportName;
    private CheckBox timeGoal;
    private CheckBox distanceGoal;
    private Button addSport;

    /**
     * Instantiates a new Add sport dialog fragment.
     */
    public AddSportDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    /**
     * New instance add sport dialog fragment.
     *
     * @return the add sport dialog fragment
     */
    public static AddSportDialogFragment newInstance() {
        return new AddSportDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_sport_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get views
        sportName = view.findViewById(R.id.sport_name);
        timeGoal = view.findViewById(R.id.time_goal);
        distanceGoal = view.findViewById(R.id.distance_goal);
        Button cancel = view.findViewById(R.id.cancel);
        addSport = view.findViewById(R.id.add_sport);

        // Populate views
        Objects.requireNonNull(getDialog()).setTitle("Add sport");
        // Show soft keyboard automatically and request focus to field
        sportName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        timeGoal.setOnCheckedChangeListener((buttonView, isChecked) ->
                addSport.setEnabled(isChecked || distanceGoal.isChecked()));

        distanceGoal.setOnCheckedChangeListener((buttonView, isChecked) ->
                addSport.setEnabled(isChecked || timeGoal.isChecked()));

        // Add sport
        addSport.setOnClickListener(v -> {
            // learn what type of goals are needed
            int authorizedGoals;
            if (timeGoal.isChecked() && !distanceGoal.isChecked()) authorizedGoals = 1;
            else if (distanceGoal.isChecked() && !timeGoal.isChecked())  authorizedGoals = 2;
            else authorizedGoals = 0;

            // create the new sport
            DataManager.addSport(new Sport(sportName.getText().toString(), authorizedGoals));
            MainFragmentPageAdapter mFPA = ((MainActivity) getActivity()).getMainFragmentPageAdapter();
            if (mFPA.getMySportsFragment() != null)
                mFPA.getMySportsFragment().getSportAdapter().notifyDataSetChanged();

            // close dialog
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            // close dialog
            dismiss();
        });
    }

}
