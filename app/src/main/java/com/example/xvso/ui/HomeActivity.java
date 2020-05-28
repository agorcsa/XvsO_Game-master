package com.example.xvso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityWelcomeBinding;
import com.example.xvso.uifirebase.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Welcome Screen";

    private ActivityWelcomeBinding welcomeBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        welcomeBinding.multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // assign a multi-player game session
                startGameOnline(view);
            }
        });

        welcomeBinding.aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opens About Activity
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startGameOnline(View view) {
        Intent intent = new Intent(this, OnlineUsersActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_log_out_home) {
            showToast(getString(R.string.log_out_menu));
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

