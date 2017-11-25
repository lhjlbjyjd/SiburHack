package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GoalsListActivity extends AppCompatActivity {

    private Goal[] goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_list);
        Task task = (Task)getIntent().getSerializableExtra("Task");
        setTitle(task.getName());
        goals = task.getGoals();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.goals_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example
        GoalListAdapter mAdapter = new GoalListAdapter(goals, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
