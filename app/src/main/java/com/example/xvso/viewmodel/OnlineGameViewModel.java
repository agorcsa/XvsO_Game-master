package com.example.xvso.viewmodel;


import android.os.Build;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public MutableLiveData<Game> gameLiveData;

    private String gameID = "";

    private ArrayList<Integer> board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OnlineGameViewModel(String gameID) {
        this.gameID = gameID;

          if (gameID != null) {

            query = FirebaseDatabase.getInstance().getReference(MULTIPLAYER).child((gameID));

            FirebaseQueryLiveData resultLiveData = new FirebaseQueryLiveData(query);
              gameLiveData = (MutableLiveData<Game>) Transformations.map(resultLiveData, new GameDeserializer());

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
        gameLiveData.setValue(game);
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
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(3).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(5).getTag() == team) {
            game.getWinningLines().setCenterHorizontal(1);
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(6).getTag() == team && board.getCells().get(7).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setBottomHorizontal(1);
            game.setStatus(Game.STATUS_ROUND_FINISHED);
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
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(1).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(7).getTag() == team) {
            game.getWinningLines().setCenterVertical(1);
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(5).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setRightVertical(1);
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
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
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(6).getTag() == team) {
            game.getWinningLines().setRightLeftDiagonal(1);
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
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
                game.setHostScore(game.getHostScore() + 1);
                game.setStatus(Game.STATUS_ROUND_FINISHED);
                query.setValue(game);

            } else {

                game.setGameResult(2);
                game.setGuestScore(game.getGuestScore() + 1);
                game.setStatus(Game.STATUS_ROUND_FINISHED);
                query.setValue(game);

            }
            return true;
        } else {
            for (Cell cell: game.getBoard().getCells()) {
                if (cell.getTag() == 0) {
                    return false; // code exits if it finds an empty cell
                }
            }
            game.setGameResult(3); // draw
            game.setStatus(Game.STATUS_ROUND_FINISHED);
            query.setValue(game);
            return false;
        }
    }

    public void gameEnded(){
        isGameInProgress.postValue(false);
        updateScore();
    }

    public void newRound() {
        game = gameLiveData.getValue();
        game.setBoard(new Board());
        game.setWinningLines(new WinningLines());
        game.setGameResult(0);
        game.setRoundCount(game.getRoundCount() + 1);
        game.setStatus(Game.STATUS_ROUND_FINISHED);
        query.setValue(game);
    }

    public void resetGame() {

        game = gameLiveData.getValue();
        if (game != null) {
            game.setBoard(new Board());
            game.setWinningLines(new WinningLines());
            game.setGameResult(0);
            game.setRoundCount(0);
            resetScore();
            // a new game will start, so status will be put back to "playing"
            game.setStatus(Game.STATUS_GAME_RESET);
            gameLiveData.setValue(game);
        }
    }

    public void resetScore() {
        if (game != null) {
            game.setHostScore(0);
            game.setGuestScore(0);
        }
    }

    public void timeUp() {
        game = gameLiveData.getValue();
        game.setGameResult(3);
        query.setValue(game);
    }

    public void playGame() {
        game = gameLiveData.getValue();
        game.setStatus(Game.STATUS_PLAY_AGAIN);
        game.setWinningLines(new WinningLines());
        query.setValue(game);
    }

    public void playNewGame() {
        game = gameLiveData.getValue();
        game.setStatus(Game.STATUS_PLAY_AGAIN);
        game.setGuestScore(0);
        game.setHostScore(0);
        game.setWinningLines(new WinningLines());
        query.setValue(game);
    }

    public void exitGame() {
        game = gameLiveData.getValue();
        game.setStatus(Game.STATUS_USER_EXIT);
        query.setValue(game);
        game.setStatus(Game.STATUS_GAME_FINISHED);
        game.setWinningLines(new WinningLines());
        query.setValue(game);
    }

    public void onGameExitAbruptly() {
        game = gameLiveData.getValue();
        game.setStatus(Game.STATUS_USER_EXIT);
        query.setValue(game);
    }
}
