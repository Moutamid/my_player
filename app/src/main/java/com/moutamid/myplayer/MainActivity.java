package com.moutamid.myplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView playBtn, pauseBtn;
    ImageView forward, backward;
    ImageView forward10, backward10;
    MediaPlayer mediaPlayer;

    LinearLayout layout_play , layout_pause;
    CardView card_seek;

    SeekBar seekBar;
    TextView playerPosition , playerDuration;

    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.idBtnPlay);
        pauseBtn = findViewById(R.id.idBtnPause);
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
                    int seekForwardTime = 10 * 1000;
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
                    int seekBackwardTime = 10 * 1000;
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
                layout_play.setVisibility(View.GONE);
                layout_pause.setVisibility(View.VISIBLE);
                card_seek.setVisibility(View.VISIBLE);
                playAudio();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_play.setVisibility(View.VISIBLE);
                layout_pause.setVisibility(View.GONE);
                card_seek.setVisibility(View.GONE);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                    playerPosition.setText("00:00");
                    playerDuration.setText("00:00");
                    Toast.makeText(MainActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
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
        String audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable , 0);
            playerDuration.setText(convertFormat(mediaPlayer.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }
}