package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GoalsListActivity extends AppCompatActivity {

    private Goal[] goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_list);
        Task task;
        if(!getIntent().getBooleanExtra("isCurrentTask", false)) {
            task = ((MyApp) getApplicationContext()).getTask(getIntent().getIntExtra("Task", 0));
        }else{
            task = ((MyApp) getApplicationContext()).getCurrentTask();
        }
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
        final GoalListAdapter mAdapter = new GoalListAdapter(goals, getIntent().getIntExtra("Task", 0),this);


        final Task proxyTask = task;

        if(((MyApp) getApplicationContext()).getCurrentTask() != null){
            findViewById(R.id.fulfillTask).setVisibility(View.GONE);
        }else{
            findViewById(R.id.fulfillTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyApp) getApplicationContext()).setCurrentTask(proxyTask);
                    view.setVisibility(View.GONE);
                    mAdapter.startFulfillingTask();
                }
            });
        }


        mRecyclerView.setAdapter(mAdapter);
    }
}
