package com.example.city_weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText kotaEditText;
    TextView cuacaTextView;
    TextView deskripsiTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {
        //parameter 1 ialah apa yang akan dilakukan asynctask
        //parameter 2 ialah
        //parameter 3 ialah kembalian dari asynctask


        @Override
        protected String doInBackground(String... params) {
            //protected String ini dapat di akses di package ini
            String result = "";
            URL url ;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }






        }

        protected  void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.i("result", result);
                String weatherInfo =  jsonObject.getString("weather");
                Log.i("weather", weatherInfo);
                JSONArray arr =  new JSONArray(weatherInfo);
                for(int i = 0; i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    Log.i("main", main);
                    Log.i("descripion", description);
                    cuacaTextView.setText(main);
                    deskripsiTextView.setText(description);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kotaEditText = (EditText) findViewById(R.id.kotaEditText);
        cuacaTextView = (TextView) findViewById(R.id.cuacaKotaTextView);
        deskripsiTextView = (TextView) findViewById(R.id.deskripsiTextView);
    }

    public void cariCuaca(View view){
        String cuacaKota = kotaEditText.getText().toString();
        DownloadTask task = new DownloadTask();
        //api key
        //545ca74824f667a6e84b845f47938c27
        String link = "http://api.openweathermap.org/data/2.5/weather?q="+cuacaKota+"&APPID=545ca74824f667a6e84b845f47938c27";
        Log.i("kota", cuacaKota);
        Log.i("link", link);
        task.execute(link);
    }
}
