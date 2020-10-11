package com.example.xvso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityAboutBinding;


public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding aboutBinding;
    private TextView attributionLink;
    private boolean isImageClicked = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        attributionLink = findViewById(R.id.attribution_link_text_view);

        aboutBinding.astronautImageView.setImageResource(R.drawable.astronautprofile);

        aboutBinding.astronautImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isImageClicked) {

                    isImageClicked = true;

                    aboutBinding.moonImageView.animate().alpha(0f).setDuration(2000);
                    aboutBinding.astronautImageView.animate().alpha(1f).setDuration(2000);

                    showViews();
                    hidePhotoAttributions();

                } else {

                    isImageClicked = false;

                    aboutBinding.astronautImageView.animate().alpha(0f).setDuration(2000);
                    aboutBinding.moonImageView.animate().alpha(1f).setDuration(2000);

                    hideViews();
                    displayPhotoAttributions();
                }
            }
        });

        String aboutText2 = getResources().getString(R.string.rule_2)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.rule_3)
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + getResources().getString(R.string.spanish_support);

        String aboutText0 = getResources().getString(R.string.welcome);

        String aboutText1 = getResources().getString(R.string.about_the_app)
                + System.getProperty("line.separator");

        String aboutText3 = getResources().getString(R.string.attributions_text);

        aboutBinding.aboutTextView0.setText(aboutText0);

        aboutBinding.aboutTextView1.setText(aboutText1);

        aboutBinding.aboutTextView2.setText(aboutText2);

        aboutBinding.aboutTextView3.setText(aboutText3);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void displayPhotoAttributions() {

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);

        aboutBinding.attributionsText.setAnimation(animation);
        aboutBinding.aboutScrollView.setAnimation(animation);
        aboutBinding.moonClickTextView.setAnimation(animation);

        aboutBinding.attributionsText.setVisibility(View.VISIBLE);
        aboutBinding.aboutScrollView.setVisibility(View.VISIBLE);
        aboutBinding.moonClickTextView.setVisibility(View.VISIBLE);

        attributionLink.setMovementMethod(LinkMovementMethod.getInstance());
        attributionLink.setText(Html.fromHtml(getString(R.string.attributions)));
    }

    public void onExitToHome(View view) {
        Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void hideViews() {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(2000);

        aboutBinding.aboutLayout.setAnimation(animation);
        aboutBinding.aboutLayout.setVisibility(View.INVISIBLE);
    }

    public void showViews() {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);

        aboutBinding.aboutLayout.setAnimation(animation);
        aboutBinding.aboutLayout.setVisibility(View.VISIBLE);
    }

    public void hidePhotoAttributions() {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(2000);

        aboutBinding.attributionsText.startAnimation(animation);
        aboutBinding.aboutScrollView.startAnimation(animation);
        aboutBinding.moonClickTextView.startAnimation(animation);

        aboutBinding.attributionsText.setVisibility(View.INVISIBLE);
        aboutBinding.aboutScrollView.setVisibility(View.INVISIBLE);
        aboutBinding.moonClickTextView.setVisibility(View.INVISIBLE);
    }
}
