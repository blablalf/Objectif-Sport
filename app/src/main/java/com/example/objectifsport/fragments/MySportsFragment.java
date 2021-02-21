package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.SportAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MySportsFragment extends Fragment {

    private SportAdapter sportAdapter;

    public static MySportsFragment newInstance() {
        return new MySportsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_sports_fragment, container, false);
        sportAdapter = new SportAdapter(view.getContext(), DataManager.getSports());
        ListView listView = view.findViewById(R.id.sports_list);
        listView.setAdapter(sportAdapter);

        FloatingActionButton addSportButton = view.findViewById(R.id.add_sport);
        addSportButton.setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            AddSportDialogFragment addSportDialogFragment = AddSportDialogFragment.newInstance();
            assert fm != null;
            addSportDialogFragment.show(fm, "fragment_add_sport");
        });



        return view;
    }

    public BaseAdapter getSportAdapter() {
        return sportAdapter;
    }
}