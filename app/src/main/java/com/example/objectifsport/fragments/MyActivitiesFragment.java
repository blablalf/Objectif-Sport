package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.ActivityAdapter;

public class MyActivitiesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MyActivitiesFragment newInstance() {
        MyActivitiesFragment fragment = new MyActivitiesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_activities_fragment, container, false);

        ActivityAdapter activityAdapter = new ActivityAdapter(view.getContext(), DataManager.getInstance().getActivities());
        ListView listView = view.findViewById(R.id.activities_list);
        listView.setAdapter(activityAdapter);

        return view;
    }


}
