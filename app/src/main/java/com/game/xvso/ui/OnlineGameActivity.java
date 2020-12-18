package com.game.xvso.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.game.xvso.R;
import com.game.xvso.object.Game;
import com.game.xvso.object.User;
import com.game.xvso.databinding.ActivityOnlineGameBinding;

import com.game.xvso.viewmodel.OnlineGameViewModel;
import com.game.xvso.viewmodel.OnlineUsersViewModelFactory;
import com.game.xvso.widget.Xvs0WidgetPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class OnlineGameActivity extends BaseActivity {

    private static final String LOG_TAG = "OnlineGameActivity";
    private static final String GAME_ID = "gameId";
    private static final String GUEST = "guest";

    public static final String ACTION_DATA_UPDATED ="com.example.xvso.ACTION_DATA_UPDATED";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    int activePlayer = 1;
    private OnlineGameViewModel onlineGameViewModel;
    private ActivityOnlineGameBinding onlineGameBinding;
    // current player user name
    private String userName = "";
    // other player user name
    private String opponentFirstName = "";
    private String gameId = "";
    // current user is signed in with X
    private int gameState = 0;
    private User host;
    private User guest;
    private String hostUID = "";
    private String hostFirstName = "";
    private String hostName = "";
    private String guestFirstName = "";
    private String guestName = "";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private CountDownTimer timer = null;
    private final int interval = 1000;
    private final int minute = 60000;

    private String emailLoggedUser;

    private Xvs0WidgetPreferences widgetPreferences = new Xvs0WidgetPreferences();

    private static final String KEY = "key";

    MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int sound1, sound2, sound3, sound4, sound5, sound6;

    private static final String USER_EXIT = "user_exit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTimer();

        setupPositiveSound();
        setupNegativeSound();

        widgetPreferences.resetData(this);
        widgetPreferences.updateWidgets(getApplicationContext());


        SharedPreferences sharedPref = getSharedPreferences("switch_status", Context.MODE_PRIVATE);
        boolean value = sharedPref.getBoolean("switch_value", false);
        if (value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                soundPool = new SoundPool.Builder()
                        .setMaxStreams(2)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else {
                soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            }
            sound2 = soundPool.load(this, R.raw.orbit, 1); // load a sound into SoundPool, but doesn't play it
            sound3 = soundPool.load(this, R.raw.orbitbeat, 2);
            sound4 = soundPool.load(this, R.raw.spaceloop, 3);
            sound5 = soundPool.load(this, R.raw.moongarden, 4);

            mediaPlayer = MediaPlayer.create(this, R.raw.orbitbeat);
            mediaPlayer.start(); // play background music
        }

        if (getIntent().getExtras() != null) {

            gameId = Objects.requireNonNull(getIntent().getExtras().get(GAME_ID)).toString();

            onlineGameViewModel = ViewModelProviders.of(this, new OnlineUsersViewModelFactory(gameId)).get(OnlineGameViewModel.class);
            onlineGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_game);
            onlineGameBinding.setViewModel(onlineGameViewModel);
            onlineGameBinding.setLifecycleOwner(this);

            winnerIsInvisible();

            displayHostUserName();
            setInitialVisibility();
            animateViews();

            showWinningText();

            reference.child("multiplayer").child(gameId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Game game = dataSnapshot.getValue(Game.class);
                    if (game != null) {
                        assert game != null;
                        int gameResult = game.getGameResult();
                        switch (gameResult) {
                            case 0:
                                //restartTimer();
                                // widgetPreferences.saveData(OnlineGameActivity.this, game);
                                // widgetPreferences.updateWidgets(getApplicationContext());
                                break;
                            case 1:
                                timer.cancel();
                                widgetPreferences.saveData(OnlineGameActivity.this, game);
                                widgetPreferences.updateWidgets(getApplicationContext());
                                break;
                            case 2:
                                timer.cancel();
                                widgetPreferences.saveData(OnlineGameActivity.this, game);
                                widgetPreferences.updateWidgets(getApplicationContext());
                                break;
                            case 3:
                                timer.cancel();
                                break;
                        }

                        host = game.getHost();
                        guest = game.getGuest();
                        hostUID = host.getUID();
                        hostFirstName = host.getFirstName();
                        hostName = host.getName();
                        guestFirstName = guest.getFirstName();
                        guestName = guest.getName();

                        if (TextUtils.isEmpty(hostFirstName)) {
                            onlineGameBinding.onlinePlayer1Text.setText(hostName);
                        } else {
                            onlineGameBinding.onlinePlayer1Text.setText(hostFirstName);
                        }

                        onlineGameBinding.onlinePlayer1Score.setText(String.valueOf(game.getHostScore()));

                        if (TextUtils.isEmpty(guestFirstName)) {
                            onlineGameBinding.onlinePlayer2Text.setText(guestName);
                        } else {
                            onlineGameBinding.onlinePlayer2Text.setText(guestFirstName);
                        }
                        onlineGameBinding.onlinePlayer2Score.setText(String.valueOf(game.getGuestScore()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        reference.child("multiplayer").child(gameId).child("roundCount").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              restartTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gameState = 1;

        reference.child("playing").child(gameId).child("turn").addValueEventListener(new ValueEventListener() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = (String) dataSnapshot.getValue();
                    if (value.equals(userName)) {

                        onlineGameBinding.onlinePlayer1Text.setText(getResources().getString(R.string.turn_order));
                        onlineGameBinding.onlinePlayer2Text.setText(getResources().getString(R.string.turn_order));
                        setEnableClick(true);
                        activePlayer = 1;

                    } else if (value.equals(opponentFirstName)) {

                        onlineGameBinding.onlinePlayer1Text.setText(getString(R.string.players_turn, opponentFirstName));
                        onlineGameBinding.onlinePlayer2Text.setText(getString(R.string.players_turn, opponentFirstName));
                        setEnableClick(false);
                        activePlayer = 2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("playing").child(gameId).child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    activePlayer = 2;
                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        String value = "";
                        String firstPlayer = userName;
                        for (String key : map.keySet()) {
                            value = (String) map.get(key);
                            if (value.equals(userName)) {
                                activePlayer = 2;
                            } else {
                                activePlayer = 1;
                            }
                            firstPlayer = value;
                            String[] splitID = key.split(":");
                            // TO DO
                            //otherPlayer.(Integer.parseInt(splitID[1]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setEnableClick(boolean b) {

        onlineGameBinding.block1.setClickable(b);
        onlineGameBinding.block2.setClickable(b);
        onlineGameBinding.block3.setClickable(b);

        onlineGameBinding.block4.setClickable(b);
        onlineGameBinding.block5.setClickable(b);
        onlineGameBinding.block6.setClickable(b);

        onlineGameBinding.block7.setClickable(b);
        onlineGameBinding.block8.setClickable(b);
        onlineGameBinding.block9.setClickable(b);
    }


    public void setInitialVisibility() {
        onlineGameBinding.block1.setVisibility(View.INVISIBLE);
        onlineGameBinding.block2.setVisibility(View.INVISIBLE);
        onlineGameBinding.block3.setVisibility(View.INVISIBLE);
        onlineGameBinding.block4.setVisibility(View.INVISIBLE);
        onlineGameBinding.block5.setVisibility(View.INVISIBLE);
        onlineGameBinding.block6.setVisibility(View.INVISIBLE);
        onlineGameBinding.block7.setVisibility(View.INVISIBLE);
        onlineGameBinding.block8.setVisibility(View.INVISIBLE);
        onlineGameBinding.block9.setVisibility(View.INVISIBLE);

        onlineGameBinding.vsImageView.setVisibility(View.VISIBLE);

        onlineGameBinding.onlinePlayer1Text.setVisibility(View.INVISIBLE);
        onlineGameBinding.onlinePlayer2Text.setVisibility(View.INVISIBLE);
    }

    public void animateViews() {
        onlineGameBinding.vsImageView.animate().alpha(0f).setDuration(3000);

        onlineGameBinding.block1.setVisibility(View.VISIBLE);
        onlineGameBinding.block2.setVisibility(View.VISIBLE);
        onlineGameBinding.block3.setVisibility(View.VISIBLE);
        onlineGameBinding.block4.setVisibility(View.VISIBLE);
        onlineGameBinding.block5.setVisibility(View.VISIBLE);
        onlineGameBinding.block6.setVisibility(View.VISIBLE);
        onlineGameBinding.block7.setVisibility(View.VISIBLE);
        onlineGameBinding.block8.setVisibility(View.VISIBLE);
        onlineGameBinding.block9.setVisibility(View.VISIBLE);

        onlineGameBinding.onlinePlayer1Text.setVisibility(View.VISIBLE);
        onlineGameBinding.onlinePlayer2Text.setVisibility(View.VISIBLE);
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void startTimer() {
        timer = new CountDownTimer(minute, interval) {
            @SuppressLint("StringFormatInvalid")
            public void onTick(long millisUntilFinished) {
                int remaining = (int) ((millisUntilFinished / 1000) % 60);
                String clock = String.format(Locale.US, "00:%02d", remaining);
                onlineGameBinding.timerTextViewOnline.setText(clock);
            }

            public void onFinish() {
                onlineGameBinding.timerTextViewOnline.setText(getResources().getString(R.string.game_over));
                onlineGameViewModel.timeUp();
            }
        };
        timer.start();
    }

    public void restartTimer(){
        if (timer != null) {
            timer.cancel();
        }
        startTimer();
    }

    public void hideWinningLines() {
        onlineGameBinding.leftVertical.setVisibility(View.INVISIBLE);
        onlineGameBinding.centerVertical.setVisibility(View.INVISIBLE);
        onlineGameBinding.rightVertical.setVisibility(View.INVISIBLE);
        onlineGameBinding.topHorizontal.setVisibility(View.INVISIBLE);
        onlineGameBinding.centerHorizontal.setVisibility(View.INVISIBLE);
        onlineGameBinding.bottomHorizontal.setVisibility(View.INVISIBLE);
        onlineGameBinding.leftRightDiagonal.setVisibility(View.INVISIBLE);
        onlineGameBinding.rightLeftDiagonal.setVisibility(View.INVISIBLE);
    }

    public void displayHostUserName() {
        FirebaseUser user = getFirebaseUser();
        emailLoggedUser = user.getEmail();
        onlineGameBinding.onlinePlayer1Text.setText(convertEmailToString(emailLoggedUser));
    }

    // shows winning text according to the gameResult
    public void showWinningText() {
        onlineGameViewModel.gameLiveData.observe(this, game -> {
            if (game.getStatus() == Game.STATUS_ROUND_FINISHED) {
                if (game.getGameResult() == 1) {
                    winnerIsVisible();
                    onlineGameBinding.winningImageView.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(R.drawable.astronaut)
                            .into(onlineGameBinding.winningImageView);
                    onlineGameBinding.showWinnerTextView.setText("Winner is " + game.getHost().getName() + " !");
                } else if (game.getGameResult() == 2) {
                    winnerIsVisible();
                    onlineGameBinding.winningImageView.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(R.drawable.ufo_black)
                            .into(onlineGameBinding.winningImageView);
                    onlineGameBinding.showWinnerTextView.setText("Winner is " + game.getGuest().getName() + " !");
                } else if (game.getGameResult() == 3) {
                    winnerIsVisible();
                    onlineGameBinding.winningImageView.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(R.drawable.draw)
                            .into(onlineGameBinding.winningImageView);
                    onlineGameBinding.showWinnerTextView.setText("It's a draw!");
                }
            }

            // runs when the EXIT button is clicked
            if (game.getStatus() == Game.STATUS_USER_EXIT) {

                // Starts the OnlineUsersActivity  with 2 seconds delay
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {

                        if (isSoundOn) {
                            negativeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    showToast("Your counterpart has left the game!");
                                    Intent intent = new Intent(OnlineGameActivity.this, OnlineUsersActivity.class);
                                    intent.putExtra(KEY, true);
                                    startActivity(intent);
                                }
                            });
                            negativeSound.start();
                        }
                    }
                };
                handler.postDelayed(r, 2000);
            }

            if (game.getStatus() == Game.STATUS_PLAY_AGAIN) {

                if (isSoundOn) {
                    positiveSound.start();
                }

                //showToast("A new round will start!");
                onlineGameViewModel.newRound();
                onlineGameViewModel.togglePlayer();
                winnerIsInvisible();
                showBoard();
            }

            if (game.getStatus() == Game.STATUS_GAME_RESET) {
                if (isSoundOn) {
                    positiveSound.start();
                }
                //showToast("A new game will start!");
                // This will cause an infinite loop as will cause the observer to be called again and again
                //onlineGameViewModel.resetGame();
                onlineGameViewModel.resetScore();
                onlineGameViewModel.togglePlayer();
                winnerIsInvisible();
                showBoard();
            }
        });
    }


    private String convertEmailToString(String email) {
        return email.substring(0, Objects.requireNonNull(getFirebaseUser().getEmail()).indexOf("@"));
    }

    public void winnerIsVisible() {

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                onlineGameBinding.turnSwitcherTextView.setVisibility(View.INVISIBLE);
                hideBoard();
                hideWinningLines();
                // onlineGameBinding.showWinnerLayout.setVisibility(View.VISIBLE);
                onlineGameBinding.showWinnerLayout.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 2000);
    }

    public void winnerIsInvisible() {

        onlineGameBinding.showWinnerLayout.setVisibility(View.INVISIBLE);
    }

    // not needed

  /*  public void onPlayAgainClick(View view) {
        onlineGameViewModel.newRound();
        onlineGameViewModel.togglePlayer();
        winnerIsInvisible();
        showBoard();
    }*/

   /* public void onResetGameClick(View view) {
        if (isSoundOn) {
            positiveSound.start();
        }
        startTimer();
        onlineGameViewModel.resetGame();
        onlineGameViewModel.togglePlayer();
        // change game status
        winnerIsInvisible();
        showBoard();
    }*/


    public void showBoard() {
        onlineGameBinding.gridLayout.setVisibility(View.VISIBLE);
    }

    public void hideBoard() {
        onlineGameBinding.gridLayout.setVisibility(View.INVISIBLE);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        if (soundPool != null) soundPool.release();
    }

    public void onExitClicked(View view) {
        if (isSoundOn) {
            negativeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Intent intent = new Intent(OnlineGameActivity.this, HomeActivity.class);
                    intent.putExtra(KEY, true);
                    startActivity(intent);
                    onlineGameViewModel.onGameExitAbruptly();
                }
            });
            negativeSound.start();
        } else {
            Intent intent = new Intent(OnlineGameActivity.this, HomeActivity.class);
            intent.putExtra(KEY, true);
            startActivity(intent);
            onlineGameViewModel.onGameExitAbruptly();
        }
    }

    @Override
    public void onBackPressed() {
        // place your code as needed here
        onlineGameViewModel.exitGame();
        super.onBackPressed();
    }
}