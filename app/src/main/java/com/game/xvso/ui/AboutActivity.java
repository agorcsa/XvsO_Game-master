package com.game.xvso.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.game.xvso.R;
import com.game.xvso.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding aboutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        setupPositiveSound();
        setupNegativeSound();

        readSoundFromSharedPrefs();

        String aboutText0 = getResources().getString(R.string.welcome);

        String aboutText1 = getResources().getString(R.string.about_the_app)
                + System.getProperty("line.separator");

        String aboutText2 = getResources().getString(R.string.rule_2)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.rule_3)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.spanish_support);


        aboutBinding.aboutTextView0.setText(aboutText0);

        aboutBinding.aboutTextView1.setText(aboutText1);

        aboutBinding.aboutTextView2.setText(aboutText2);
    }

    public void onExitButtonClick(View view) {

        if (isSoundOn) {
            negativeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });
            negativeSound.start();
        } else {
            Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
