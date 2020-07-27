package com.example.xvso.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.xvso.R;
import com.example.xvso.object.Board;
import com.example.xvso.object.Game;
import com.example.xvso.object.User;
import com.example.xvso.adapter.GameAdapter;
import com.example.xvso.databinding.ActivityOnlineUsersBinding;
import com.example.xvso.uifirebase.BaseActivity;
import com.example.xvso.viewmodel.OnlineUsersViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OnlineUsersActivity extends BaseActivity implements GameAdapter.JoinGameClick {

    private static final String LOG_TAG = "OnlineUsersActivity";
    private static final String MULTIPLAYER = "multiplayer";
    private static final String GAME_ID = "gameId";
    private static final String GUEST = "guest";

    private static final int REQUEST_NOT_ACCEPTED = 0;
    private static final int REQUEST_ACCEPTED = 1;

    private static final String STATUS = "status";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private ActivityOnlineUsersBinding usersBinding;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    // triggered at user sign-in, sign-out, or change, or when the listener was registered
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<String> loggedUsersArrayList = new ArrayList();
    private ArrayAdapter loggedUsersArrayAdapter;
    //    private ArrayAdapter requestedUsersArrayAdapter;
    private ListView requestedUsersListView;
    private ArrayList<String> requestedUsersArrayList = new ArrayList<>();
    private TextView userIdTextView;
    private String LoginUID;
    private String LoginUserID;
    private String userName;
    private Game game = new Game();
    private OnlineUsersViewModel onlineUsersViewModel;
    private ArrayList<Game> mOpenGamesList = new ArrayList<>();
    private ArrayList<Game> mOpenGames = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private GameAdapter gameAdapter;

    private User host;

    private DatabaseReference query;

    private User currentUser = new User();
    private User myUser;

    private boolean newGame;
    private String key;

    private String guestFirstName;
    private String guestName;

    private TextView joinButton;

    private AlertDialog alertDialog;
    AlertDialog.Builder builder;

    private User guest;
    private boolean isAlertBoxShown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_users);
        onlineUsersViewModel = ViewModelProviders.of(this).get(OnlineUsersViewModel.class);

        joinButton = findViewById(R.id.join_game_text_view);

        usersBinding.setViewModel(onlineUsersViewModel);
        usersBinding.setLifecycleOwner(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        buildRecyclerView(currentUser);


        myRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);

                updateLoginUsers(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onlineUsersViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                    myUser = user;
                    if (myUser != null) {
                        buildRecyclerView(myUser);
                        readFromDatabase();
                        LoginUserID = myUser.getEmailAddress();
                        usersBinding.userLoginTextview.setText(LoginUserID);
                    }
                }
        });

        gameAdapter.notifyDataSetChanged();
    }


    public void startGame(String key) {

        Intent intent = new Intent(getApplicationContext(), OnlineGameActivity.class);

        String gameID = database.getReference("multiplayer").child(key).getKey();

        intent.putExtra(GAME_ID, gameID);

        startActivity(intent);
        finish();
    }


    public void updateLoginUsers(DataSnapshot dataSnapshot) {

        String key = "";
        Set<String> set = new HashSet<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);

            if (user != null) {
                String userEmail = user.getEmailAddress();

                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {

                    String currentUserEmail = mAuth.getCurrentUser().getEmail();

                    if (userEmail != null && currentUserEmail != null) {
                        if (!userEmail.equals(currentUserEmail)) {
                            set.add(user.getName());
                        }
                    }
                }
            }
        }
    }

    public void buildRecyclerView(User user) {

        layoutManager = new LinearLayoutManager(this);
        gameAdapter = new GameAdapter(this, mOpenGamesList, user);
        usersBinding.gamesRecyclerView.setHasFixedSize(true);
        usersBinding.gamesRecyclerView.setLayoutManager(layoutManager);
        usersBinding.gamesRecyclerView.setAdapter(gameAdapter);
    }

    public void addNewGame() {

        if (myUser != null) {
            Game game = new Game();
            game.setHost(myUser);
            game.setGuest(guest);
            game.setCurrentPlayer(myUser.getName());
            game.setBoard(new Board());
            userName = game.getHost().getUserName();
            game.setUserName(userName);
            game.setAcceptedRequest(Game.STATUS_WAITING);
            DatabaseReference newGameRef = myRef.child(MULTIPLAYER).push();
            key = newGameRef.getKey();
            game.setKey(key);
            newGameRef.setValue(game);

            if (key != null) {

                myRef.child(MULTIPLAYER).child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //startGame();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    public void readFromDatabase() {

        Query query = database.getReference(MULTIPLAYER)
                .orderByChild(STATUS)
                .equalTo(Game.STATUS_WAITING);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mOpenGamesList.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Game game = item.getValue(Game.class);
                    User host = game.getHost();
                    if (host != null) {
                        if (!host.getName().equals(myUser.getName())) {
                            mOpenGamesList.add(game);
                        }
                        String uidHost = host.getUID();
                        if (myUser != null) {
                            String UID = myUser.getUID();
                            // makes sure that the host can add only one game at a time
                            if (UID.equals(uidHost)) {
                                newGame = true;
                                key = item.getKey();
                                opponentJoinedGame(key);
                            }
                        }
                    }
                }

                if (!newGame) {
                    addNewGame();
                }

                if (newGame) {
                    opponentJoinedGame(key);
                }

                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public boolean userNameCheck(Boolean b) {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            LoginUserID = user.getEmail();

            b = userName.contains(".");

        }
        return b;
    }


    public boolean opponentJoinedGame(String key) {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("multiplayer").child(key).child("guest");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                guest = dataSnapshot.getValue(User.class);

                if (guest != null) {

                    String guestUID = guest.getUID();

                    if (!TextUtils.isEmpty(guestUID)) {
                        // Perhaps checking that the guest does not correspond to our currently logged in user?
                        if (!guest.getUID().equals(myUser.getUID())) {
                            if (!isAlertBoxShown) {
                                showAlert(key);
                                isAlertBoxShown = true;
                            }
                        }
                    } else {
                        game.setStatus(Game.STATUS_WAITING);
                        database.getReference("multiplayer").child(key).child("status").setValue(Game.STATUS_WAITING);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public void onJoinGameClick(String key, View view) {

        database.getReference("multiplayer").child(key).child("guest").setValue(myUser);

        DatabaseReference ref = database.getReference(MULTIPLAYER).child(key).child("acceptedRequest");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    Integer isRequestAccepted = dataSnapshot.getValue(Integer.class);

                    if (isRequestAccepted == REQUEST_ACCEPTED) {
                        startGame(key);
                        game.setStatus(Game.STATUS_PLAYING);
                        database.getReference("multiplayer").child(key).child("status").setValue(Game.STATUS_PLAYING);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        view.setClickable(false);
        view.invalidate();
    }

    public String getGuestName(User user) {
        if (TextUtils.isEmpty(user.getFirstName())) {
            return user.getName();
        } else {
            return user.getFirstName();
        }
    }

    // shows AlertDialog
    // when guest sends a request to the host
    public void showAlert(String key) {

        DatabaseReference ref = database.getReference(MULTIPLAYER).child(key).child("guest");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guest = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!isFinishing()) {
            new MaterialDialog.Builder(this)
                    .icon(getResources().getDrawable(R.drawable.ic_cross, null))
                    .limitIconToDefaultSize()
                    .title(R.string.alert_dialog_title)
                    .content(getString(R.string.alert_dialog_content, getGuestName(guest)))
                    .positiveText(R.string.alert_dialog_yes)
                    .negativeText(R.string.alert_dialog_no)
                    .theme(Theme.DARK)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            // updates the acceptedRequest variable in the Firebase database
                            database.getReference("multiplayer").child(key).child("acceptedRequest").setValue(REQUEST_ACCEPTED);
                            startGame(key);
                            /// --> here is the problem
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            game.setStatus(Game.STATUS_WAITING);
                            database.getReference("multiplayer").child(key).child("status").setValue(Game.STATUS_WAITING);
                        }
                    })
                    .show();
        }
    }
}
