package com.example.mohammad.fighterjet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by mohammad on 24/12/16.
 */

public class PlayerMethod {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 0;

    private boolean boosting;
    private final int GRAVITY = -10;
    private int maxY;
    private int minY;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    private Rect collison;

    public PlayerMethod(Context context,int screenX,int screenY){
        x = 75;
        y = 50;
        speed = 1;

        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.player);

        maxY = screenY - bitmap.getHeight();
        minY = 0;

        boosting = false;

        collison = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }
    public void setBoosting() {
        boosting = true;
    }
    public void stopBoosting() {
        boosting = false;
    }
    public void update(){
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        //ship will go down to hunt pussy
        y -= speed + GRAVITY;
        //without pussy, ship will not blow out
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }

        collison.left = x;
        collison.top = y;
        collison.right = x + bitmap.getWidth();
        collison.bottom = y + bitmap.getHeight();
    }
    public Rect getDetectCollision() {
        return collison;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public  int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSpeed(){
        return speed;
    }
}
