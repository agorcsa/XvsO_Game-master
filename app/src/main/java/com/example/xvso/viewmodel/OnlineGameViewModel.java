package com.example.xvso.viewmodel;


import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.xvso.deserializer.GameDeserializer;
import com.example.xvso.object.Board;
import com.example.xvso.object.Game;
import com.example.xvso.firebaseutils.FirebaseQueryLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OnlineGameViewModel extends BaseViewModel {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String MULTIPLAYER = "multiplayer";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference query;

    private MutableLiveData<ArrayList<Integer>> boardLiveData = new MutableLiveData<>();
    public LiveData<Game> gameLiveData;
    private Game game = new Game();

    private String gameID = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OnlineGameViewModel(String gameID) {
        this.gameID = gameID;

          if (gameID != null) {

            query = FirebaseDatabase.getInstance().getReference(MULTIPLAYER).child((gameID));

            FirebaseQueryLiveData resultLiveData = new FirebaseQueryLiveData(query);
            gameLiveData = Transformations.map(resultLiveData, new GameDeserializer());

        }
    }

    public void saveCellToFirebase(int position) {
        game = gameLiveData.getValue();
        if (auth.getCurrentUser().getUid().equals(game.getHost().getUID())) {
            game.getBoard().getCells().get(position).setTag(1);
        } else {
            game.getBoard().getCells().get(position).setTag(2);
        }
        if (game.getCurrentPlayer().equals(game.getHost().getName())) {
            game.setCurrentPlayer(game.getGuest().getName());
        } else {
            game.setCurrentPlayer(game.getHost().getName());
        }
        query
                .setValue(game)
        ;
        checkForWin();
    }

    public boolean checkRows() {

        int team;

        if (auth.getCurrentUser().getUid().equals(game.getHost().getUID())) {
            team = 1;
        } else {
            team = 2;
        }

            Board board = game.getBoard();

        if (board.getCells().get(0).getTag() == team && board.getCells().get(1).getTag() == team && board.getCells().get(2).getTag() == team) {
            topHorizontalLine.setValue(true);
            return true;
        } else if (board.getCells().get(3).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(5).getTag() == team) {
            centerHorizontal.setValue(true);
            return true;
        } else if (board.getCells().get(6).getTag() == team && board.getCells().get(7).getTag() == team && board.getCells().get(8).getTag() == team) {
            bottomHorizontal.setValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkColumns() {

        int team;

        if (auth.getCurrentUser().getUid().equals(game.getHost().getUID())) {
            team = 1;
        } else {
            team = 2;
        }

        Board board = game.getBoard();
        if (board.getCells().get(0).getTag() == team && board.getCells().get(3).getTag() == team && board.getCells().get(6).getTag() == team) {
            leftVertical.setValue(true);
            return true;
        } else if (board.getCells().get(1).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(7).getTag() == team) {
            centerVertical.setValue(true);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(5).getTag() == team && board.getCells().get(8).getTag() == team) {
            rightVertical.setValue(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDiagonals() {

        int team;

        if (auth.getCurrentUser().getUid().equals(game.getHost().getUID())) {
            team = 1;
        } else {
            team = 2;
        }
        Board board = game.getBoard();


        if (board.getCells().get(0).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(8).getTag() == team) {
            leftRightDiagonal.setValue(true);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(6).getTag() == team) {
            rightLeftDiagonal.setValue(true);
            return true;
        } else {
            return false;
        }
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
}
