package com.example.xvso.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityAboutBinding;


public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding aboutBinding;
    private TextView attributionLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        attributionLink = findViewById(R.id.attribution_link_text_view);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                displayPhotoAttributions();
            }
        }, 3000);

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

        //aboutBinding.aboutTextView.setText(aboutText);
    }

    @Override
    public void onBackPressed() {
        // place your code as needed here
        super.onBackPressed();
    }

    public void displayPhotoAttributions() {
        attributionLink.setMovementMethod(LinkMovementMethod.getInstance());
        attributionLink.setText(Html.fromHtml(getString(R.string.attributions)));
    }

    public void startImplicitIntent(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pngtree.com/so/world-space-day-illustration"));
        startActivity(intent);
    }

    public void onExitToHome(View view) {
        Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
