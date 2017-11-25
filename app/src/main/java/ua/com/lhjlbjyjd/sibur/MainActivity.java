package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.tasks_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        Task[] testData = new Task[10];
        Goal[] goalsData = new Goal[6];
        for(int i = 0; i < 6; i++)
            goalsData[i] = new Goal("Goal "+i, false, i%2 == 0, i%3 == 0);
        for(int i = 0; i < 10; i++)
            testData[i] = new Task("Задание "+i+1, false, "0", goalsData);
        TaskListAdapter mAdapter = new TaskListAdapter(testData, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
