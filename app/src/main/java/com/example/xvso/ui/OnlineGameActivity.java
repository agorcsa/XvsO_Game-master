package com.example.xvso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;
import com.example.xvso.object.Game;
import com.example.xvso.object.User;
import com.example.xvso.databinding.ActivityOnlineGameBinding;
import com.example.xvso.uifirebase.LoginActivity;
import com.example.xvso.viewmodel.OnlineGameViewModel;
import com.example.xvso.viewmodel.OnlineUsersViewModelFactory;
import com.example.xvso.widget.Xvs0WidgetPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OnlineGameActivity extends AppCompatActivity {

    private static final String LOG_TAG = "OnlineGameActivity";
    private static final String GAME_ID = "gameId";
    private static final String GUEST = "guest";

    public static final String ACTION_DATA_UPDATED ="com.example.xvso.ACTION_DATA_UPDATED";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    int activePlayer = 1;
    ArrayList<Integer> player1 = new ArrayList<>();
    ArrayList<Integer> player2 = new ArrayList<>();
    private OnlineGameViewModel onlineGameViewModel;
    private ActivityOnlineGameBinding onlineGameBinding;
    // current player user name
    private String userName = "";
    // other player user name
    private String opponentFirstName = "";
    private String gameId = "";
    private String requestType = "";
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

    private Xvs0WidgetPreferences widgetPreferences = new Xvs0WidgetPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTimer();

        widgetPreferences.resetData(this);
        widgetPreferences.updateWidgets(getApplicationContext());

        if (getIntent().getExtras() != null) {

            gameId = Objects.requireNonNull(getIntent().getExtras().get(GAME_ID)).toString();

            onlineGameViewModel = ViewModelProviders.of(this, new OnlineUsersViewModelFactory(gameId)).get(OnlineGameViewModel.class);
            onlineGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_game);
            onlineGameBinding.setViewModel(onlineGameViewModel);
            onlineGameBinding.setLifecycleOwner(this);

            setInitialVisibility();
            animateViews();

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
                                showToast(getString(R.string.has_won, game.getHost().getName()));
                                timer.cancel();
                                widgetPreferences.saveData(OnlineGameActivity.this, game);
                                widgetPreferences.updateWidgets(getApplicationContext());
                                break;
                            case 2:
                                showToast(getString(R.string.has_won, game.getGuest().getName()));
                                timer.cancel();
                                widgetPreferences.saveData(OnlineGameActivity.this, game);
                                widgetPreferences.updateWidgets(getApplicationContext());
                                break;
                            case 3:
                                showToast("It's a draw!");
                                timer.cancel();
                                break;
                        }
                        host = game.getHost();
                        guest = game.getGuest();
                        hostUID = host.getUID();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String firebaseUID = firebaseUser.getUid();
                        hostFirstName = host.getFirstName();
                        hostName = host.getName();
                        guestFirstName = guest.getFirstName();
                        guestName = guest.getName();
                        if (TextUtils.isEmpty(hostFirstName)) {
                            onlineGameBinding.player1Text.setText(
                                    getString(R.string.player_name_score, hostName, game.getHostScore()));
                        } else {
                            onlineGameBinding.player1Text.setText(
                                    getString(R.string.player_name_score, hostFirstName, game.getHostScore()));
                        }
                        if (TextUtils.isEmpty(guestFirstName)) {
                            onlineGameBinding.player2Text.setText(
                                    getString(R.string.player_name_score, guestName, game.getGuestScore()));
                        } else {
                            onlineGameBinding.player2Text.setText(
                                    getString(R.string.player_name_score, guestFirstName, game.getGuestScore()));
                        }
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
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = (String) dataSnapshot.getValue();
                    if (value.equals(userName)) {

                        onlineGameBinding.player1Text.setText(getResources().getString(R.string.turn_order));
                        onlineGameBinding.player2Text.setText(getResources().getString(R.string.turn_order));
                        setEnableClick(true);
                        activePlayer = 1;
                    } else if (value.equals(opponentFirstName)) {

                        onlineGameBinding.player1Text.setText(getString(R.string.players_turn, opponentFirstName));
                        onlineGameBinding.player2Text.setText(getString(R.string.players_turn, opponentFirstName));
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

        onlineGameBinding.player1Text.setVisibility(View.INVISIBLE);
        onlineGameBinding.player2Text.setVisibility(View.INVISIBLE);
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

        onlineGameBinding.player1Text.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player1Text.setVisibility(View.VISIBLE);
            }
        }, 3000);

        onlineGameBinding.player2Text.postDelayed(new Runnable() {
            public void run() {
                onlineGameBinding.player2Text.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     * creates a menu in the right-up corner of the screen
     *
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
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(OnlineGameActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_new_round) {
            onlineGameViewModel.newRound();
        } else if (item.getItemId() == R.id.action_new_game) {
            onlineGameViewModel.resetGame();
            widgetPreferences.resetData(OnlineGameActivity.this);
            widgetPreferences.updateWidgets(OnlineGameActivity.this);
        } else if (item.getItemId() == R.id.action_watch_video) {
            Intent intent = new Intent(OnlineGameActivity.this, VideoActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_log_out) {
            showToast(getString(R.string.log_out_menu));
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(OnlineGameActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(OnlineGameActivity.this, ProfileActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showToast(String message) {

        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.StyleableToast).show();
    }

    public void startTimer() {
        timer = new CountDownTimer(minute, interval) {
            public void onTick(long millisUntilFinished) {
                onlineGameBinding.timerTextView.setText(getString(R.string.time_left, millisUntilFinished / 1000));
            }

            public void onFinish() {
                onlineGameBinding.timerTextView.setText(getResources().getString(R.string.game_over));
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
}
