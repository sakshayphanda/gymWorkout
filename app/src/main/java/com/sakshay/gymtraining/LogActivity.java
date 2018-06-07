package com.sakshay.gymtraining;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class LogActivity extends AppCompatActivity {

    private TextView countdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        InitUI();
        setListeners();
        VideoTimer();
    }

    private void setListeners() {
    }

    private void InitUI() {
        countdownTimer = findViewById(R.id.countdown);
    }


    private void VideoTimer() {
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {

                //   Log.d("------", String.valueOf(seconds));
                countdownTimer.setText(" Take rest for : " + l / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(LogActivity.this, IntroductionActivity.class);
                startActivity(intent);
            }
        }.start();
    }
}
