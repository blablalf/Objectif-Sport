package com.example.objectifsport.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.SportAdapter;
import com.example.objectifsport.model.Sport;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MySportsFragment extends Fragment {

    private DataManager dataManager;
    private FloatingActionButton addSportButton;

    public static MySportsFragment newInstance() {
        MySportsFragment fragment = new MySportsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = DataManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_sports, container, false);
        SportAdapter sportAdapter = new SportAdapter(view.getContext(), dataManager.getSports());
        ListView listView = view.findViewById(R.id.sports_list);
        listView.setAdapter(sportAdapter);

        addSportButton = view.findViewById(R.id.add_sport);
        addSportButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Add Sport");

            final EditText input = new EditText(view.getContext());
            input.setHint(R.string.sport_name);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("add", (dialog, which) -> {
                dataManager.getSports().add(new Sport(input.getText().toString()));
                dataManager.save();
            });

            builder.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());

            builder.show();
            /*
            System.out.println("Add sport");

            FragmentManager fm = getFragmentManager();
            AddSportDialogFragment addSportDialogFragment = AddSportDialogFragment.newInstance("Add sport");
            addSportDialogFragment.show(fm, "fragment_add_sport");*/
        });

        return view;
    }
}