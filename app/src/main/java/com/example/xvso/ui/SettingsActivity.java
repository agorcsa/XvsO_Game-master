package com.example.xvso.ui;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivitySettingsBinding;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding settingsBinding;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).hide();

        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.setLifecycleOwner(this);
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void setUpMediaPlayer() {
        mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.orbit);
        mediaPlayer.start();
    }
}
