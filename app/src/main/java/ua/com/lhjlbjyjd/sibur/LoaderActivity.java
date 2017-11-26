package ua.com.lhjlbjyjd.sibur;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
    }

    MyApp app = (MyApp)getApplicationContext();

    class InfoUpdate extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        List<Task> tasks = new ArrayList<>();

        @Override
        protected String doInBackground(Void... params) {
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

                JSONObject jsonObject = new JSONObject(resultJson);
                int error = jsonObject.getInt("error");
                if(error == 0){
                    JSONArray array = jsonObject.getJSONArray("news");
                    for(int i = 0; i < array.length(); i++){
                        tasks.add(new Task());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
        }
    }
}
