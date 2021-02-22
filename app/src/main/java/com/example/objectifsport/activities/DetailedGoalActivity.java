package com.example.objectifsport.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.goals.Goal;

import org.threeten.bp.Duration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_detailed_goal);

        TextView goalDescription = findViewById(R.id.goal_description);
        TextView sportName = findViewById(R.id.sport_name);
        TextView creationDate = findViewById(R.id.creation_date);
        TextView goalStatus = findViewById(R.id.goal_status);

        LinearLayout deadlineDateContainer = findViewById(R.id.deadline_date_container);
        TextView deadlineDate = findViewById(R.id.deadline_date);

        LinearLayout timeGoalContainer = findViewById(R.id.time_goal_container);
        TextView timeGoal = findViewById(R.id.time_goal);
        TextView timeReached = findViewById(R.id.no_time_reached);
        TextView timePercentage = findViewById(R.id.time_percentage);
        ProgressBar timeProgress = findViewById(R.id.time_progress);

        LinearLayout distanceGoalContainer = findViewById(R.id.distance_goal_container);
        TextView distanceGoal = findViewById(R.id.distance_goal);
        TextView distanceReached = findViewById(R.id.distance_reached);
        TextView distancePercentage = findViewById(R.id.distance_percentage);
        ProgressBar distanceProgress = findViewById(R.id.distanceProgress);

        Goal goal = DataManager.getGoals().get(getIntent().getIntExtra("position", 0));
        goalDescription.setText(goal.getDescription());
        sportName.setText(goal.getSport().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(goal.getCreationDate().getTime()));
        goalStatus.setText((goal.isAchieved()) ? getResources().getString(R.string.goal_achieved):
                (goal.hasDeadlineDate() && new Date().before(goal.getDeadlineDate().getTime()))?
                        getResources().getString(R.string.goal_not_achieved):
                        getResources().getString(R.string.goal_in_progress));
        goalStatus.setBackgroundColor((goal.isAchieved()) ? getResources().getColor(R.color.good):
                (goal.hasDeadlineDate() && new Date().before(goal.getDeadlineDate().getTime()))?
                        getResources().getColor(R.color.not_good):
                        getResources().getColor(R.color.in_progress));

        if (goal.hasDeadlineDate()) {
            deadlineDateContainer.setVisibility(View.VISIBLE);
            deadlineDate.setText(formatter.format(goal.getDeadlineDate().getTime()));
        }

        // time part
        if (goal.getAuthorizedGoal() == 1 || goal.getAuthorizedGoal() == 0) {
            timeGoalContainer.setVisibility(View.VISIBLE);
            Duration timeGoalDuration = goal.getDuration();
            int hours = (int)(timeGoalDuration.getSeconds()/3600);
            int minutes = (int)((timeGoalDuration.getSeconds()%3600)/60);
            int seconds = (int)((timeGoalDuration.getSeconds()%3600)%60);
            String durationStr = hours + "h:" + minutes + "m:" + seconds + "s";
            timeGoal.setText(durationStr);
            timeGoalDuration = Duration.ofMillis(goal.getTimeProgress());
            hours = (int)(timeGoalDuration.getSeconds()/3600);
            minutes = (int)((timeGoalDuration.getSeconds()%3600)/60);
            seconds = (int)((timeGoalDuration.getSeconds()%3600)%60);
            durationStr = hours + "h:" + minutes + "m:" + seconds + "s";
            timeReached.setText(durationStr);
            double percentage = (goal.getTimeProgress() == 0)?
                    0:((double) goal.getTimeProgress()/(double) goal.getDuration().toMillis())*100;
            if (percentage > 100 || goal.isAchieved()) percentage = 100;
            timePercentage.setText(new DecimalFormat("#.##").format(percentage));
            timeProgress.setProgress((int) percentage);
        }

        // distance part
        if (goal.getAuthorizedGoal() == 2 || goal.getAuthorizedGoal() == 0) {
            distanceGoalContainer.setVisibility(View.VISIBLE);
            distanceGoal.setText(String.valueOf(goal.getDistance()));
            String distanceReachedText =
                    new DecimalFormat("#.##").format(goal.getDistanceProgress())
                    + getResources().getString(R.string.distance_unit_km);
            distanceReached.setText(distanceReachedText);
            double percentage = (goal.getDistanceProgress() == 0)?
                    0:((double) goal.getDistanceProgress()/goal.getDistance())*100;
            if (percentage > 100 || goal.isAchieved()) percentage = 100;
            distancePercentage.setText(new DecimalFormat("#.##").format(percentage));
            distanceProgress.setProgress((int) percentage);
        }
    }

    public void backToMyGoals(View view) {
        onBackPressed();
    }
}
