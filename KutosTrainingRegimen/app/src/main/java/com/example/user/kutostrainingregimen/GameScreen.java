package com.example.user.kutostrainingregimen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameScreen extends Activity implements SensorEventListener {

    public SharedPreferences sharedPref;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public GameView gameView;

    Button homeButton;

    public boolean motion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sharedPref = getSharedPreferences("myPrefs" , MODE_PRIVATE);

        motion = sharedPref.getBoolean("Motion",true);

        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*
            values[0]: acceleration in axis X
            values[1]: acceleration in axis Y
            values[2]: acceleration in axis Z
         */

        if (gameView.ready && motion)
            gameView.GetTilt(event.values[0], event.values[1]);

        if (gameView.over)
        {
            this.onBackPressed();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register listener
        // Frequency SENSOR_DELAY_GAME, second biggest. Try also SENSOR_DELAY_FASTEST, SENSOR_DELAY_NORMAL
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // delete listener
        mSensorManager.unregisterListener(this);
    }
}
