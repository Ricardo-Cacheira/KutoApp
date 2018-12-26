package com.example.user.kutostrainingregimen;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class WalkingFragment extends Fragment implements SensorEventListener, StepListener {

    private GestureDetectorCompat gestureObject;
    private Button back;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public static TextView TvSteps, TvPercent, TvGoal, TvTravel;
    public Button BtnStart;
    private Button BtnStop;
    public StepDetector simpleStepDetector;
    public SensorManager sensorManager;
    public Sensor accel;
    private static final String TEXT_NUM_STEPS = " steps";
    public static int numSteps;
    float currPercentage;
    String formattedValue;

    public Button longBtn, mediumBtn, shortBtn;
    public static int goalSteps;

    public NotificationManager notificationManager;
    public NotificationCompat.Builder mBuilder;

    public static int nShakes=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Walking completed";
            String description = "some description";
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
                .setContentTitle("Walking completed")
                .setContentText("You just completed your walking challenge!")
                .setVisibility(VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You just completed your walking challenge! Congratulations!!"))
                .setPriority(
                        NotificationCompat.PRIORITY_MAX)
//                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_walking, container, false);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        nShakes = 0;

        TvSteps = (TextView) rootView.findViewById(R.id.nSteps);
        TvPercent = (TextView) rootView.findViewById(R.id.percentSteps);
        TvGoal = (TextView) rootView.findViewById(R.id.goalTxt);
        TvTravel = (TextView) rootView.findViewById(R.id.travelTxt);
        BtnStart = (Button) rootView.findViewById(R.id.startBtn);
        BtnStop = (Button) rootView.findViewById(R.id.stopBtn);

        TvSteps.setText(numSteps + TEXT_NUM_STEPS);
        TvGoal.setText("Goal: " + goalSteps + " steps");
        currPercentage = (float) numSteps/goalSteps * 100;
        formattedValue = String.format("%.1f", currPercentage);

        if (Float.isNaN(currPercentage))
            TvPercent.setText("You need to set a goal!");
        else
        TvPercent.setText("You've completed " + formattedValue + "% of your goal!");

        final View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_popup_class, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        longBtn = (Button) popupView.findViewById(R.id.longBtn);
        mediumBtn = (Button) popupView.findViewById(R.id.mediumBtn);
        shortBtn = (Button) popupView.findViewById(R.id.shortBtn);

        longBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetChallenge(10000);
                TvTravel.setText("Long Travel");
                popupWindow.dismiss();
            }
        });

        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetChallenge(2500);
                TvTravel.setText("Medium Travel");
                popupWindow.dismiss();
            }
        });

        shortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetChallenge(10);
                TvTravel.setText("Short Travel");
                popupWindow.dismiss();

            }
        });

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TvSteps.setText(numSteps + TEXT_NUM_STEPS);
                if (Float.isNaN(currPercentage))
                    TvPercent.setText("You need to set a goal!");
                else
                    TvPercent.setText("You've completed " + formattedValue + "% of your goal!");

                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                numSteps = 0;
                goalSteps = 0;
                TvSteps.setText(numSteps + TEXT_NUM_STEPS);
                TvPercent.setText("You need to set a goal!");
                TvTravel.setText("Choose a travel distance");
                sensorManager.unregisterListener(WalkingFragment.this);
            }
        });

        return rootView;
    }

    private void SetChallenge(int steps)
    {
        goalSteps = steps;
        numSteps = 0;
        TvGoal.setText("Goal: " + goalSteps + " steps");
        if (Float.isNaN(currPercentage))
            TvPercent.setText("You've completed 0% of your goal!");
        else
            TvPercent.setText("You've completed " + formattedValue + "% of your goal!");
        sensorManager.unregisterListener(WalkingFragment.this);
        sensorManager.registerListener(WalkingFragment.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(numSteps + TEXT_NUM_STEPS);
        float currPercentage = (float) numSteps/goalSteps * 100;
        String formattedValue = String.format("%.1f", currPercentage);
        TvPercent.setText("You've completed " + formattedValue + "% of your goal!");

        if (currPercentage >= 100) {
            TvSteps.setText(goalSteps + TEXT_NUM_STEPS);
            TvPercent.setText("You've completed your goal!");
            sensorManager.unregisterListener(WalkingFragment.this);
            nShakes++;
            notificationManager.notify(001, mBuilder.build());
        }
    }
}
