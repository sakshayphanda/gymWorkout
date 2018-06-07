package com.sakshay.gymtraining;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class ExerciseActivity extends AppCompatActivity {

    private EditText totalReps;
    private VideoView exerciseVideoView;
    private TextView count;
    private Handler handler;
    private int i=0;
    private int total;
    private int seconds;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initUI();
        setListeners();
    }

    private void initUI() {
        totalReps = (EditText) findViewById(R.id.reps);
        exerciseVideoView = (VideoView) findViewById(R.id.exercise);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.rep;
        exerciseVideoView.setVideoURI(Uri.parse(path));
        count = (TextView) findViewById(R.id.count);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(exerciseVideoView);
        exerciseVideoView.setMediaController(mediaController);
    }


    private void setListeners() {
        exerciseVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exerciseVideoView.isPlaying() && !totalReps.getText().toString().isEmpty())
                    exerciseVideoView.pause();

                else
                    exerciseVideoView.start();
            }
        });
        exerciseVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
            @Override
            public void onPrepared(MediaPlayer mp) {

                int duration = mp.getDuration() / 1000;
                int hours = duration / 3600;
                int minutes = (duration / 60) - (hours * 60);
                seconds = duration - (hours * 3600) - (minutes * 60);
                String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void proceed(View view) {
         total = Integer.parseInt(totalReps.getText().toString());
         if(total<5)
         {
             Toast.makeText(this, "Enter atleast 5", Toast.LENGTH_SHORT).show();
             return;
         }

        count.setText(i + "/" + total);

        exerciseVideoView.pause();

        exerciseVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                duration = mediaPlayer.getDuration();
                int hours = duration / 3600;
                int minutes = (duration / 60) - (hours * 60);
                seconds = duration - (hours * 3600) - (minutes * 60) ;
            }
        });
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(seconds*1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    if(Thread.currentThread().isInterrupted())
                                        return;

                                    if(i<=total-1) {
                                        if(!exerciseVideoView.isPlaying())
                                        exerciseVideoView.start();
                                        exerciseVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                updateTextView();
                                            }
                                        });
                                    }
                                    else {
                                        Thread.currentThread().interrupt();
                                        exerciseVideoView.pause();
                                    }
                            }

                        });

                    }
                } catch (InterruptedException e) {
                    Log.d("----","abc");
                }
                catch (Exception ex)
                {
                    Toast.makeText(ExerciseActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        };

        t.start();
       // t.interrupt();
    }

    private void updateTextView() {
        if (i >= total-1)

        {
            Toast.makeText(this, "Successfully completed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,LogActivity.class);
            startActivity(intent);

        }


        else {
            count.setText(++i + "/" + total);
        }
    }
}
