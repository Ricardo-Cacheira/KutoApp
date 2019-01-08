package com.example.user.kutostrainingregimen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class OverviewFragment extends Fragment {

    public TrainingRegimen trainingRegimen;
    private static final long START_TIME = 20000;
    private static final String TIME_LEFT_LONG = "timeLeftMillis";
    private static final String TIME_END_LONG = "timeEndMillis";
    private static final String TIME_RUNNING_BOOL = "timerRunning";
    private static final String SHAKE_COUNTER_INT = "shakeCounter";

    private TextView timer, nextDailyTxt, nOfShakesTxt;

    private TextView gold, shards, lvl, vitality;
    private Button acceptDaily;
    private long timeLeftMillis = START_TIME;
    private boolean timerRunning;
    private long timeEnd;
    private CountDownTimer mCountdownTimer;
    private RecyclerView inventory;
    private RecyclerView.Adapter adapter;

    public NotificationManager notificationManager;
    public NotificationCompat.Builder mBuilder;
    public SensorManager sensorManager;

    private ImageButton shakeBtn;

    public static int nOfShakes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Daily mission";
            String description = "You have a new daily mission available!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getContext(), "1")
                .setSmallIcon(R.drawable.walkingdownn)
                .setContentTitle("Daily mission")
                .setContentText("You have a new daily mission available!")
                .setVisibility(VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have a new daily mission available! Go accept it!!"))
                .setPriority(
                        NotificationCompat.PRIORITY_MAX)
//                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_overview, container, false);

        trainingRegimen = (TrainingRegimen) this.getActivity();

        ArrayList<Item> inventory = initInventory();
        timer = (TextView) rootView.findViewById(R.id.timer);
        nextDailyTxt = (TextView) rootView.findViewById(R.id.dailyTxt);
        acceptDaily = (Button) rootView.findViewById(R.id.Accept);


        gold = (TextView) rootView.findViewById(R.id.gold);
        shards = (TextView) rootView.findViewById(R.id.shards);
        vitality = (TextView) rootView.findViewById(R.id.vitality);
        lvl = (TextView) rootView.findViewById(R.id.level);


//        this.inventory = (RecyclerView) rootView.findViewById(R.id.inventory);
//        RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(getContext());
//        this.inventory.setLayoutManager(mLayoutManager);
//
//        adapter = new ItemAdapter(inventory);
//        this.inventory.setAdapter(adapter);

        shakeBtn = (ImageButton) rootView.findViewById(R.id.shakebtn);
        nOfShakesTxt = (TextView) rootView.findViewById(R.id.nShakes);

        nOfShakesTxt.setText(""+ nOfShakes);

        acceptDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLeftMillis = START_TIME;
                timerRunning = true;
                nextDailyTxt.setVisibility(View.VISIBLE);
                StartTimer();
            }
        });

        shakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nOfShakes > 0) {
                    Intent intent = new Intent(getActivity(), ShakeActivity.class);
                    intent.putExtra("ip", ((TrainingRegimen)getActivity()).ip);
                    intent.putExtra("name",((TrainingRegimen)getActivity()).name);
                    intent.putExtra("pass",((TrainingRegimen)getActivity()).pass);
                    intent.putExtra("gold",((TrainingRegimen)getActivity()).getGold());
                    intent.putExtra("shards",((TrainingRegimen)getActivity()).getShards());
                    intent.putExtra("json", ((TrainingRegimen)getActivity()).data.toString());
                    getActivity().startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void StartTimer()
    {
        timeEnd = System.currentTimeMillis() + timeLeftMillis;

        mCountdownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                UpdateCountdownText();
            }

            @Override
            public void onFinish() {
                acceptDaily.setVisibility(View.VISIBLE);
                nextDailyTxt.setVisibility(View.INVISIBLE);
                timerRunning = false;
                timer.setText("Accept daily");
                nOfShakes++;
                nOfShakesTxt.setText("" + nOfShakes);

                notificationManager.notify(001, mBuilder.build());
            }
        }.start();

        acceptDaily.setVisibility(View.INVISIBLE);
    }

    public void UpdateShakes()
    {
        nOfShakes++;
        nOfShakesTxt.setText("" + nOfShakes);
    }

    private void UpdateCountdownText()
    {
        int minutes = (int) timeLeftMillis / 1000 / 60;
        int seconds = (int) timeLeftMillis / 1000 % 60;
        int hours = (int) timeLeftMillis / 1000 / 60 / 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02dh%02dm%02ds", hours, minutes, seconds);
        timer.setText(timeLeftFormatted);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

        timeLeftMillis = prefs.getLong(TIME_LEFT_LONG, START_TIME);
        timerRunning = prefs.getBoolean(TIME_RUNNING_BOOL, false);
        nOfShakes = prefs.getInt(SHAKE_COUNTER_INT, 0);

        nOfShakes = (ShakeActivity.shakesLeft - nOfShakes) + nOfShakes;
        nOfShakes += WalkingFragment.nShakes;
        WalkingFragment.nShakes = 0;

        UpdateValues();

        if(timerRunning)
        {
            UpdateCountdownText();
            timeEnd = prefs.getLong(TIME_END_LONG, 0);
            timeLeftMillis = timeEnd - System.currentTimeMillis();

            if(timeLeftMillis < 0) {
                timeLeftMillis = 0;
                timer.setText("Accept daily");
                nOfShakes++;

                timerRunning = false;
            } else
            {
                StartTimer();
                nextDailyTxt.setVisibility(View.VISIBLE);
            }
        }
        nOfShakesTxt.setText("" + nOfShakes);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(TIME_LEFT_LONG, timeLeftMillis);
        editor.putBoolean(TIME_RUNNING_BOOL, timerRunning);
        editor.putLong(TIME_END_LONG, timeEnd);
        editor.putInt(SHAKE_COUNTER_INT, nOfShakes);

        editor.apply();
    }

    private ArrayList<Item> initInventory()
    {
        ArrayList<Item> list = new ArrayList<Item>();

        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));

        return list;
    }

    public void UpdateValues()
    {
        gold.setText("Gold: "+ ((TrainingRegimen)getActivity()).getGold());
        shards.setText("Shards: "+ ((TrainingRegimen)getActivity()).getShards());
        vitality.setText("Vitality: " +((TrainingRegimen)getActivity()).getVitality());
        lvl.setText("Level : " + ((TrainingRegimen)getActivity()).getLvl());
    }

}
