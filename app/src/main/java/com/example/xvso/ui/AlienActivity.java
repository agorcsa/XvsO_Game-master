package com.example.xvso.ui;

import android.content.Intent;

import android.os.Bundle;
import android.text.Html;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

    // fields in bold
    // TO DO: add strings to strings.xml
    private String name = "Name: ";
    private String characteristics = "Characteristics";
    private String nameOnEarth = "Name on earth: ";
    private String race = "Race: ";
    private String height = "Height: ";
    private String adulthood = "Adulthood: ";
    private String planet = "Planet: ";
    private String story = " short story";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien);

        viewPager2 = findViewById(R.id.alien_view_pager_slider);
        alienDescription = findViewById(R.id.alien_description_text_view);
        scroll = findViewById(R.id.scroll_text);
        cardView = findViewById(R.id.alien_card_view);
        xButton = findViewById(R.id.x_button);

        exitButton = findViewById(R.id.exit_button);
        chooseAlien = findViewById(R.id.choose_alien_text_view);

        String alien1Text = "<b>" + name + "</b> " + "Tasp"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Humans call it Monoculus"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Amphibio"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "60 - 80cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 14 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Pegasus 3"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Tasp's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "On their home planet, Pegasus 3, the climate is very stormy most of the part of the year, but their amphibian nature helped them to find abundant food during the whole year. Their planet is home to usually smaller life forms, Tasp being the largest."
                + "<br>" + "</br>"
                + "<br>" + "</br>";

        String alien2Text = "<b>" + name + "</b> " + "Obgu"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Humans call it Happy Eyed Face"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Alborian"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "30 - 40cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 7 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Alborus"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Obgu's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "Alborians are forming a society with common support for each other, but they live under strict rules. They are very friendly and helpful to humans. Having an Alborian on a spaceship can be very beneficial, as they are great navigators. Alborus is a planet wih moderate climate and luxuriant plants, the food of the Alborians is mainly plat base and they eat several species of snails for protein. "
                + "<br>" + "</br>"
                + "<br>" + "</br>";

        String alien3Text = "<b>" + name + "</b> " + "Eshu"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Yellow Snail"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Redolan"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "30 - 40cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 8 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Redolus"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Eshu's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "The Redolans developed from the same ancestors as the Alborians. Million of years before, both the Alborians and the Redolans, lived together on the same planet, which was a supernova. They live also in a developed society, they are omnivores, but they are able to sustain themselves also only with plant-based food. They make excellent traders and scientists."
                + "<br>" + "</br>"
                + "<br>" + "</br>";

        String alien4Text = "<b>" + name + "</b> " + "Ushuqop"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Blue Monster"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Atlasians"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "90 - 110 cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 11 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Atlas 2"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Ushuqop's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "Ushuqop comes from the cold planet Atlas 2, one of the 6 planets from the Atlas planet cluster. Atlasians hunt for animals in groups. Cruel in war, but very patient and polite with guests, they indulge in hedonistic pleasure like alcohol, music and gambling. Their strong strategic thinking and coleric nature make then a fearful enemy."
                + "<br>" + "</br>"
                + "<br>" + "</br>";

        String alien5Text = "<b>" + name + "</b> " + "Yumay"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Running Strawberry"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Atlasian"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "30 - 40cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 9 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Atlas 4"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Yumay's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "The creature lost its connection with nature due to their society development, as they are provided with artificial food and drinks. As they have fast feet, running is traditional sport and many running competitions are being organised. Atlas 4 is a planet with hot climate, so the aliens spend most o their time outside. Their main food is a sort of giant strawberry, which gives the alien the pink color and happy mood."
                + "<br>" + "</br>";

        String alien6Text = "<b>" + name + "</b> " + "Uzapoc"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + characteristics + "</b>"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + nameOnEarth + "</b>" + "Red Devil"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + race + "</b> " + "Patarian"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + height + "</b>" + "120 - 140cm (for adult of its species)"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + adulthood + "</b>" + "under 15 years"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + planet + "</b>" + "Patarius"
                + "<br>" + "</br>"
                + "<br>" + "</br>"
                + "<b>" + "Eshu's" + story + ":" + "</b>"
                + "<br>" + "</br>"
                + "Their planet is volcanic, rocky with high temperatures. Patarians are mainly carnivors and they feel the need to hunt often. They are individualistic, polygamic and with a huge appetite for food."
                + "<br>" + "</br>"
                + "<br>" + "</br>";

        sliderItems.add(new SliderItem(alienName1, R.drawable.alien1, alien1Text));
        sliderItems.add(new SliderItem(alienName2, R.drawable.alien2, alien2Text));
        sliderItems.add(new SliderItem(alienName3, R.drawable.alien3, alien3Text));
        sliderItems.add(new SliderItem(alienName4, R.drawable.alien4, alien4Text));
        sliderItems.add(new SliderItem(alienName5, R.drawable.alien5, alien5Text));
        sliderItems.add(new SliderItem(alienName6, R.drawable.alien6, alien6Text));

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
        Intent intent = new Intent(AlienActivity.this, ComputerActivity.class);
        int image = item.getImage();
        intent.putExtra(ALIEN_KEY, image);
        intent.putExtra(ALIEN_NAME, item.getName());
        startActivity(intent);
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
        Intent intent = new Intent(AlienActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
