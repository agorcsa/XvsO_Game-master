package com.example.xvso.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xvso.R;
import com.example.xvso.adapter.SlideAdapter;
import com.example.xvso.object.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class AlienActivity extends AppCompatActivity implements SlideAdapter.ShowDescription, SlideAdapter.SliderViewHolder.AlienClick {

    public static final String ALIEN_KEY = "alien_key";
    private ViewPager2 viewPager2;
    List<SliderItem> sliderItems = new ArrayList<>();

    private TextView alienDescription;
    private ConstraintLayout alienDescriptionContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien);

        viewPager2 = findViewById(R.id.alien_view_pager_slider);
        alienDescriptionContainer = findViewById(R.id.alien_text_container);
        alienDescription = findViewById(R.id.alien_description_text_view);

        String alien1Text = "Name: Tasp"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Characteristics:"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Name on earth: Humans call it Monoculus"
                + System.getProperty("line.separator")
                + "Race: Amphibio"
                + System.getProperty("line.separator")
                + "Height: 60 - 80cm (for adult of its species)"
                + System.getProperty("line.separator")
                + "Adulthood: under 14 years"
                + System.getProperty("line.separator")
                + "Planet: Pegasus 3"
                + System.getProperty("line.separator")
                + "Taps' short story:"
                + System.getProperty("line.separator")
                + "On their home planet, Pegasus 3, the climate is very stormy most of the part of the year, but their amphibian nature helped them to find abundant food during the whole year. Their planet is home to usually smaller life forms, Tasp being the largest."
                + System.getProperty("line.separator");

        String alien2Text = "Name: Glu"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Characteristics:"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Name on earth: Humans call it Eyed-Jelly"
                + System.getProperty("line.separator")
                + "Race: Alborian"
                + System.getProperty("line.separator")
                + "Height: 135 - 150cm (for adult of its species)"
                + System.getProperty("line.separator")
                + "Adulthood: under 7 years"
                + System.getProperty("line.separator")
                + "Planet: Alborus"
                + System.getProperty("line.separator")
                + "Glu's short story:"
                + System.getProperty("line.separator")
                + "Alborus is a planet with moderate climate. Alborians are forming a society with common support to each others. Everything is well designed and they live under strict social rules. They are friendly beings, but humans have to be careful not to break their rules as punishment can even to not return to Earth ever again."
                + System.getProperty("line.separator");

        String alien3Text = "3";
        String alien4Text = "4";
        String alien5Text = "5";
        String alien6Text = "6";

        sliderItems.add(new SliderItem(R.drawable.alien1, alien1Text));
        sliderItems.add(new SliderItem(R.drawable.alien2, alien2Text));
        sliderItems.add(new SliderItem(R.drawable.alien3, alien3Text));
        sliderItems.add(new SliderItem(R.drawable.alien4, alien4Text));
        sliderItems.add(new SliderItem(R.drawable.alien5, alien5Text));
        sliderItems.add(new SliderItem(R.drawable.alien6, alien6Text));

        viewPager2.setAdapter(new SlideAdapter(sliderItems, viewPager2, this, this::onImageHovering));

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
                alienDescriptionContainer.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    @Override
    public void onImageHover(String description) {
        alienDescriptionContainer.setVisibility(View.VISIBLE);
        alienDescription.setText(description);
    }
}
