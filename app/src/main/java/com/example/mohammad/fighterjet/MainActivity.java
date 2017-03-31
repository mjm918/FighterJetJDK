package com.example.mohammad.fighterjet;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer;
    private ImageButton btn_playnow;
    private ImageButton btn_highscore;
    private ImageButton ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.effect);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        btn_playnow = (ImageButton) findViewById(R.id.btn_playnow);
        btn_highscore = (ImageButton) findViewById(R.id.btn_highscore);
        ask = (ImageButton) findViewById(R.id.ask);

        btn_playnow.setOnClickListener(this);
        btn_highscore.setOnClickListener(this);
        ask.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Confirm Exit");
        alertDialogBuilder.setMessage("Are You Sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_playnow:
                startActivity(new Intent(MainActivity.this,GameActivity.class));
                break;
            case R.id.btn_highscore:
                startActivity(new Intent(MainActivity.this,ScoreBoard.class));
                break;
            case R.id.ask:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("About This Game");
                alertDialogBuilder.setMessage(R.string.ask);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Return To Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
        }
    }
}
