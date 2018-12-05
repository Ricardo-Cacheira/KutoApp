package com.example.user.kutostrainingregimen;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {

    private enum State
    {
        Idle,
        Charging,
        Jab,
    }

    private ArrayList<Bitmap> left;
    private ArrayList<Bitmap> right;
    private Bitmap image;
    private Bitmap ring;
//    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public int frameCounter = 0;

    public int state = 0;
    public int side;


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
                Random r = new Random();
                side = r.nextInt(1 - 0 + 1) + 0;
                if (frameCounter > 40)
                {
                    state = 1;
                    if (side == 0) //0 left / 1 right
                    {
                        image = left.get(1);
                    }else{
                        image = right.get(1);
                    }
                    frameCounter = 0;
                }
                break;
            case Charging:
                if (frameCounter > 30)
                {
                    state = 2;
                    if (side == 0) //0 left / 1 right
                    {
                        image = left.get(2);
                    }else{
                        image = right.get(2);
                    }
                    frameCounter = 0;
                }
                break;
            case Jab:
                if (frameCounter > 20)
                {
                    state = 0;
                    if (side == 0) //0 left / 1 right
                    {
                        image = left.get(0);
                    }else{
                        image = right.get(0);
                    }
                    frameCounter = 0;
                }
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
    }
}
