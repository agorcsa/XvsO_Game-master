package com.example.xvso.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.R;

import com.example.xvso.databinding.ActivitySinglePlayerBinding;
import com.example.xvso.object.Team;
import com.example.xvso.uifirebase.BaseActivity;
import com.example.xvso.uifirebase.LoginActivity;
import com.example.xvso.viewmodel.OnlineGameViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SinglePlayerActivity extends BaseActivity {

    private static final String LOG_TAG = "SinglePlayerActivity";
 
    private ActivitySinglePlayerBinding singleBinding;
    
    private OnlineGameViewModel onlineGameViewModel;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_player);
        onlineGameViewModel = ViewModelProviders.of(this).get(OnlineGameViewModel.class);
        singleBinding.setViewModel(onlineGameViewModel);
        singleBinding.setLifecycleOwner(this);
    }

    /**
     * method used for the chips to enter the board
     * @param view which represents one of the nine cells of the board
     */
    public void dropIn(View view) {

        // animate
        ImageView counter = (ImageView) view;
        counter.setTranslationY(-1000f);
        counter.animate().translationYBy(1000f).setDuration(300);

        // play
        mScoreViewModel.play(Integer.parseInt((String) view.getTag()));

        if (mScoreViewModel.checkForWin()) {
            mScoreViewModel.gameEnded();
            announceWinner();
        } else if (mScoreViewModel.fullBoard()) {
            showToast(getString(R.string.draw));
        } else {
            mScoreViewModel.togglePlayer();
        }
    }

    /**
     * announces the winner of the game (X or O) through a toast message
     */
    public void announceWinner() {

        int team = mScoreViewModel.getCurrentTeam().getTeamType();

        if (team == Team.TEAM_X) {
            showToast(getString(R.string.player_x_won));
        } else {
            showToast(getString(R.string.player_y_won));
        }
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
            mScoreViewModel.newRound();
            mScoreViewModel.togglePlayer();
        } else if (item.getItemId() == R.id.action_new_game) {
            mScoreViewModel.resetGame();
            mScoreViewModel.togglePlayer();
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

    /**
     *
     * auxiliary method which displays a toast message only by giving the message as String parameter
     * @param message
     */
    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
