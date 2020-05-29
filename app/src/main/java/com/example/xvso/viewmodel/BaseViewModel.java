package com.example.xvso.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.xvso.deserializer.Deserializer;
import com.example.xvso.object.Board;
import com.example.xvso.object.Game;
import com.example.xvso.object.Team;
import com.example.xvso.object.User;
import com.example.xvso.firebaseutils.FirebaseQueryLiveData;
import com.example.xvso.object.WinningLines;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseViewModel extends ViewModel {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public final MutableLiveData<Boolean> topHorizontalLine = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> centerHorizontal = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> bottomHorizontal = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> leftVertical = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> centerVertical = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> rightVertical = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> leftRightDiagonal = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> rightLeftDiagonal = new MutableLiveData<>(false);

    private MutableLiveData<Team> teamX = new MutableLiveData<>();
    private MutableLiveData<Team> teamO = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsGameInProgress() {
        return isGameInProgress;
    }

    private void setIsGameInProgress(MutableLiveData<Boolean> isGameInProgress) {
        this.isGameInProgress = isGameInProgress;
    }

    public MutableLiveData<Boolean> isGameInProgress = new MutableLiveData<>(true);

    private Team currentTeam;
    // represents the tag of each cell of the grid
    // (0, 1, 2)
    // (3, 4, 5)
    // (6, 7, 8)
    private int tag;
    private String displayName;
    private ArrayList<Integer> board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

    private MutableLiveData<ArrayList<Integer>> boardLiveData = new MutableLiveData<>();

    private boolean gameOver;
    private DatabaseReference query;
    private LiveData<User> userLiveData;
    private FirebaseAuth auth;

    private Game game = new Game();
    // constructor
    // will be called when MainActivity starts
    @RequiresApi(api = Build.VERSION_CODES.N)
    public BaseViewModel() {

        teamX.setValue(new Team(Team.TEAM_X));
        teamO.setValue(new Team(Team.TEAM_O));
        currentTeam = teamX.getValue();

        auth = FirebaseAuth.getInstance();

        if (auth.getUid() != null) {

            query = FirebaseDatabase.getInstance().getReference("users").child((auth.getUid()));

            FirebaseQueryLiveData resultLiveData = new FirebaseQueryLiveData(query);
            userLiveData = Transformations.map(resultLiveData, new Deserializer());
        }
    }

    public ArrayList<Integer> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<Integer> board) {
        this.board = board;
    }

    public MutableLiveData<Team> getTeamX() {
        return teamX;
    }

    public void setTeamX(MutableLiveData<Team> teamX) {
        this.teamX = teamX;
    }

    public MutableLiveData<Team> getTeamO() {
        return teamO;
    }

    public void setTeamO(MutableLiveData<Team> teamO) {
        this.teamO = teamO;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public MutableLiveData<Boolean> getTopHorizontalLine() {
        return topHorizontalLine;
    }

    public MutableLiveData<Boolean> getCenterHorizontal() {
        return centerHorizontal;
    }

    public MutableLiveData<Boolean> getBottomHorizontal() {
        return bottomHorizontal;
    }

    public MutableLiveData<Boolean> getLeftVertical() {
        return leftVertical;
    }

    public MutableLiveData<Boolean> getCenterVertical() {
        return centerVertical;
    }

    public MutableLiveData<Boolean> getRightVertical() {
        return rightVertical;
    }

    public MutableLiveData<Boolean> getLeftRightDiagonal() {
        return leftRightDiagonal;
    }

    public MutableLiveData<Boolean> getRightLeftDiagonal() {
        return rightLeftDiagonal;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void togglePlayer() {
        if (currentTeam == teamO.getValue()) {
            currentTeam = teamX.getValue();
        } else {
            currentTeam = teamO.getValue();
        }
    }

    public boolean checkRows() {

        int team = currentTeam.getTeamType();

        if (board.get(0) == team && board.get(1) == team && board.get(2) == team) {
            topHorizontalLine.setValue(true);
            return true;
        } else if (board.get(3) == team && board.get(4) == team && board.get(5) == team) {
            centerHorizontal.setValue(true);
            return true;
        } else if (board.get(6) == team && board.get(7) == team && board.get(8) == team) {
            bottomHorizontal.setValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkColumns() {

        int team = currentTeam.getTeamType();

        if (board.get(0) == team && board.get(3) == team && board.get(6) == team) {
            leftVertical.setValue(true);
            return true;
        } else if (board.get(1) == team && board.get(4) == team && board.get(7) == team) {
            centerVertical.setValue(true);
            return true;
        } else if (board.get(2) == team && board.get(5) == team && board.get(8) == team) {
            rightVertical.setValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDiagonals() {

        int team = currentTeam.getTeamType();

        if (board.get(0) == team && board.get(4) == team && board.get(8) == team) {
            leftRightDiagonal.setValue(true);
            return true;
        } else if (board.get(2) == team && board.get(4) == team && board.get(6) == team) {
            rightLeftDiagonal.setValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean fullBoard() {
        // iterate in the whole list and read the mCellStatus of the mCellIndex

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean checkForWin() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            setGameOver(true);
            return true;
        } else {
            setGameOver(false);
            return false;
        }
    }

    public void updateScore() {
        // the score for the winning team would be increased after one round

        // TO DO
        // game.setHostScore()
        // game.setGuestScore()
        currentTeam.incrementScore();

        if (currentTeam.getTeamType() == Team.TEAM_X) {
            // the winning team (team X) takes the current team instance
            teamX.setValue(currentTeam);

        } else {
            // the winning team (team O) takes the current team instance
            teamO.setValue(currentTeam);
        }
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUserLiveData(LiveData<User> userLiveData) {
        this.userLiveData = userLiveData;
    }

    public MutableLiveData<ArrayList<Integer>> getBoardLiveData() {
        return boardLiveData;
    }

    public void setBoardLiveData(MutableLiveData<ArrayList<Integer>> boardLiveData) {
        this.boardLiveData = boardLiveData;
    }

    public void play(int position) {
        board.set(position, currentTeam.getTeamType());
        boardLiveData.setValue(board);
    }


    public void gameEnded(){
        isGameInProgress.postValue(false);
        updateScore();
    }
}
