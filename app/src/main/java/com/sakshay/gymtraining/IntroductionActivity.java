package com.sakshay.gymtraining;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class IntroductionActivity extends AppCompatActivity {

    private VideoView videoView;
    private String path = "";
    private TextView countdownTimer;
    private long duration;
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initUI();
        setListeners();
    }

    private void setListeners() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying())
                videoView.pause();

                else
                    videoView.start();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
            @Override
            public void onPrepared(MediaPlayer mp) {

                int duration=mp.getDuration()/1000;
                int hours = duration / 3600;
                int minutes = (duration / 60) - (hours * 60);
                seconds = duration - (hours * 3600) - (minutes * 60) ;
                String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);

                VideoTimer();
            }
        });
    }

    private void initUI() {
        videoView = (VideoView) findViewById(R.id.video);
        path="android.resource://" + getPackageName() + "/" + R.raw.sample;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        countdownTimer = (TextView) findViewById(R.id.countdown);
    }

    private void VideoTimer() {
        new CountDownTimer(seconds*1000,1000) {
            @Override
            public void onTick(long l) {

                Log.d("------", String.valueOf(seconds));
                countdownTimer.setText("Intro ends in : " + l/1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(IntroductionActivity.this,ExerciseActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    public void skip(View view) {
        Intent intent = new Intent(this,ExerciseActivity.class);
        startActivity(intent);

    }
}
