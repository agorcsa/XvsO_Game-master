package com.example.xvso.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xvso.R;
import com.example.xvso.adapter.SlideAdapter;
import com.example.xvso.object.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class AlienActivity extends AppCompatActivity implements SlideAdapter.SliderViewHolder.AlienClick {

    public static final String ALIEN_KEY = "alien_key";
    private ViewPager2 viewPager2;
    List<SliderItem> sliderItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien);

        viewPager2 = findViewById(R.id.alien_view_pager_slider);

        sliderItems.add(new SliderItem(R.drawable.alien1));
        sliderItems.add(new SliderItem(R.drawable.alien2));
        sliderItems.add(new SliderItem(R.drawable.alien3));
        sliderItems.add(new SliderItem(R.drawable.alien4));
        sliderItems.add(new SliderItem(R.drawable.alien5));
        sliderItems.add(new SliderItem(R.drawable.alien6));

        viewPager2.setAdapter(new SlideAdapter(sliderItems, viewPager2, this));

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
        Intent intent = new Intent(AlienActivity.this, ComputerActivity.class);
        int image = item.getImage();
        intent.putExtra(ALIEN_KEY, image);
        startActivity(intent);
    }

    public void onImageHovering(String text) {
        Toast viewToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        View view = findViewById(R.id.alien_view_pager_slider);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }
}
