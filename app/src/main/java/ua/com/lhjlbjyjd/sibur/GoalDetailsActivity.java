package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GoalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        Goal currentGoal = ((MyApp)getApplicationContext()).getGoalFromTask(((MyApp)getApplicationContext()).getTask(getIntent().getIntExtra("taskId", 0)),
                getIntent().getIntExtra("goalId",  0));
    }
}
