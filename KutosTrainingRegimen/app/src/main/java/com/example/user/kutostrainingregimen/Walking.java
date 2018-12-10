package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Walking extends RootActivity {

    private GestureDetectorCompat gestureObject;
    private Button back;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_walking);
//
//        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
//
//        back = (Button) findViewById(R.id.back);
//
//        back.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (Walking.this, MainActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        this.gestureObject.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
//    //creating the gesture
//    class LearnGesture extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
//        {
//            try {
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                    return false;
//                // right to left swipe
//                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
//                {
//                    Intent intent = new Intent(Walking.this, MainActivity.class);
//                    startActivity(intent);
//                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // nothing
//            }
//            return false;
//        }
//    }
}
