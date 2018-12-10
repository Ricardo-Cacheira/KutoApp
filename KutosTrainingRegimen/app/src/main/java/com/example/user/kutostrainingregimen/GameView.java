package com.example.user.kutostrainingregimen;

import android.content.Context;

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

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;
    private Enemy enemy;
    public boolean over = false;

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
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy1));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy3));
        right.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy5));
        ArrayList<Bitmap> left = new ArrayList<Bitmap>();
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy0));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy2));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy4));
        left.add(BitmapFactory.decodeResource(getResources(),R.drawable.enemy6));

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

        if (enemy.over)
            over = true;

        if (canvas != null)
        {
            enemy.draw(canvas);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            canvas.drawText("Lives: "+ enemy.lives, canvas.getWidth() - 70, 25, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                enemy.Touch(x);
        }
        return false;
    }


    public void GetTilt(float x, float y) {
        enemy.Tilt(x,y);
    }
}
