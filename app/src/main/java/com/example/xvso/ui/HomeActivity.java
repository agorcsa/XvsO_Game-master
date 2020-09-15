package com.example.xvso.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityHomeBinding;
import com.example.xvso.uifirebase.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static com.example.xvso.R.drawable.nightsky;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final String KEY = "key";
    public static final String COUNTER_PLAYER_EDIT_TEXT = "CounterPlayer EditText";

    private MediaPlayer mediaPlayer;
    public final static String SWITCH_VALUE_SOUND = "switch_value_sound";
    public final static String SWITCH_VALUE_MUSIC = "switch_value_music";
    public final static String SWITCH_VALUE_MODE = "switch_value_mode";

    private ActivityHomeBinding homeBinding;

    private EditText dialogEditText;

    public static final String STATUS = "status";
    private boolean isSoundOn = true;
    private boolean isMusicOn = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        configureActionBar();
        playMusic();
        //playSound();

        homeBinding.singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                createAlertDialogSingle();
            }
        });

        homeBinding.computerPlayerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AlienActivity.class);
                startActivity(intent);
            }
        });

        homeBinding.multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, OnlineUsersActivity.class);
                startActivity(intent);
            }
        });

        homeBinding.aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    public void configureActionBar() {
        Drawable drawable = getResources().getDrawable(nightsky);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(drawable);
        getSupportActionBar().setTitle("");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAlertDialogSingle() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogStyle);
        alertDialog.setTitle("Player 0");
        alertDialog.setMessage("Enter counter player name");

        dialogEditText = new EditText(HomeActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        dialogEditText.setLayoutParams(lp);
        dialogEditText.setTextColor(getColor(R.color.colorPrimary));

        dialogEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(8)
        });

        alertDialog.setView(dialogEditText);
        alertDialog.setIcon(R.drawable.ic_cross);

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String namePlayer0 = dialogEditText.getText().toString();
                        writeGuestNameToSharedPrefs(namePlayer0);
                        if (!namePlayer0.isEmpty()) {
                            //showToast("Game will start against " + namePlayer0);
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

    public void writeGuestNameToSharedPrefs(String string) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COUNTER_PLAYER_EDIT_TEXT, string);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() == null || !getIntent().hasExtra(KEY)) {
            animateRocket();
        } else {
            homeBinding.motionLayout.setProgress(100f);
        }
    }

    public void animateRocket() {
        homeBinding.motionLayout.transitionToEnd();
        boolean isRocketAnimated = true;
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
        } else if (item.getItemId() == R.id.action_profile_home) {
            Intent settingsIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(settingsIntent);
        } else if (item.getItemId() == R.id.action_music_home) {
            // toggle button (stop music)
            if (isMusicOn) {
                item.setIcon(R.drawable.music_off);
                isMusicOn = false;
                saveToSharedPrefs(isMusicOn);
            } else {
                item.setIcon(R.drawable.music_on);
                isMusicOn = true;
                saveToSharedPrefs(isMusicOn);
            }
        } else if (item.getItemId() == R.id.action_sound_home) {
            // toggle button (stop sound)
            if (isSoundOn) {
                item.setIcon(R.drawable.volume_off);
                isSoundOn = false;
                saveToSharedPrefs(isSoundOn);
            } else {
                item.setIcon(R.drawable.volume_on);
                isSoundOn = true;
                saveToSharedPrefs(isSoundOn);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveToSharedPrefs(boolean b) {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(STATUS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY, b);
        editor.apply();
    }


    public void playSound() {
        SharedPreferences sharedPref = getSharedPreferences(STATUS, Context.MODE_PRIVATE);
        boolean value = sharedPref.getBoolean(KEY, false);
        if (value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer = MediaPlayer.create(this, R.raw.orbit);
                mediaPlayer.start();
            }
        }
    }

    public void playMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.orbitbeat);
        mediaPlayer.start();
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }
}

