package com.example.mohammad.fighterjet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreBoard extends AppCompatActivity {


    private TextView textView,textView2,textView3,textView4;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        textView = (TextView) this.findViewById(R.id.textView);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        textView3 = (TextView) this.findViewById(R.id.textView3);
        textView4 = (TextView) this.findViewById(R.id.textView4);


        sharedPreferences  = getSharedPreferences("com.example.mohammad.fighterjet", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText("1. "+sharedPreferences.getInt("score1",0));
        textView2.setText("2. "+sharedPreferences.getInt("score2",0));
        textView3.setText("3. "+sharedPreferences.getInt("score3",0));
        textView4.setText("4. "+sharedPreferences.getInt("score4",0));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ScoreBoard.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
