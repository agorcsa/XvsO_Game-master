package com.example.xvso.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityPolicyAttributionsBinding;
import com.example.xvso.license.ReadFileAsyncTask;
import com.yydcdut.markdown.MarkdownProcessor;
import com.yydcdut.markdown.syntax.text.TextFactory;
import com.yydcdut.rxmarkdown.RxMDConfiguration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        try {
            displayMarkdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void displayMarkdown() throws IOException {
        MarkdownProcessor markdownProcessor = new MarkdownProcessor(this);
        markdownProcessor.factory(TextFactory.create());
        policyBinding.policyText.setText(markdownProcessor.parse(readFromFile(getApplicationContext(), "privacypolicy.txt")));
    }

    private String readFromFile(Context context, String fname) {

        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder("");

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fname)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                stringBuilder.append(mLine + "\n");

                RxMDConfiguration rxMDConfiguration = new RxMDConfiguration.Builder(context)
                        .setLinkFontColor(Color.BLUE)//default color of link text
                        .setUnOrderListColor(Color.BLUE)
                        .build();

                MarkdownProcessor markdownProcessor = new MarkdownProcessor(this);
                markdownProcessor.config(rxMDConfiguration);
                markdownProcessor.factory(TextFactory.create());
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    Log.v("LOG_TAG", "Text File couldn not be open");
                }
            }
        }
        return stringBuilder.toString();
    }
}
