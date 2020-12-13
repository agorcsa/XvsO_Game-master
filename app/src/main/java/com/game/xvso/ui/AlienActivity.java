package com.game.xvso.ui;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.game.xvso.R;
import com.game.xvso.adapter.SlideAdapter;
import com.game.xvso.object.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class AlienActivity extends BaseActivity implements SlideAdapter.ShowDescription, SlideAdapter.SliderViewHolder.AlienClick {

    public static final String ALIEN_KEY = "alien_key";
    public static final String ALIEN_NAME = "alien_name";

    private ViewPager2 viewPager2;
    List<SliderItem> sliderItems = new ArrayList<>();

    private TextView alienDescription;
    private TextView scroll;
    private TextView chooseAlien;
    private Button exitButton;

    private CardView cardView;

    private Button xButton;
    private String alienName1 = "Tasp";
    private String alienName2 = "Glu";
    private String alienName3 = "Eshu";
    private String alienName4 = "Ushuqop";
    private String alienName5 = "Yumay";
    private String alienName6 = "Uzapoc";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien);

        setupPositiveSound();
        setupNegativeSound();

        readSoundFromSharedPrefs();

        viewPager2 = findViewById(R.id.alien_view_pager_slider);
        alienDescription = findViewById(R.id.alien_description_text_view);
        scroll = findViewById(R.id.scroll_text);
        cardView = findViewById(R.id.alien_card_view);
        xButton = findViewById(R.id.x_button);

        exitButton = findViewById(R.id.exit_button);
        chooseAlien = findViewById(R.id.choose_alien_text_view);

        String alien1 = getResources().getString(R.string.alien1Text);
        String alien2 = getResources().getString(R.string.alien2Text);
        String alien3 = getResources().getString(R.string.alien3Text);
        String alien4 = getResources().getString(R.string.alien4Text);
        String alien5 = getResources().getString(R.string.alien5Text);
        String alien6 = getResources().getString(R.string.alien6Text);

        sliderItems.add(new SliderItem(alienName1, R.drawable.alien1, alien1));
        sliderItems.add(new SliderItem(alienName2, R.drawable.alien2, alien2));
        sliderItems.add(new SliderItem(alienName3, R.drawable.alien3, alien3));
        sliderItems.add(new SliderItem(alienName4, R.drawable.alien4, alien4));
        sliderItems.add(new SliderItem(alienName5, R.drawable.alien5, alien5));
        sliderItems.add(new SliderItem(alienName6, R.drawable.alien6, alien6));

        viewPager2.setAdapter(new SlideAdapter(sliderItems, viewPager2, this, this));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onAlienClick(SliderItem item) {

        if (isSoundOn) {
            positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Intent intent = new Intent(AlienActivity.this, ComputerActivity.class);
                    int image = item.getImage();
                    intent.putExtra(ALIEN_KEY, image);
                    intent.putExtra(ALIEN_NAME, item.getName());
                    startActivity(intent);
                }
            });
            positiveSound.start();
        } else {
            Intent intent = new Intent(AlienActivity.this, ComputerActivity.class);
            int image = item.getImage();
            intent.putExtra(ALIEN_KEY, image);
            intent.putExtra(ALIEN_NAME, item.getName());
            startActivity(intent);
        }
    }


    @Override
    public void onImageHover(String description) {
        chooseAlien.setVisibility(View.INVISIBLE);
        viewPager2.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        alienDescription.setText(Html.fromHtml(description));
    }

    public void onXButtonClick(View view) {
        chooseAlien.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.VISIBLE);
    }

    public void onExitAlienScreen(View view) {

        if (isSoundOn) {
            negativeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    Intent intent = new Intent(AlienActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });
            negativeSound.start();
        } else {
            Intent intent = new Intent(AlienActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
