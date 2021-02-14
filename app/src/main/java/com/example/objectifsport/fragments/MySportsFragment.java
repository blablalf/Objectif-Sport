package com.example.objectifsport.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.SportAdapter;
import com.example.objectifsport.model.Sport;

import java.util.ArrayList;

public class MySportsFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private DataManager dataManager = new DataManager();

    public static MySportsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MySportsFragment fragment = new MySportsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sports, container, false);
        SportAdapter sportAdapter = new SportAdapter(view.getContext(), dataManager.getSports());
        ListView listView = view.findViewById(R.id.sports_list);
        listView.setAdapter(sportAdapter);
        return view;
    }
}