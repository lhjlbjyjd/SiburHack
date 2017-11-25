package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GoalsListActivity extends AppCompatActivity {

    private Goal[] goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_list);
        Task task = (Task)getIntent().getSerializableExtra("task");
        setTitle(task.getName());
        goals = task.getGoals();
    }
}
