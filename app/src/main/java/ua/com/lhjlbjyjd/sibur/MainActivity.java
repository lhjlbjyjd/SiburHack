package ua.com.lhjlbjyjd.sibur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (MyApp) getApplicationContext();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("Task", app.getTask(0));
                startActivity(intent);
            }
        });

        View currentTask = findViewById(R.id.current_task);
        if(app.getCurrentTask() == null){
            currentTask.setVisibility(View.GONE);
        }else{
            Log.d("CurrentTask", app.getCurrentTask().getName());
            ((TextView)currentTask.findViewById(R.id.task_name_text)).setText(app.getCurrentTask().getName());
            currentTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, GoalsListActivity.class);
                    intent.putExtra("isCurrentTask", true);
                    startActivity(intent);
                }
            });
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.tasks_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        TaskListAdapter mAdapter = new TaskListAdapter(app.getTasks(),app.getCurrentTask() != null, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
