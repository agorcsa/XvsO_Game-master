package com.example.xvso.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityAboutBinding;

import java.util.Objects;


public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding aboutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        Objects.requireNonNull(getSupportActionBar()).hide();

        String aboutText = getResources().getString(R.string.welcome)
                            + System.getProperty("line.separator")
                            +  System.getProperty("line.separator")
                            + getResources().getString(R.string.about_the_app)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.rule_1)
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.rule_2)
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.rule_3)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.spanish_support);

        aboutBinding.aboutTextView.setText(aboutText);
    }

    @Override
    public void onBackPressed() {
        // place your code as needed here
        super.onBackPressed();
    }
}
