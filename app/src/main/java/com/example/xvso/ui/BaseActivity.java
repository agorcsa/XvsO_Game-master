package com.example.xvso.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xvso.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    public static final String KEY = "key";

    public static final String MUSIC_STATUS = "music_status";
    public static final String SOUND_STATUS = "sound_status";

    public boolean isMusicOn = true;
    public boolean isSoundOn = true;

    // used for music
    public android.media.MediaPlayer mediaPlayer;

    // used for sound
    public MediaPlayer positiveSound;
    public MediaPlayer negativeSound;

    FirebaseAuth auth;

    public void saveMusicToSharedPrefs(boolean b) {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(MUSIC_STATUS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY, b);
        editor.apply();
    }

    public void saveSoundToSharedPrefs(boolean b) {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(SOUND_STATUS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY, b);
        editor.apply();
    }

    public boolean readMusicFromSharedPrefs() {
        SharedPreferences sharedPrefs = getSharedPreferences(MUSIC_STATUS, Context.MODE_PRIVATE);
        boolean value = sharedPrefs.getBoolean(KEY, false);
        isMusicOn = value;
        return isMusicOn;
    }

    public boolean readSoundFromSharedPrefs() {
        SharedPreferences sharedPrefs = getSharedPreferences(SOUND_STATUS, Context.MODE_PRIVATE);
        boolean value = sharedPrefs.getBoolean(KEY, false);
        isSoundOn = value;
        return isSoundOn;
    }

    public void playMusic() {
        Handler handler = new Handler();
        Runnable myRunnable = new Runnable() {
            public void run() {
                mediaPlayer = android.media.MediaPlayer.create(BaseActivity.this, R.raw.orbitbeat);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        };
        handler.postDelayed(myRunnable, 1000);
    }

    public void stopMusic() {
        mediaPlayer.stop();
        Log.v("LOG_TAG", "Media player is stopped");
    }

    // Firebase
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getFirebaseUser() {

        return auth.getCurrentUser();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (positiveSound != null) {
            positiveSound.stop();
            positiveSound.release();
            positiveSound = null;
        }

        if (negativeSound != null) {
            negativeSound.stop();
            negativeSound.release();
            negativeSound = null;
        }
        Log.i("LOG_TAG", "onPause is called");
    }

    public void checkMusic() {
        isMusicOn = readMusicFromSharedPrefs();
        if (isMusicOn) {
            playMusic();
        }
    }

    public void checkSound() {
        isSoundOn = readSoundFromSharedPrefs();
        if (isSoundOn) {
            playPositiveSound();
            playNegativeSound();
        }
    }

    public void setupPositiveSound() {
        positiveSound = MediaPlayer.create(this, R.raw.positive);
        Log.i("LOG_TAG", "Positive sound");
    }

    public void setupNegativeSound() {
        negativeSound = MediaPlayer.create(this, R.raw.negative);
        Log.i("LOG_TAG", "Negative sound");
    }

    public void playPositiveSound() {
        if (isSoundOn) {
            positiveSound.start();
        }
    }

    public void playNegativeSound() {
        if (isSoundOn) {
            negativeSound.start();
        }
    }

    public void stopPositiveSound() {
        if (positiveSound != null) {
            positiveSound.stop();
        }
    }

    public void stopNegativeSound() {
        if (negativeSound != null) {
            negativeSound.stop();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
