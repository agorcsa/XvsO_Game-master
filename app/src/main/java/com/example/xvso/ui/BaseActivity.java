package com.example.xvso.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

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

    public static final String STATUS = "status";
    public android.media.MediaPlayer mediaPlayer;

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

    // MediaPlayer
    public void playSound() {
        SharedPreferences sharedPref = getSharedPreferences(STATUS, Context.MODE_PRIVATE);
        boolean value = sharedPref.getBoolean(KEY, false);
        if (value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer = android.media.MediaPlayer.create(this, R.raw.orbit);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
    }

    public void playMusic() {
        mediaPlayer = android.media.MediaPlayer.create(this, R.raw.orbitbeat);
        mediaPlayer.start();
        //mediaPlayer.setLooping(true);
    }

    public void stopMusic() {
        mediaPlayer.stop();
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
}
