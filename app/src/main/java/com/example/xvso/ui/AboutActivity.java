package com.example.xvso.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding aboutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        String aboutText = getResources().getString(R.string.welcome_earthling) + System.getProperty("line.separator") +  System.getProperty("line.separator") + getResources().getString(R.string.about_the_app);
        aboutBinding.aboutTextView.setText(aboutText);
    }
}
