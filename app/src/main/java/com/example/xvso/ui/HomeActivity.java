package com.example.xvso.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.xvso.R;
import com.example.xvso.databinding.ActivityWelcomeBinding;
import com.example.xvso.object.Game;
import com.example.xvso.uifirebase.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Welcome Screen";
    public static final String COUNTER_PLAYER_EDIT_TEXT = "CounterPlayer EditText";

    private ActivityWelcomeBinding welcomeBinding;

    private EditText editText;

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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Player 0");
        alertDialog.setMessage("Enter counter player name");

        editText = new EditText(HomeActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_cross);

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String namePlayer0 = editText.getText().toString();
                            if (!namePlayer0.isEmpty()) {
                                showToast("Game will start against " + namePlayer0);
                                writeToSharedPref();
                                Intent intent = new Intent(HomeActivity.this, SinglePlayerActivity.class);
                                startActivity(intent);
                            } else {
                                showToast("Game can not start without introducing Player0's name!");
                            }
                        }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("You have chosen to not start the game!");
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

public void writeToSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(COUNTER_PLAYER_EDIT_TEXT, editText.getText().toString());
        editor.apply();
    }
}

