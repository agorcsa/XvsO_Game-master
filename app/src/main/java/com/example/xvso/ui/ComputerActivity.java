package com.example.xvso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityComputerBinding;
import com.example.xvso.object.Game;
import com.example.xvso.object.User;
import com.example.xvso.uifirebase.BaseActivity;
import com.example.xvso.uifirebase.LoginActivity;
import com.example.xvso.viewmodel.ComputerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

public class ComputerActivity extends BaseActivity {

    private static final String LOG_TAG = "ComputerActivity";

    private ActivityComputerBinding computerBinding;

    private ComputerViewModel computerViewModel;

    private String emailLoggedUser;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        computerBinding = DataBindingUtil.setContentView(this, R.layout.activity_computer);
        computerViewModel = ViewModelProviders.of(this).get(ComputerViewModel.class);
        computerBinding.setViewModel(computerViewModel);
        computerBinding.setLifecycleOwner(this);

        winnerIsInvisible();

        displayHostUserName();

        setInitialVisibility();
        animateViews();

        showWinningText();

        displayScore();
    }

    public void setInitialVisibility() {
        computerBinding.block1Computer.setVisibility(View.INVISIBLE);
        computerBinding.block2Computer.setVisibility(View.INVISIBLE);
        computerBinding.block3Computer.setVisibility(View.INVISIBLE);
        computerBinding.block4Computer.setVisibility(View.INVISIBLE);
        computerBinding.block5Computer.setVisibility(View.INVISIBLE);
        computerBinding.block6Computer.setVisibility(View.INVISIBLE);
        computerBinding.block7Computer.setVisibility(View.INVISIBLE);
        computerBinding.block8Computer.setVisibility(View.INVISIBLE);
        computerBinding.block9Computer.setVisibility(View.INVISIBLE);

        computerBinding.vsImageViewComputer.setVisibility(View.VISIBLE);

        computerBinding.computerPlayer1Text.setVisibility(View.INVISIBLE);
        computerBinding.computerPlayer2Text.setVisibility(View.INVISIBLE);
    }

    public void enableCellClick() {
        computerBinding.block1Computer.setClickable(true);
        computerBinding.block2Computer.setClickable(true);
        computerBinding.block3Computer.setClickable(true);
        computerBinding.block4Computer.setClickable(true);
        computerBinding.block5Computer.setClickable(true);
        computerBinding.block6Computer.setClickable(true);
        computerBinding.block7Computer.setClickable(true);
        computerBinding.block8Computer.setClickable(true);
        computerBinding.block9Computer.setClickable(true);
    }

    public void disableCellClick() {
        computerBinding.block1Computer.setClickable(false);
        computerBinding.block2Computer.setClickable(false);
        computerBinding.block3Computer.setClickable(false);
        computerBinding.block4Computer.setClickable(false);
        computerBinding.block5Computer.setClickable(false);
        computerBinding.block6Computer.setClickable(false);
        computerBinding.block7Computer.setClickable(false);
        computerBinding.block8Computer.setClickable(false);
        computerBinding.block9Computer.setClickable(false);

    }

    public void animateViews() {
        computerBinding.vsImageViewComputer.animate().alpha(0f).setDuration(3000);

        computerBinding.block1Computer.setVisibility(View.VISIBLE);
        computerBinding.block2Computer.setVisibility(View.VISIBLE);
        computerBinding.block3Computer.setVisibility(View.VISIBLE);
        computerBinding.block4Computer.setVisibility(View.VISIBLE);
        computerBinding.block5Computer.setVisibility(View.VISIBLE);
        computerBinding.block6Computer.setVisibility(View.VISIBLE);
        computerBinding.block7Computer.setVisibility(View.VISIBLE);
        computerBinding.block8Computer.setVisibility(View.VISIBLE);
        computerBinding.block9Computer.setVisibility(View.VISIBLE);

        computerBinding.computerPlayer1Text.postDelayed(new Runnable() {
            public void run() {
                computerBinding.computerPlayer1Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        computerBinding.computerPlayer2Text.postDelayed(new Runnable() {
            public void run() {
                computerBinding.computerPlayer2Text.setVisibility(View.VISIBLE);

            }
        }, 3000);
    }

    /**
     *
     * auxiliary method which displays a toast message only by giving the message as String parameter
     * @param message
     */
    public void showToast(String message) {
        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.StyleableToast).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(ComputerActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_new_round) {
            computerViewModel.newRound();
            computerViewModel.togglePlayer();
            winnerIsInvisible();
        } else if (item.getItemId() == R.id.action_new_game) {
            computerViewModel.resetGame();
            computerViewModel.togglePlayer();
            winnerIsInvisible();
            // no need to reset the score, as boardLiveData.setValue is being called on an empty board
        } else if (item.getItemId() == R.id.action_watch_video) {
            Intent intent = new Intent(ComputerActivity.this, VideoActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_log_out) {
            showToast(getString(R.string.log_out_menu));
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(ComputerActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(ComputerActivity.this, ProfileActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void winnerIsVisible() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computerBinding.showWinnerLayout.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    public void winnerIsInvisible() {
        computerBinding.showWinnerLayout.setVisibility(View.INVISIBLE);
    }


    public void showWinningText(){
        computerViewModel.getGameLiveData().observe(this, game -> {
            if (game.getGameResult() == 1) {
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText("Winner is " + convertEmailToString(emailLoggedUser));
                game.setHostScore(game.getHostScore() + 1);
            } else if (game.getGameResult() == 2) {
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText("Winner is computer ");
                game.setGuestScore(game.getGuestScore() + 1);
            } else if (game.getGameResult() == 3) {
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText("It's a draw!");
            }
        });
    }

    public void displayHostUserName(){
        FirebaseUser user = getFirebaseUser();
        emailLoggedUser = user.getEmail();
        computerBinding.computerPlayer1Text.setText(convertEmailToString(emailLoggedUser));
        computerViewModel.setHostPlayerName(convertEmailToString(emailLoggedUser));
    }

    private String convertEmailToString(String email) {
        return email.substring(0, Objects.requireNonNull(getFirebaseUser().getEmail()).indexOf("@"));
    }

    public void onPlayAgainClick(View view) {
        computerViewModel.newRound();
        computerViewModel.togglePlayer();
        winnerIsInvisible();
    }

    public void displayScore() {
       Game game = computerViewModel.getGameLiveData().getValue();
        if (game != null) {

            User host = game.getHost();
            String hostFirstName = host.getFirstName();
            String hostName = host.getName();

            String guestName = "Computer";

            int hostScore = game.getHostScore();
            int guestScore = game.getGuestScore();

            Toast.makeText(getApplicationContext(),  hostName + " : " + hostScore
                    + guestName + " : " + guestScore
                    , Toast.LENGTH_LONG).show();

            if (TextUtils.isEmpty(hostFirstName)) {
                computerBinding.computerPlayer1Text.setText(
                        getString(R.string.player_name_score, hostName, game.getHostScore()));
            } else {
                computerBinding.computerPlayer1Text.setText(
                        getString(R.string.player_name_score, hostFirstName, game.getHostScore()));
                computerViewModel.checkForWin();
            }

            computerBinding.computerPlayer2Text.setText(
                    getString(R.string.player_name_score, guestName, game.getGuestScore()));
            computerViewModel.checkForWin();
        }
    }
}
