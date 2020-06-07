package com.example.xvso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        hideHomeViews();

        animateRocket();

        showHomeViews();

        welcomeBinding.singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opens SinglePlayer Activity
                startSinglePlayer(view);
                createAlertDialog();
            }
        });

        welcomeBinding.computerPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startComputerActivity(view);
            }
        });

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

    public void startComputerActivity(View view) {
        Intent intent = new Intent(HomeActivity.this, ComputerActivity.class);
        startActivity(intent);
    }


    public void startGameOnline(View view) {
        // opens OnlineUsers Activity
        Intent intent = new Intent(this, OnlineUsersActivity.class);
        startActivity(intent);
    }

    public void startSinglePlayer(View view) {
        // opens SinglePlayer Activity
        Intent intent = new Intent(HomeActivity.this, SinglePlayerActivity.class);
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

    public void hideHomeViews() {
        welcomeBinding.appTitle.setVisibility(View.INVISIBLE);
        welcomeBinding.singlePlayerButton.setVisibility(View.INVISIBLE);
        welcomeBinding.computerPlayerButton.setVisibility(View.INVISIBLE);
        welcomeBinding.multiPlayerButton.setVisibility(View.INVISIBLE);
        welcomeBinding.aboutButton.setVisibility(View.INVISIBLE);
    }

    public void showHomeViews() {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                welcomeBinding.appTitle.setVisibility(View.VISIBLE);
                welcomeBinding.singlePlayerButton.setVisibility(View.VISIBLE);
                welcomeBinding.computerPlayerButton.setVisibility(View.VISIBLE);
                welcomeBinding.multiPlayerButton.setVisibility(View.VISIBLE);
                welcomeBinding.aboutButton.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    public void animateRocket() {
        welcomeBinding.rocketImageView.animate().translationYBy(-2000).setDuration(3000);
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_custom_view,null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        Button btn_positive = dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = dialogView.findViewById(R.id.dialog_negative_btn);
        final EditText et_name = dialogView.findViewById(R.id.et_name);

        final AlertDialog dialog = builder.create();

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();
                String name = et_name.getText().toString();
                Toast.makeText(getApplication(),
                        "Submitted name : " + name, Toast.LENGTH_SHORT).show();
                // Say hello to the submitter
                //tv_message.setText("Hello " + name + "!");
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss/cancel the alert dialog
                //dialog.cancel();
                dialog.dismiss();
                Toast.makeText(getApplication(),
                        "No button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Display the custom alert dialog on interface
        dialog.show();
    }
}

