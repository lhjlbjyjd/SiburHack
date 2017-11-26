package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GoalDetailsActivity extends AppCompatActivity {

    String pattern = "dd/MM/yyyy HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);

        Goal currentGoal = ((MyApp)getApplicationContext()).getGoalFromTask(((MyApp)getApplicationContext()).getTask(getIntent().getIntExtra("taskId", 0)),
                getIntent().getIntExtra("goalId",  0));

        ((ImageView)findViewById(R.id.goal_image)).setImageBitmap(currentGoal.getImage());
        ((TextView)findViewById(R.id.goal_name)).setText(currentGoal.getDescription());
        setTitle(currentGoal.getDescription());
        DateFormat df = new SimpleDateFormat(pattern);
        ((TextView)findViewById(R.id.goal_duration)).setText(df.format(currentGoal.getBeginDate()) + " - " + df.format(currentGoal.getEndDate()));
    }
}
