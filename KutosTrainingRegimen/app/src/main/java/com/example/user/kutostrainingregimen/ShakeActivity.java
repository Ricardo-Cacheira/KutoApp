package com.example.user.kutostrainingregimen;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class ShakeActivity extends AppCompatActivity {

    JSONObject data = new JSONObject();
    int gold, shards, lvl;
    private ShakeListener mShaker;
    private Button backButton;
    private boolean hasShaked;
    private TextView shakeTxt;
    private ConstraintLayout layout;

    public static int shakesLeft = OverviewFragment.nOfShakes;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_activity);
        shakesLeft = OverviewFragment.nOfShakes;
        backButton = (Button) findViewById(R.id.goBackBtn);
        layout = (ConstraintLayout) findViewById(R.id.popupLayout);
        shakeTxt = (TextView) findViewById(R.id.shaketxt);

        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        final String ip = intent.getStringExtra("ip");
        final String name = intent.getStringExtra("name");
        final String pass = intent.getStringExtra("pass");
        gold = intent.getIntExtra("gold",0);
        shards = intent.getIntExtra("shards",0);
        if(getIntent().hasExtra("json")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                shakeTxt.setVisibility(View.VISIBLE);
            }
        });

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake() {
                if (shakesLeft > 0) {
                    vibe.vibrate(700);
                    hasShaked = true;
                    shakesLeft--;
                    getReward();
                    new PostDataTask().execute("http://"+ip+":8080/api/saveData?username="+name+"&password="+pass+ "&data=" + data);
                    shakeTxt.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getReward() {
        Random r = new Random();
        int range = r.nextInt((lvl+2) - (lvl-10)) + (lvl-10);
        if (range <= 0)
            range = 1;

        gold += 150 * range;
        shards += 5* range;
        try {
            data.put("gold", gold);
            data.put("shards", shards);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        mShaker.resume();
        super.onResume();
    }
    @Override
    public void onPause()
    {
        mShaker.pause();
        super.onPause();
    }

    class PostDataTask extends AsyncTask<String, Void, String>
    {
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            try
            {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000); // milliseconds
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json"); // set header
                urlConnection.connect();

                // Read data response from the server

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    result.append(line).append("\n");

                }

            }
            catch (IOException ex)
            {
                return  ex.getMessage();

            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int aJsonInt = 0;
            int coins = 0;

            try
            {
                JSONObject jsonObject = new JSONObject(result);
                aJsonInt = jsonObject.getInt("status");

                JSONObject accountObj = jsonObject.getJSONObject("account");
                JSONObject dataObj = accountObj.getJSONObject("data");

            }
            catch(Exception e){

            }
        }

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {
                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("gold", "420");

                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }

            return result.toString();
        }
    }
}
