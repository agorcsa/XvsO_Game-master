package com.example.xvso.viewmodel;


import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.xvso.deserializer.GameDeserializer;
import com.example.xvso.object.Board;
import com.example.xvso.object.Cell;
import com.example.xvso.object.Game;
import com.example.xvso.firebaseutils.FirebaseQueryLiveData;
import com.example.xvso.object.WinningLines;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineGameViewModel extends BaseViewModel {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String MULTIPLAYER = "multiplayer";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database =  FirebaseDatabase.getInstance();
    private DatabaseReference query;
    private MutableLiveData<ArrayList<Cell>> boardLiveData = new MutableLiveData<>();
    private Game game = new Game();
    public LiveData<Game> gameLiveData;

    private String gameID = "";

    private ArrayList<Integer> board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

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
            game.getWinningLines().setTopHorizontalLine(1);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(3).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(5).getTag() == team) {
            game.getWinningLines().setCenterHorizontal(1);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(6).getTag() == team && board.getCells().get(7).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setBottomHorizontal(1);
            query.setValue(game);
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
            game.getWinningLines().setLeftVertical(1);
            query.setValue(1);
            return true;
        } else if (board.getCells().get(1).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(7).getTag() == team) {
            game.getWinningLines().setCenterVertical(1);
            query.setValue(1);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(5).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setRightVertical(1);
            query.setValue(1);
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
            game.getWinningLines().setLeftRightDiagonal(1);
            query.setValue(1);
            game.setGameResult(1);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(6).getTag() == team) {
            game.getWinningLines().setRightVertical(1);
            query.setValue(1);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForWin() {
        game = gameLiveData.getValue();
        if (checkRows() || checkColumns() || checkDiagonals()) {
            if (auth.getCurrentUser().getUid().equals(game.getHost().getUID())) {
                game.setGameResult(1);
            } else {
                game.setGameResult(2);
            }
            query.setValue(game);
            return true;
        } else {
            query.setValue(game);
            return false;
        }
    }

    public void newRound() {

        clearBoard();

        game.setBoard(new Board());
        // and then push the Game to Firebase

        //reset the winning lines/scores/currentPlayer.
        game.setWinningLines(new WinningLines());

        game.setCurrentPlayer(game.getHost().toString());

        isGameInProgress.setValue(true);
    }
}
