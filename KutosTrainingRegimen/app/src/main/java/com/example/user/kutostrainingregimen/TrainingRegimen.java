package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

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
import java.util.ArrayList;
import java.util.List;


public class TrainingRegimen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    OverviewFragment overviewFragment;
    WalkingFragment walkingFragment;
    MinigameFragment minigameFragment;
    TipsFragment tipsFragment;
    MenuItem prevMenuItem;

    String ip;
    String name;
    String pass;

    JSONObject data = new JSONObject();

    public int gold=1, shards=0, vitality=0, lvl=0;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getShards() {
        return shards;
    }

    public void setShards(int shards) {
        this.shards = shards;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_regimen);

        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        name = intent.getStringExtra("name");
        pass = intent.getStringExtra("pass");

        new GetDataTask().execute("http://"+ip+":8080/api/getData?username="+name+"&password="+pass);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OverviewFragment()).commit();

        bottomNavigationView  = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_overview:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_walking:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_game:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_tips:
                        viewPager.setCurrentItem(3);
                        break;

                    default: return false;
                }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page",""+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        overviewFragment=new OverviewFragment();
        walkingFragment=new WalkingFragment();
        minigameFragment=new MinigameFragment();
        tipsFragment=new TipsFragment();

        adapter.addFragment(overviewFragment);
        adapter.addFragment(walkingFragment);
        adapter.addFragment(minigameFragment);
        adapter.addFragment(tipsFragment);
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetDataTask().execute("http://"+ip+":8080/api/getData?username="+name+"&password="+pass);
    }

    class GetDataTask extends AsyncTask<String, Void, String>
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
//                JSONObject accountObj = jsonObject.getJSONObject("player");
                JSONObject dataObj = accountObj.getJSONObject("data");

                data = dataObj;

                CalculateLevel(dataObj.getInt("xp"));
                gold = dataObj.getInt("gold");
                shards = dataObj.getInt("shards");
                vitality = dataObj.getInt("vitality");

                overviewFragment.UpdateValues();
                Log.d("mongo","gold "+gold);
                Log.d("mongo","shards "+shards);
                Log.d("mongo","vit "+vitality);
                Log.d("mongo","lvl "+lvl);

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

    public void CalculateLevel(int xp)
    {
        int currReqXp = 0;
        int tempXp = 0;
        int lastReqXp = 0;

        for (int i = 1; i < 999; i++)
        {
            currReqXp = 120 + (i * i * 10);
            tempXp += currReqXp;

            if (tempXp >= xp)
            {
                lvl = i;
                if (i == 1) lastReqXp = 0;
                break;
            }
            else
                lastReqXp += currReqXp;
        }
    }
}
