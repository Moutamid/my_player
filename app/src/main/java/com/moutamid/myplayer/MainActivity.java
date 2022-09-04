package com.moutamid.myplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView playBtn, pauseBtn , idBtnPlay2 , idBtnStop;
    ImageView forward, backward;
    ImageView forward10, backward10;
    MediaPlayer mediaPlayer;

    LinearLayout layout_play , layout_pause;
    CardView card_seek;

    SeekBar seekBar;
    TextView playerPosition , playerDuration;

    Handler handler = new Handler();
    Runnable runnable;
    String audioUrl;

    @Override
    protected void onStart() {
        super.onStart();
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.idBtnPlay);
        pauseBtn = findViewById(R.id.idBtnPause);
        idBtnPlay2 = findViewById(R.id.idBtnPlay2);
        idBtnStop = findViewById(R.id.idBtnStop);
        forward = findViewById(R.id.idBtnforward);
        backward = findViewById(R.id.idBtnbackward);
        forward10 = findViewById(R.id.idBtnforward10);
        backward10 = findViewById(R.id.idBtnbackward10);

        layout_play = findViewById(R.id.layout_play);
        layout_pause = findViewById(R.id.layout_pause);
        card_seek = findViewById(R.id.card_seek);

        layout_play.setVisibility(View.VISIBLE);

        seekBar = findViewById(R.id.seek_bar);
        playerPosition = findViewById(R.id.player_positiom);
        playerDuration = findViewById(R.id.player_duration);

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this::run , 500);
            }
        };

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int seekForwardTime = 30 * 1000;
                    if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                        mediaPlayer.seekTo(currentPosition + seekForwardTime);
                    } else {
                        mediaPlayer.seekTo(mediaPlayer.getDuration());
                    }
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int seekBackwardTime = 30 * 1000;
                    if (currentPosition - seekBackwardTime >= 0) {
                        mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        mediaPlayer.seekTo(0);
                    }
                }
            }
        });

        forward10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int x = mediaPlayer.getDuration();
                    int seekForwardTime = (x*10)/100;
                    if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                        mediaPlayer.seekTo(currentPosition + seekForwardTime);
                    } else {
                        mediaPlayer.seekTo(mediaPlayer.getDuration());
                    }
                }
            }
        });

        backward10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int x = mediaPlayer.getDuration();
                    int seekBackwardTime = (x * 10)/100;
                    if (currentPosition - seekBackwardTime >= 0) {
                        mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        mediaPlayer.seekTo(0);
                    }
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
                // ARE WE CONNECTED TO THE NET
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    playAudio();
                } else {
                    Toast.makeText(MainActivity.this, "Internet is not available...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idBtnStop.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
                idBtnPlay2.setVisibility(View.VISIBLE);
                layout_play.setVisibility(View.GONE);
                layout_pause.setVisibility(View.VISIBLE);
                card_seek.setVisibility(View.VISIBLE);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                    Toast.makeText(MainActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                }
            }
        });

        idBtnPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idBtnStop.setVisibility(View.GONE);
                idBtnPlay2.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
            }
        });

        idBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idBtnStop.setVisibility(View.GONE);
                layout_pause.setVisibility(View.GONE);
                layout_play.setVisibility(View.VISIBLE);
                card_seek.setVisibility(View.GONE);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    Toast.makeText(MainActivity.this, "Audio has been Stopped", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    private void playAudio() {
        layout_play.setVisibility(View.GONE);
        layout_pause.setVisibility(View.VISIBLE);
        card_seek.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
        idBtnPlay2.setVisibility(View.GONE);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        handler.postDelayed(runnable , 0);
        playerDuration.setText(convertFormat(mediaPlayer.getDuration()));
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }
}