package com.example.user.kutostrainingregimen;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Enemy{

    private enum State
    {
        Idle,
        Charging,
        Jab,
    }


    public String TAG = "click";

    private ArrayList<Bitmap> left;
    private ArrayList<Bitmap> right;
    private Bitmap image;
    private Bitmap ring;

    public int lives = 3;

//    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public int frameCounter = 0;

    public String result="";
    public boolean dodge = false;
    public boolean clicked = false;
    public boolean over = false;

    public int chargeFrames = 30;

    public int state = 0;
    public int dodges = 0;
    public int side;
    public int clickedSide;


    public Enemy(ArrayList<Bitmap> right, ArrayList<Bitmap> left,Bitmap ring) {
        this.left = left;
        this.right = right;
        this.image = left.get(0);
        this.ring = ring;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void update() {

        frameCounter++;
        State whichState = State.values()[state];
        switch (whichState) {
            case Idle:
                result = "";
                Random r = new Random();
                if (frameCounter > 40)
                {
                    //r.nextInt((max - min) + 1) + min;
                    side = r.nextInt((1 - 0) + 1) + 0;
                    Log.d(TAG,side+"side");
                    state = 1;
                    if (side == 1) //0 left / 1 right
                    {
                        image = left.get(1);
                    }else{
                        image = right.get(1);
                    }
                    frameCounter = 0;
                }
                break;
            case Charging:
                if (clicked)
                    dodge = (clickedSide==side);
                else
                    dodge = false;

                //Log.d("click", (clickedSide==side)+" cs:"+clickedSide+" s:"+side);

                if (frameCounter > chargeFrames)
                {
                    if (chargeFrames >= 13)
                        chargeFrames--;

                    state = 2;
                    int next = (dodge) ? 3 : 2;
                    if (side == 1) //0 left / 1 right
                    {
                        image = left.get(next);
                    }else{
                        image = right.get(next);
                    }
                    frameCounter = 0;
                    clicked = false;
                }
                break;
            case Jab:
                result= (dodge) ? "DODGE !" : "HIT !";
                if (frameCounter > 20)
                {
                    if (!dodge) lives--;
                    else dodges++;

                    state = 0;
                    if (side == 1) //0 left / 1 right
                    {
                        image = left.get(0);
                    }else{
                        image = right.get(0);
                    }
                    frameCounter = 0;
                }
                if (lives <= 0) over = true;
                break;
        }
    }

    public void draw(Canvas canvas)
    {
        float h = (float) (canvas.getHeight()/2) - image.getHeight()/2;
        float h2 = (float) canvas.getHeight();
        float w = (float) (canvas.getWidth()/2) - image.getWidth()/2;
        float w2 = (float) canvas.getWidth();

        Rect src = new Rect(0,0,ring.getWidth(),ring.getHeight());
        Rect dst = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

        canvas.drawBitmap(ring, src, dst,null);
        canvas.drawBitmap(image,w,h,null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(150);
        int x;
        if(result == "DODGE !")
            x=250;
        else
            x=400;
        canvas.drawText(result, x, 250, paint);
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    public void Touch(int x)
    {
        if (!clicked) {
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            State whichState = State.values()[state];
            if (whichState == State.Charging) {
                clicked = true;
                if (x >= screenWidth / 2) //right screen
                {
                    clickedSide = 1;
                } else { //left screen
                    clickedSide = 0;
                }
            }
            Log.d(TAG,clickedSide+"clickedside");
        }
    }

    public void Tilt(float x,float y)
    {
        if (Math.abs(x) > Math.abs(y)) {
            State whichState = State.values()[state];
            if (whichState == State.Charging) {
                clicked = true;
                if (x < 0) //right screen
                {
                    clickedSide = 1;
                } else if (x > 0) { //left screen
                    clickedSide = 0;
                }
            }
        }
        /*
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                Log.d(TAG, "You tilt the device right");
            }
            if (x > 0) {
                Log.d(TAG, "You tilt the device left");
            }
        } else {
            if (y < 0) {
                Log.d(TAG, "You tilt the device up");
            }
            if (y > 0) {
                Log.d(TAG, "You tilt the device down");
            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            Log.d(TAG, "Not tilt device");
        }
        */
    }


}
