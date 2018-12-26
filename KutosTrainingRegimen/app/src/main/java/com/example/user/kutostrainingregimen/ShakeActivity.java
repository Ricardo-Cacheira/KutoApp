package com.example.user.kutostrainingregimen;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

public class ShakeActivity extends AppCompatActivity {

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
                    shakeTxt.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
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
}
