package com.example.xvso.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityAboutBinding;
import com.google.android.libraries.places.api.model.PhotoMetadata;
// Add an import statement for the client library.
import com.google.android.libraries.places.api.Places;


public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding aboutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

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

    public void displayPhotoAttributions() {
        // attribution links

        // ufo image
        //<a href='https://pngtree.com/so/world-space-day-illustration'>world-space-day-illustration png from pngtree.com</a>

        // green alien
        //<a href='https://pngtree.com/so/cartoon'>cartoon png from pngtree.com</a>

        // Get the photo metadata from the Place object.
        //PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

        // Get the attribution text.
        // String attributions = photoMetadata.getAttributions();
    }
}
