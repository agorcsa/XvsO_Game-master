package com.example.xvso.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityPolicyAttributionsBinding;
import com.example.xvso.license.ReadFileAsyncTask;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class PolicyAttributionsActivity extends BaseActivity implements ReadFileAsyncTask.OnTaskCompleted {

    private ActivityPolicyAttributionsBinding policyBinding;

    private boolean isImageClicked = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        policyBinding = DataBindingUtil.setContentView(this, R.layout.activity_policy_attributions);

        try {
            String text = new ReadFileAsyncTask(new WeakReference<Context>(this), this).execute().get();
            policyBinding.policyText.setText(text);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        isMusicOn = readMusicFromSharedPrefs();
        if (isMusicOn) {
            playMusic();
        }

        isSoundOn = readSoundFromSharedPrefs();
        if (isSoundOn) {
            playSound();
        }

        policyBinding.blueMoonImageView.setImageResource(R.drawable.bluemoon);

        policyBinding.blueMoonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isImageClicked) {

                    isImageClicked = true;

                    policyBinding.moonImageView.animate().alpha(0f).setDuration(2000);
                    policyBinding.blueMoonImageView.animate().alpha(1f).setDuration(2000);

                    showViews();
                    hidePhotoAttributions();

                } else {

                    isImageClicked = false;

                    policyBinding.blueMoonImageView.animate().alpha(0f).setDuration(2000);
                    policyBinding.moonImageView.animate().alpha(1f).setDuration(2000);

                    hideViews();
                    displayPhotoAttributions();
                }
            }
        });
    }

    public void hideViews() {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(2000);

        policyBinding.blueMoonClickTextView.setAnimation(animation);
        policyBinding.blueMoonClickTextView.setVisibility(View.INVISIBLE);

        policyBinding.policyScrollView.setAnimation(animation);
        policyBinding.policyScrollView.setVisibility(View.INVISIBLE);
    }

    public void showViews() {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);

        policyBinding.blueMoonClickTextView.setAnimation(animation);
        policyBinding.blueMoonClickTextView.setVisibility(View.VISIBLE);

        policyBinding.policyScrollView.setAnimation(animation);
        policyBinding.policyScrollView.setVisibility(View.VISIBLE);
    }

    public void onExitToHome(View view) {
        Intent intent = new Intent(PolicyAttributionsActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskCompleted(String result) {
        // here show the text from result
        policyBinding.policyText.setText(result);
    }

    public void displayPhotoAttributions() {

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);

        policyBinding.attributionsText.setAnimation(animation);
        policyBinding.attributionsScrollView.setAnimation(animation);
        policyBinding.moonClickTextView.setAnimation(animation);

        policyBinding.attributionsText.setVisibility(View.VISIBLE);
        policyBinding.attributionsScrollView.setVisibility(View.VISIBLE);
        policyBinding.moonClickTextView.setVisibility(View.VISIBLE);

        policyBinding.attributionLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        policyBinding.attributionLinkTextView.setText(Html.fromHtml(getString(R.string.attributions)));
    }

    public void hidePhotoAttributions() {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(2000);

        policyBinding.attributionsText.startAnimation(animation);
        policyBinding.attributionsScrollView.startAnimation(animation);
        policyBinding.moonClickTextView.startAnimation(animation);

        policyBinding.attributionsText.setVisibility(View.INVISIBLE);
        policyBinding.attributionsScrollView.setVisibility(View.INVISIBLE);
        policyBinding.moonClickTextView.setVisibility(View.INVISIBLE);
    }
}
