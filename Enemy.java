package com.example.surfacegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private int maxSpeed;
    private int minSpeed;

    private Rect detectCollision;


    public Enemy(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        maxX = screenX;
        maxY = screenY - bitmap.getHeight();;
        minX = 0;
        minY = 100;
        Random generator = new Random();
        speed = generator.nextInt(5);
        maxSpeed = 5;
        x = maxX;
        y = generator.nextInt(maxY);

        detectCollision =  new Rect(x, y, x +bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(int playerSpeed) {

        x -= playerSpeed;
        x -= speed;

        if (x < 0) {

            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            if(maxSpeed < 20) maxSpeed += 5;
            speed = generator.nextInt(maxSpeed);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getDetectCollision() {
        detectCollision =  new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        return detectCollision;
    }
}
