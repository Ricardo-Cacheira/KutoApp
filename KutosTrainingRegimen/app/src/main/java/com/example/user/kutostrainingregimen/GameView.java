package com.example.user.kutostrainingregimen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Enemy enemy;


    public GameView(Context context)
    {
        super(context);

        getHolder().addCallback(this);

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
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy4));
        ArrayList<Bitmap> left = new ArrayList<Bitmap>();
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy0));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2));

        enemy = new Enemy(right,left,BitmapFactory.decodeResource(getResources(),R.drawable.ring));

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
        enemy.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if (canvas != null)
        {

            enemy.draw(canvas);
        }
    }
}
