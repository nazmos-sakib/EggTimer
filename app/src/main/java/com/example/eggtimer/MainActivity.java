package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.example.eggtimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    boolean isTimerRunning = false;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.seekBarMainActivity.setMax(600); //10 minutes
        binding.seekBarMainActivity.setProgress(30); //30 seconds
        binding.seekBarMainActivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnGoMainActivity.setOnClickListener(v -> {
            if (!isTimerRunning){
                binding.seekBarMainActivity.setEnabled(false);
                binding.btnGoMainActivity.setText(R.string.stop);
                goTimer();
            } else {
                onCancelTimer();
            }
        });

    }

    public void goTimer(){
        isTimerRunning = true;
        countDownTimer = new CountDownTimer(binding.seekBarMainActivity.getProgress()* 1000L, 1000) {
            @Override
            public void onTick(long l) {

                updateTimer((int) l / 1000);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: ");
                //onCancelTimer();
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                player.start();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (player.isPlaying()) {
                            new Handler().postDelayed(this, 1000);
                        }else {
                            reSetTimer();
                        }
                    }
                });

            }
        }.start();
    }

    public void updateTimer(int secondsLeft){
        binding.seekBarMainActivity.setProgress(secondsLeft);
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        binding.tvTimeLeftMainActivity.setText(String.valueOf(minutes).concat(":").concat(String.valueOf(seconds)));
    }

    public void onCancelTimer(){
        countDownTimer.cancel();
        reSetTimer();
    }

    public void reSetTimer(){
        isTimerRunning = false;
        binding.seekBarMainActivity.setEnabled(true);
        binding.btnGoMainActivity.setText(R.string.go);
        updateTimer(30);
    }
}