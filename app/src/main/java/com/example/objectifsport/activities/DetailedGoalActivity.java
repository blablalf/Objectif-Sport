package com.example.objectifsport.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.objectifsport.R;

public class DetailedGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_detailed_goal);
    }

    public void backToMyGoals(View view) {
        onBackPressed();
    }
}
