package ua.com.lhjlbjyjd.sibur;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ua.com.lhjlbjyjd.sibur.GoalListAdapter.REQUEST_IMAGE_CAPTURE;

public class GoalsListActivity extends AppCompatActivity {
    private String android_id;
    private Goal[] goals;
    int lastPhotoIndex = -1;
    GoalListAdapter mAdapter;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_list);

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

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
        mAdapter = new GoalListAdapter(goals, getIntent().getIntExtra("Task", 0), task, this);

        final GoalListAdapter proxyAdapter = mAdapter;
        final Task proxyTask = task;

        if(((MyApp) getApplicationContext()).getCurrentTask() != null || task.isFulfilled() || (!task.getExecutorID().equals(android_id) && !task.getExecutorID().equals("0"))){
            findViewById(R.id.fulfillTask).setVisibility(View.GONE);
        }else{
            findViewById(R.id.fulfillTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                new OwnTask().execute(proxyTask.getId());
                }
            });
        }


        mRecyclerView.setAdapter(mAdapter);
    }

    public void takePhoto(int i){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            lastPhotoIndex = i;
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            goals[lastPhotoIndex].setImage(imageBitmap);
            goals[lastPhotoIndex].setPhotoRequired(false);
            mAdapter.notifyDataSetChanged();
            lastPhotoIndex = -1;
        }
    }

    class OwnTask extends AsyncTask<Integer, Void, Boolean> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        List<Task> tasks = new ArrayList<>();
        boolean responce = false;

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                URL url = new URL("http://siburapi.zzz.com.ua/php/ownTask.php?id=" + params[0] + "&worker=" + android_id);

                Log.d("URL", String.valueOf(url));

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

                JSONObject object = new JSONObject(resultJson);
                responce = object.getInt("result") != 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responce;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                ((MyApp) getApplicationContext()).setCurrentTask(task);
                findViewById(R.id.fulfillTask).setVisibility(View.GONE);
                mAdapter.startFulfillingTask();
                goals[0].setBeginDate(System.currentTimeMillis());
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalsListActivity.this, R.style.AlertDialogStyle);
                builder.setTitle("Ошибка!");
                builder.setMessage("Во время выполнения операции произошла неизвестная ошибка :(");
                builder.setPositiveButton("Ок", null);
                builder.show();
            }
        }
    }
}
