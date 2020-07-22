package com.example.xvso.ui;

import android.content.Intent;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivitySettingsBinding;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding settingsBinding;

    private MediaPlayer mediaPlayer;

    public final static String SWITCH_VALUE_SOUND = "switch_value_sound";
    public final static String SWITCH_VALUE_MUSIC = "switch_value_music";
    public final static String SWITCH_VALUE_MODE = "switch_value_mode";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).hide();

        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.setLifecycleOwner(this);

        saveSwitchValue();
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onSwitchClick(Switch s, String key) {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("switch_status", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(key, b);
                editor.apply();
            }
        });
    }
    public void saveSwitchValue() {
        onSwitchClick(settingsBinding.soundSwitch, SWITCH_VALUE_SOUND);
        onSwitchClick(settingsBinding.musicSwitch, SWITCH_VALUE_MUSIC);
        onSwitchClick(settingsBinding.modeSwitch, SWITCH_VALUE_MODE);
    }
}
