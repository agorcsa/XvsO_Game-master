package com.example.xvso.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityComputerBinding;
import com.example.xvso.object.Game;
import com.example.xvso.object.User;
import com.example.xvso.uifirebase.BaseActivity;
import com.example.xvso.viewmodel.ComputerViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.Objects;

public class ComputerActivity extends BaseActivity {

    private static final String LOG_TAG = "ComputerActivity";

    private ActivityComputerBinding computerBinding;

    private ComputerViewModel computerViewModel;

    private String emailLoggedUser;

    private final Handler handler = new Handler();

    private static final String KEY = "key";

    private CountDownTimer timer = null;
    private final int interval = 1000;
    private final int minute = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        computerBinding = DataBindingUtil.setContentView(this, R.layout.activity_computer);
        computerViewModel = ViewModelProviders.of(this).get(ComputerViewModel.class);
        computerBinding.setViewModel(computerViewModel);
        computerBinding.setLifecycleOwner(this);

        // hides status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hides action bar
        getSupportActionBar().hide();

        // starts the round timer
        startTimer();

        winnerIsInvisible();

        displayHostUserName();

        setInitialVisibility();
        animateViews();

        observeGame();

        initialiseScore();
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

    public void winnerIsVisible() {

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                computerBinding.turnSwitcherTextViewComputer.setVisibility(View.INVISIBLE);
                hideBoard();
                hideWinningLines();
                computerBinding.showWinnerLayout.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 1200);
    }

    public void winnerIsInvisible() {
        computerBinding.showWinnerLayout.setVisibility(View.INVISIBLE);
    }


    // updates the winner
    // updates the score
    // displays the updated score
    public void observeGame(){
        computerViewModel.getGameLiveData().observe(this, game -> {
            if (game.getGameResult() == 1) {
                stopTimer();
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText(convertEmailToString(emailLoggedUser) + " won!");
                User host = game.getHost();
                String hostName = host.getName();
                computerBinding.computerPlayer1Text.setText(
                        getString(R.string.player_name_score, hostName, game.getHostScore()));

            } else if (game.getGameResult() == 2) {
                stopTimer();
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText("Computer won!");

                String guestName = "Computer";
                computerBinding.computerPlayer2Text.setText(
                        getString(R.string.player_name_score, guestName, game.getGuestScore()));
            } else if (game.getGameResult() == 3) {
                stopTimer();
                winnerIsVisible();
                computerBinding.showWinnerTextView.setText("It's a draw!");
               // no score update
            }
            disableCellClick();
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
        startTimer();
        computerViewModel.newRound();
        computerViewModel.togglePlayer();
        winnerIsInvisible();
        showBoard();
    }

    public void onResetGameClick(View view) {
        startTimer();
        computerViewModel.resetGame();
        initialiseScore();
        computerViewModel.togglePlayer();
        winnerIsInvisible();
        showBoard();
    }

    public void initialiseScore() {
       Game game = computerViewModel.getGameLiveData().getValue();
        if (game != null) {

            User host = game.getHost();
            String hostFirstName = host.getFirstName();
            String hostName = host.getName();

            String guestName = "Computer";

            if (TextUtils.isEmpty(hostFirstName)) {
                computerBinding.computerPlayer1Text.setText(
                        getString(R.string.player_name_score, hostName, game.getHostScore()));
            } else {
                computerBinding.computerPlayer1Text.setText(
                        getString(R.string.player_name_score, hostFirstName, game.getHostScore()));
            }

            computerBinding.computerPlayer2Text.setText(
                    getString(R.string.player_name_score, guestName, game.getGuestScore()));
        }
    }

    public void showBoard() {
        computerBinding.gridLayoutComputer.setVisibility(View.VISIBLE);
    }

    public void hideBoard() {
        computerBinding.gridLayoutComputer.setVisibility(View.INVISIBLE);
    }

    public void hideWinningLines() {
        computerBinding.leftVertical.setVisibility(View.INVISIBLE);
        computerBinding.centerVertical.setVisibility(View.INVISIBLE);
        computerBinding.rightVertical.setVisibility(View.INVISIBLE);
        computerBinding.topHorizontal.setVisibility(View.INVISIBLE);
        computerBinding.centerHorizontal.setVisibility(View.INVISIBLE);
        computerBinding.bottomHorizontal.setVisibility(View.INVISIBLE);
        computerBinding.leftRightDiagonal.setVisibility(View.INVISIBLE);
        computerBinding.rightLeftDiagonal.setVisibility(View.INVISIBLE);
    }

    public void startTimer() {
        timer = new CountDownTimer(minute, interval) {
            @SuppressLint("StringFormatInvalid")
            public void onTick(long millisUntilFinished) {
                int remaining = (int) ((millisUntilFinished / 1000) % 60);
                String clock = String.format(Locale.US, "00:%02d", remaining);
                computerBinding.timerTextViewComputer.setText(clock);
            }

            public void onFinish() {
                computerViewModel.timeUp();
            }
        };
        timer.start();
    }

    public void onExitClicked(View view) {
        Intent intent = new Intent(ComputerActivity.this, HomeActivity.class);
        intent.putExtra(KEY, true);
        startActivity(intent);
    }

    public void stopTimer() {
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        // place your code as needed here
        super.onBackPressed();
    }
}
