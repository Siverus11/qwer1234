package com.example.surfacegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Friend friend;
    private Enemy enemy;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    Bitmap boom;

    private ArrayList<Star> stars = new ArrayList<Star>();

    int screenX;
    int countMisses;

    boolean flag ;
    boolean isEnemyDestroyed;
    boolean isEnemyDestroyedSound;


    private boolean isGameOver;


    int score;


    int highScore[] = new int[4];


    SharedPreferences sharedPreferences;

    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;

    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        friend = new Friend(context,screenX,screenY);
        enemy = new Enemy(context,screenX,screenY);

        isEnemyDestroyed = false;
        isEnemyDestroyedSound = false;
        countMisses = 0;

        boom = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        this.screenX = screenX;
        isGameOver = false;


        score = 0;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", MODE_PRIVATE);


        //highScore[0] = sharedPreferences.getInt("score1", 0);
        //highScore[1] = sharedPreferences.getInt("score2", 0);
        //highScore[2] = sharedPreferences.getInt("score3", 0);
        //highScore[3] = sharedPreferences.getInt("score4", 0);
        this.context = context;


        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);


        gameOnsound.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;

        }

        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                stopMusic();
                if(!sharedPreferences.contains("score1")){
                    sharedPreferences.edit().putString("score1", score + "").apply();
                } else if(!sharedPreferences.contains("score2")){
                    sharedPreferences.edit().putString("score2", score + "").apply();
                } else if(!sharedPreferences.contains("score3")){
                    sharedPreferences.edit().putString("score3", score + "").apply();
                } else if(!sharedPreferences.contains("score4")){
                    sharedPreferences.edit().putString("score4", score + "").apply();
                } else {
                    sharedPreferences.edit().putString("score4", sharedPreferences.getString("score3", "0")).apply();
                    sharedPreferences.edit().putString("score3", sharedPreferences.getString("score2", "0")).apply();
                    sharedPreferences.edit().putString("score2", sharedPreferences.getString("score1", "0")).apply();
                    sharedPreferences.edit().putString("score1", score + "").apply();
                }

                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
            if(countMisses > 2){
                isGameOver = true;
            }
            if(isGameOver) try {
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);


            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }


            paint.setTextSize(30);
            canvas.drawText("Очки: "+score,100,50,paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint);

            canvas.drawBitmap(
                    enemy.getBitmap(),
                    enemy.getX(),
                    enemy.getY(),
                    paint);

            if(isEnemyDestroyed){
                canvas.drawBitmap(boom,
                        enemy.getX(),
                        enemy.getY(),
                        paint);
            }

            if(countMisses > 2){
                isGameOver = true;
            }


            if(isGameOver){
                killedEnemysound.start();
                canvas.drawBitmap(boom, player.getX(),player.getY(),paint);
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Конец игры",canvas.getWidth()/2,yPos,paint);
                stopMusic();
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    public void friendCollision(){
        if(player.getDetectCollision().contains(friend.getDetectCollision().right, friend.getDetectCollision().top)
                || player.getDetectCollision().contains(friend.getDetectCollision().left, friend.getDetectCollision().top)
                || player.getDetectCollision().contains(friend.getDetectCollision().right, friend.getDetectCollision().bottom)
                || player.getDetectCollision().contains(friend.getDetectCollision().left, friend.getDetectCollision().bottom)){
            isGameOver = true;
        }
    }

    public void enemyCollision(){
        if(player.getDetectCollision().contains(enemy.getDetectCollision().right, enemy.getDetectCollision().top)
                || player.getDetectCollision().contains(enemy.getDetectCollision().left, enemy.getDetectCollision().top)
                || player.getDetectCollision().contains(enemy.getDetectCollision().right, enemy.getDetectCollision().bottom)
                || player.getDetectCollision().contains(enemy.getDetectCollision().left, enemy.getDetectCollision().bottom)){
            isEnemyDestroyed = true;
        }
    }


    public static void stopMusic(){
        gameOnsound.stop();
    }

    private void update() {
        score++;
        if(enemy.getX() < 15 && !isEnemyDestroyed){
            countMisses++;
        } else {
            isEnemyDestroyed = false;
            isEnemyDestroyedSound = false;
        }

        player.update();

        friend.update(player.getSpeed());

        enemy.update(player.getSpeed());



        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        friendCollision();
        if(!isEnemyDestroyed) enemyCollision();
        if(isEnemyDestroyed && !isEnemyDestroyedSound && !killedEnemysound.isPlaying()){
            killedEnemysound.start();
            isEnemyDestroyedSound = true;
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


}