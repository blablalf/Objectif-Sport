package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.ActivityAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyActivitiesFragment extends Fragment {

    private ActivityAdapter activityAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MyActivitiesFragment newInstance() {
        return new MyActivitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_activities_fragment, container, false);

        activityAdapter = new ActivityAdapter(view.getContext(), DataManager.getActivities());
        ListView listView = view.findViewById(R.id.activities_list);
        listView.setAdapter(activityAdapter);

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

    public ActivityAdapter getActivityAdapter() {
        return activityAdapter;
    }


}
