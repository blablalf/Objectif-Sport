package beetlejuice.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.activities.MainActivity;
import beetlejuice.objectifsport.adapters.MainFragmentPageAdapter;
import beetlejuice.objectifsport.model.Sport;
import beetlejuice.objectifsport.model.activities.Activity;

import java.util.ArrayList;
import java.util.Objects;

public class AddActivityDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText activityDescription;

    public AddActivityDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddActivityDialogFragment newInstance() {
        return new AddActivityDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_activity_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Get views
        activityDescription = view.findViewById(R.id.activity_description);
        Spinner sportSelect = view.findViewById(R.id.sport_select);
        Button cancel = view.findViewById(R.id.cancel);
        Button addActivity = view.findViewById(R.id.add_activity);

        // Get the models
        Objects.requireNonNull(getDialog()).setTitle("Add Activity");
        ArrayList<String> sportNames = new ArrayList<>();
        for (Sport sport : DataManager.getSports()) sportNames.add(sport.getName());

        // Populate views
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, sportNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSelect.setAdapter(adapter);

        activityDescription.requestFocus(); // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Try to add activity
        addActivity.setOnClickListener(v -> {
            // Add activity
            Activity activity = new Activity(DataManager.getSports().get(
                    sportSelect.getSelectedItemPosition()), activityDescription.getText().toString());
            DataManager.addActivity(activity);
            MainFragmentPageAdapter mFPA = ((MainActivity) Objects.requireNonNull(getActivity()))
                    .getMainFragmentPageAdapter();
            if (mFPA.getMyActivitiesFragment() != null)
                mFPA.getMyActivitiesFragment().getActivityAdapter().notifyDataSetChanged();

            // close dialog
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            // close dialog
            dismiss();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
