package com.example.mohammad.fighterjet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * Created by mohammad on 24/12/16.
 */

public class GameView extends SurfaceView implements Runnable{

    volatile boolean isPlaying;

    private PlayerMethod playerMethod;
    private DestroyEffect destroyEffect;
    private Fighters fighters;

    private Thread mainThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Background> objects = new
            ArrayList<Background>();

    private MediaPlayer mediaPlayer;

    int screenX;
    int countMisses;
    boolean flag ;
    int score;
    int highScore[] = new int[4];
    private boolean isGameOver;

    SharedPreferences sharedPreferences;

    public GameView(Context context,int screenX,int screenY) {
        super(context);
        playerMethod = new PlayerMethod(context,screenX,screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        int dots = 100;
        for (int i = 0; i < dots; i++) {
            Background background  = new Background(screenX, screenY);
            objects.add(background);
        }
        fighters = new Fighters(context,screenX,screenY);
        destroyEffect = new DestroyEffect(context);
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(),R.raw.boom);

        this.screenX = screenX;
        countMisses = 0;
        isGameOver = false;

        score = 0;

        sharedPreferences = context.getSharedPreferences("com.example.mohammad.fighterjet",Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
    }

    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            control();
        }
    }
    private void update() {
        score++;
        isGameOver = false;
        playerMethod.update();

        destroyEffect.setX(-250);
        destroyEffect.setY(-250);

        for (Background background : objects){
            background.update(playerMethod.getSpeed());
        }
        if(fighters.getX()==screenX){
            flag = true;
        }
        fighters.update(playerMethod.getSpeed());

        if (Rect.intersects(playerMethod.getDetectCollision(), fighters.getDetectCollision())) {
            destroyEffect.setX(fighters.getX());
            destroyEffect.setY(fighters.getY());
            fighters.setX(-200);
            mediaPlayer.start();
        }else{
            if(flag){
                if(playerMethod.getDetectCollision().exactCenterX() >= fighters.getDetectCollision().exactCenterX()){
                    countMisses++;
                    flag = false;
                    if(countMisses==5){
                        isPlaying = false;
                        isGameOver = true;

                        for(int i=0;i<4;i++){
                            if(highScore[i]<score){

                                final int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for(int i=0;i<4;i++){

                            int j = i+1;
                            e.putInt("score"+j,highScore[i]);
                        }
                        e.apply();
                    }
                }
            }
        }
    }
    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            //background
            paint.setColor(Color.CYAN);
            for (Background background:objects){
                paint.setStrokeWidth(background.getStarWidth());
                canvas.drawPoint(background.getX(), background.getY(), paint);
            }
            canvas.drawBitmap(
                    playerMethod.getBitmap(),
                    playerMethod.getX(),
                    playerMethod.getY(),
                    paint
            );
            //draw fighters
            canvas.drawBitmap(
                    fighters.getBitmap(),
                    fighters.getX(),
                    fighters.getY(),
                    paint
            );
            //destroy effect
            canvas.drawBitmap(
                    destroyEffect.getBitmap(),
                    destroyEffect.getX(),
                    destroyEffect.getY(),
                    paint
            );
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.drawText("Missed:"+countMisses,300,50,paint);
            canvas.drawText("Total Chances: 5",500,50,paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control(){
        try {
            mainThread.sleep(17);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    public void pause(){
        isPlaying = false;
        try {
            mainThread.join();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    public void resume(){
        isPlaying = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                playerMethod.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                playerMethod.setBoosting();
                break;
        }
        return true;
    }
}
