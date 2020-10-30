package com.example.xvso.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.RequiresApi;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivitySinglePlayerBinding;
import com.example.xvso.viewmodel.SinglePlayerViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

public class SinglePlayerActivity extends BaseActivity {

    private static final String KEY = "key";
 
    private ActivitySinglePlayerBinding singleBinding;
    
    private SinglePlayerViewModel singlePlayerViewModel;

    private String emailLoggedUser;

    private String counterPlayerName;

    private CountDownTimer timer = null;
    private final int interval = 1000;
    private final int minute = 60000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_player);
        singlePlayerViewModel = ViewModelProviders.of(this).get(SinglePlayerViewModel.class);
        singleBinding.setViewModel(singlePlayerViewModel);
        singleBinding.setLifecycleOwner(this);

        startTimer();

        winnerIsInvisible();

        displayHostUserName();
        readFromSharedPref();

        animateViews();

        showWinningText();
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

        singleBinding.singlePlayer1Text.setVisibility(View.VISIBLE);
        singleBinding.player1ScoreTextView.setVisibility(View.VISIBLE);

        singleBinding.singlePlayer2Text.setVisibility(View.VISIBLE);
        singleBinding.player2ScoreTextView.setVisibility(View.VISIBLE);

    }

    public void readFromSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        counterPlayerName  = sharedPrefs.getString(HomeActivity.COUNTER_PLAYER_EDIT_TEXT, "");
        singleBinding.singlePlayer2Text.setText(counterPlayerName);
        singlePlayerViewModel.setGuestPlayerName(counterPlayerName);
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
                stopTimer();
                winnerIsVisible();
                singleBinding.showWinnerTextView.setText("Winner is " + convertEmailToString(emailLoggedUser) + "!");
                singleBinding.winningImageView.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(R.drawable.astronaut)
                        .into(singleBinding.winningImageView);

            } else if (game.getGameResult() == 2) {
                stopTimer();
                winnerIsVisible();
                singleBinding.showWinnerTextView.setText("Winner is " + counterPlayerName + "!");
                singleBinding.winningImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(R.drawable.ufo_black)
                        .into(singleBinding.winningImageView);

            } else if (game.getGameResult() == 3) {
                stopTimer();
                winnerIsVisible();
                singleBinding.showWinnerTextView.setText("It's a draw!");
                singleBinding.winningImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(R.drawable.draw)
                        .into(singleBinding.winningImageView);
            } else {
                singleBinding.turnSwitcherTextViewSingle.setVisibility(View.VISIBLE);
            }
        });
    }

    public void winnerIsVisible() {

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                singleBinding.turnSwitcherTextViewSingle.setVisibility(View.INVISIBLE);
                hideBoard();
                hideWinningLines();
                singleBinding.showWinnerLayout.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 1200);
    }


    public void hideWinningLines() {
        singleBinding.leftVertical.setVisibility(View.INVISIBLE);
        singleBinding.centerVertical.setVisibility(View.INVISIBLE);
        singleBinding.rightVertical.setVisibility(View.INVISIBLE);
        singleBinding.topHorizontal.setVisibility(View.INVISIBLE);
        singleBinding.centerHorizontal.setVisibility(View.INVISIBLE);
        singleBinding.bottomHorizontal.setVisibility(View.INVISIBLE);
        singleBinding.leftRightDiagonal.setVisibility(View.INVISIBLE);
        singleBinding.rightLeftDiagonal.setVisibility(View.INVISIBLE);
    }

    public void winnerIsInvisible() {
        singleBinding.showWinnerLayout.setVisibility(View.INVISIBLE);
    }

    public void onPlayAgainClick(View view) {
        startTimer();
        singlePlayerViewModel.newRound();
        singlePlayerViewModel.togglePlayer();
        winnerIsInvisible();
        showBoard();
    }

    public void onResetGameClick(View view) {
        startTimer();
        singlePlayerViewModel.resetGame();
        singlePlayerViewModel.togglePlayer();
        winnerIsInvisible();
        showBoard();
    }

    public void showBoard() {
        singleBinding.gridLayoutSinglePlayer.setVisibility(View.VISIBLE);
    }

    public void hideBoard() {
        singleBinding.gridLayoutSinglePlayer.setVisibility(View.INVISIBLE);
    }

    public void startTimer() {
        timer = new CountDownTimer(minute, interval) {
            @SuppressLint("StringFormatInvalid")
            public void onTick(long millisUntilFinished) {
                int remaining = (int) ((millisUntilFinished / 1000) % 60);
                String clock = String.format(Locale.US, "00:%02d", remaining);
                singleBinding.timerTextViewSingle.setText(clock);
            }

            public void onFinish() {
                singlePlayerViewModel.timeUp();
            }
        };
        timer.start();
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void onExitClicked(View view) {
        Intent intent = new Intent(SinglePlayerActivity.this, HomeActivity.class);
        intent.putExtra(KEY, true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
