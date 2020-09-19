package com.example.xvso.ui;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String KEY = "key";

    public static final String MUSIC_STATUS = "music_status";
    public static final String SOUND_STATUS = "sound_status";

    public boolean isMusicOn = true;
    public boolean isSoundOn = true;


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
}
