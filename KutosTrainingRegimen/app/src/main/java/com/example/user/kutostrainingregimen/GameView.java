package com.example.user.kutostrainingregimen;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.PowerManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;
    private Enemy enemy;
    public boolean over = false;
    public boolean ready = false;
    public boolean motion;
    public int dodges = 0;

    public SharedPreferences sharedPref;

    public GameView(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        sharedPref = getContext().getSharedPreferences("myPrefs" , MODE_PRIVATE);

        thread = new MainThread(getHolder(),this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ArrayList<Bitmap> right = new ArrayList<Bitmap>();
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy0));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy5));
        ArrayList<Bitmap> left = new ArrayList<Bitmap>();
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy0));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy4));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy6));

        enemy = new Enemy(right,left,BitmapFactory.decodeResource(getResources(),R.drawable.ring));

        motion = sharedPref.getBoolean("Motion",true);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update()
    {
        if (enemy.over) {
            SaveScore();
            over = true;
        }
        if(ready)
            enemy.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        if (canvas != null)
        {
            if (ready) {
                enemy.draw(canvas);

                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);
                canvas.drawText("Lives: " + enemy.lives, canvas.getWidth() - 70, 25, paint);
            }else
            {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(200);
                int x = (canvas.getWidth() / 2);
                int y = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()))) ;
                canvas.drawText("Tap to Start",0,y, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (ready && !motion)
        {
            int x = (int)event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    enemy.Touch(x);
            }
        }else
        {
            ready = true;
        }
        return false;
    }


    public void GetTilt(float x, float y) {
        if (ready)
            enemy.Tilt(x,y);
    }

    public void SaveScore()
    {
        dodges = enemy.dodges;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Last" , dodges);
        if (dodges >= sharedPref.getInt("High",0))
            editor.putInt("High" , dodges);
        editor.commit();

    }
}
