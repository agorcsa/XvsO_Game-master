package com.example.xvso.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;

import com.example.xvso.databinding.ActivitySinglePlayerBinding;
import com.example.xvso.object.Game;
import com.example.xvso.uifirebase.BaseActivity;
import com.example.xvso.uifirebase.LoginActivity;
import com.example.xvso.viewmodel.SinglePlayerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

public class SinglePlayerActivity extends BaseActivity {

    private static final String LOG_TAG = "SinglePlayerActivity";
 
    private ActivitySinglePlayerBinding singleBinding;
    
    private SinglePlayerViewModel singlePlayerViewModel;

    private String emailLoggedUser;

    private String counterPlayerName;

    private Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_player);
        singlePlayerViewModel = ViewModelProviders.of(this).get(SinglePlayerViewModel.class);
        singleBinding.setViewModel(singlePlayerViewModel);
        singleBinding.setLifecycleOwner(this);

        singleBinding.showWinnerLayout.setVisibility(View.INVISIBLE);

        displayHostUserName();
        readFromSharedPref();

        setInitialVisibility();
        animateViews();

        showWinningText();
    }

    /**
     * creates a menu in the right-up corner of the screen
     * @param menu
     * @return the menu itself
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * menu options: newRound, resetGame, watchVideo, logOut, settings
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(SinglePlayerActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_new_round) {
            singlePlayerViewModel.newRound();
            singlePlayerViewModel.togglePlayer();
            winnerTextInvisible();
        } else if (item.getItemId() == R.id.action_new_game) {
            singlePlayerViewModel.resetGame();
            singlePlayerViewModel.togglePlayer();
            winnerTextInvisible();
            // no need to reset the score, as boardLiveData.setValue is being called on an empty board
        } else if (item.getItemId() == R.id.action_watch_video) {
            Intent intent = new Intent(SinglePlayerActivity.this, VideoActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_log_out) {
            showToast(getString(R.string.log_out_menu));
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(SinglePlayerActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(SinglePlayerActivity.this, ProfileActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setInitialVisibility() {
        singleBinding.block1Single.setVisibility(View.INVISIBLE);
        singleBinding.block2Single.setVisibility(View.INVISIBLE);
        singleBinding.block3Single.setVisibility(View.INVISIBLE);
        singleBinding.block4Single.setVisibility(View.INVISIBLE);
        singleBinding.block5Single.setVisibility(View.INVISIBLE);
        singleBinding.block6Single.setVisibility(View.INVISIBLE);
        singleBinding.block7Single.setVisibility(View.INVISIBLE);
        singleBinding.block8Single.setVisibility(View.INVISIBLE);
        singleBinding.block9Single.setVisibility(View.INVISIBLE);
        singleBinding.vsImageViewSingle.setVisibility(View.VISIBLE);
        singleBinding.singlePlayer1Text.setVisibility(View.INVISIBLE);
        singleBinding.singlePlayer2Text.setVisibility(View.INVISIBLE);
    }

    public void animateViews() {
        singleBinding.vsImageViewSingle.animate().alpha(0f).setDuration(3000);

        singleBinding.block1Single.setVisibility(View.VISIBLE);
        singleBinding.block2Single.setVisibility(View.VISIBLE);
        singleBinding.block3Single.setVisibility(View.VISIBLE);
        singleBinding.block4Single.setVisibility(View.VISIBLE);
        singleBinding.block5Single.setVisibility(View.VISIBLE);
        singleBinding.block6Single.setVisibility(View.VISIBLE);
        singleBinding.block7Single.setVisibility(View.VISIBLE);
        singleBinding.block8Single.setVisibility(View.VISIBLE);
        singleBinding.block9Single.setVisibility(View.VISIBLE);

        singleBinding.singlePlayer1Text.postDelayed(new Runnable() {
            public void run() {
                singleBinding.singlePlayer1Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        singleBinding.singlePlayer2Text.postDelayed(new Runnable() {
            public void run() {
                singleBinding.singlePlayer2Text.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    public void readFromSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        counterPlayerName  = sharedPrefs.getString(HomeActivity.COUNTER_PLAYER_EDIT_TEXT, "");
        singleBinding.singlePlayer2Text.setText(counterPlayerName);
        singlePlayerViewModel.setGuestPlayerName(counterPlayerName);
    }

    /**
     *
     * auxiliary method which displays a toast message only by giving the message as String parameter
     * @param message
     */
    public void showToast(String message) {
        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.StyleableToast).show();
    }

    public void displayHostUserName(){
        FirebaseUser user = getFirebaseUser();
        emailLoggedUser = user.getEmail();
        singleBinding.singlePlayer1Text.setText(convertEmailToString(emailLoggedUser));
        singlePlayerViewModel.setHostPlayerName(convertEmailToString(emailLoggedUser));
    }

    private String convertEmailToString(String email) {

        return email.substring(0, Objects.requireNonNull(getFirebaseUser().getEmail()).indexOf("@"));
    }

    public void showWinningText(){
        singlePlayerViewModel.getGameLiveData().observe(this, game -> {
            if (game.getGameResult() == 1) {
                singleBinding.showWinnerTextView.setVisibility(View.VISIBLE);
                singleBinding.showWinnerTextView.setText("Winner is " + convertEmailToString(emailLoggedUser));
                game.setHostScore(game.getHostScore() + 1);
            } else if (game.getGameResult() == 2) {
                singleBinding.showWinnerTextView.setVisibility(View.VISIBLE);
                singleBinding.showWinnerTextView.setText("Winner is " + counterPlayerName);
                game.setGuestScore(game.getGuestScore() + 1);
            } else if (game.getGameResult() == 3) {
                singleBinding.showWinnerTextView.setVisibility(View.VISIBLE);
                singleBinding.showWinnerTextView.setText("It's a draw!");
            }
        });
    }

    public void winnerTextVisible() {
        singleBinding.showWinnerTextView.setVisibility(View.VISIBLE);
    }

    public void winnerTextInvisible() {
        singleBinding.showWinnerTextView.setVisibility(View.INVISIBLE);
    }
}
