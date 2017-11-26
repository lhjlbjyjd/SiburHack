package ua.com.lhjlbjyjd.sibur;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyApp app;
    TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (MyApp) getApplicationContext();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("Task", 0);
                startActivity(intent);
            }
        });

        View currentTask = findViewById(R.id.current_task);
        if(app.getCurrentTask() == null){
            currentTask.setVisibility(View.GONE);
        }else{
            Log.d("CurrentTask", app.getCurrentTask().getName());
            ((TextView)currentTask.findViewById(R.id.task_name_text)).setText(app.getCurrentTask().getName());
            ((TextView)currentTask.findViewById(R.id.task_status)).setText("Выполняется");
            ((TextView)currentTask.findViewById(R.id.task_status)).setTextColor(Color.parseColor("#0000FF"));
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
        mAdapter = new TaskListAdapter(app.getTasks(),app.getCurrentTask() != null, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*@Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }*/

    class InfoUpdate extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        List<Task> tasks = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://siburapi.zzz.com.ua/php/getTasks.php");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                urlConnection.disconnect();

                resultJson = buffer.toString();

                JSONArray array = new JSONArray(resultJson);
                for(int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("id");
                    String name = object.getString("Name");
                    boolean state = !object.getString("StartDate").equals("0");
                    String executorId = object.getString("workerId");
                    List<Goal> goals = new ArrayList<>();
                    JSONArray goalsArray = new JSONArray(object.getString("Goals"));
                    for(int j = 0; j < goalsArray.length(); j++){
                        JSONObject goalObject = goalsArray.getJSONObject(j);
                        String description = goalObject.getString("Description");
                        String startDate = goalObject.getString("StartDate");
                        String endDate = goalObject.getString("EndDate");
                        boolean completed = goalObject.getBoolean("completed");
                        boolean photoRequired = goalObject.getBoolean("photoRequired");
                        boolean emailRequired = goalObject.getBoolean("emailRequired");
                        String photoUrl = goalObject.getString("photo");
                        goals.add(new Goal(description,!endDate.equals("0"), photoRequired, emailRequired, photoUrl, MainActivity.this));
                    }
                    tasks.add(new Task(id, name, state, executorId, goals.toArray(new Goal[goals.size()])));
                }
                app.setTasks(tasks.toArray(new Task[tasks.size()]));
                Log.d("ListSize", String.valueOf(tasks.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
        }
    }
}
