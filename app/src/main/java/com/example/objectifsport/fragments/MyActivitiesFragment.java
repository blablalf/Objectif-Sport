package com.example.objectifsport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        getActivityAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivityAdapter() != null)
        getActivityAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivityAdapter() != null)
        getActivityAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivityAdapter() != null)
        getActivityAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivityAdapter() != null)
        getActivityAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivityAdapter() != null)
            getActivityAdapter().notifyDataSetChanged();
    }

    public ActivityAdapter getActivityAdapter() {
        return activityAdapter;
    }


}
