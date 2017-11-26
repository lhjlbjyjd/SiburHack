package ua.com.lhjlbjyjd.sibur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoaderActivity extends AppCompatActivity {

    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        app = (MyApp)getApplicationContext();

        new InfoUpdate().execute();
    }

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
                        goals.add(new Goal(description,!endDate.equals("0"), photoRequired, emailRequired, photoUrl, LoaderActivity.this));
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
            Intent intent = new Intent(LoaderActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
