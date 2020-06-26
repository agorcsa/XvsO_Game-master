package com.example.xvso.viewmodel;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.example.xvso.object.Board;
import com.example.xvso.object.Cell;
import com.example.xvso.object.Game;
import com.example.xvso.object.User;
import com.example.xvso.object.WinningLines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ComputerViewModel extends BaseViewModel {

    private static final String PLAYER_X = "playerX";
    private static final String PLAYER_O = "playerO";

    private static final int TEAM_X = 1;
    private static final int TEAM_O = 2;

    private User playerX = new User();
    private User playerAI = new User();

    private Game game = new Game();
    private MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private ArrayList<Integer> preferredMoves = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

    private ArrayList<Integer> computerTime = new ArrayList<>(Arrays.asList(100, 300, 500, 1000, 1200, 1500, 200, 800, 700));

    final Handler handler = new Handler();

    public void saveCell(int position) {
        game = gameLiveData.getValue();
        if (game != null) {
                game.getBoard().getCells().get(position).setTag(1);
                gameLiveData.setValue(game);
                if (checkForWin()) {
                    // announce winner
                } else {
                            detectEmptyCell();
                            //checkForWin();
                        }
                    }
                }


    public void detectEmptyCell() {
        // I'm the computer, it's my turn now
        game.setCurrentPlayer("Player AI");
        gameLiveData.setValue(game);
        for (int i = 0; i < preferredMoves.size(); i++ ) {
            int index = preferredMoves.get(i); // get the value stored for each index on the list
            if (game.getBoard().getCells().get(index).getTag() == 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        game.getBoard().getCells().get(index).setTag(2);
                        checkForWin();
                        game.setCurrentPlayer(game.getHost().getName());
                        gameLiveData.setValue(game);
                    }
                }, computerTime.get(new Random().nextInt(computerTime.size())));
                break;
            }
        }
    }
    public boolean checkRows() {

        int team;

        if (game.getCurrentPlayer().equals(game.getHost().getName())) {
            team = TEAM_X;
        } else {
            team = TEAM_O;
        }

        Board board = game.getBoard();

        if (board.getCells().get(0).getTag() == team && board.getCells().get(1).getTag() == team && board.getCells().get(2).getTag() == team) {
            game.getWinningLines().setTopHorizontalLine(1);
            gameLiveData.setValue(game);
            return true;
        } else if (board.getCells().get(3).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(5).getTag() == team) {
            game.getWinningLines().setCenterHorizontal(1);
            gameLiveData.setValue(game);
            return true;
        } else if (board.getCells().get(6).getTag() == team && board.getCells().get(7).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setBottomHorizontal(1);
            gameLiveData.setValue(game);
            return true;
        } else {
            return false;
        }

    }

    public boolean checkColumns() {

        int team;

        if (game.getCurrentPlayer().equals(game.getHost().getName())) {
            team = TEAM_X;
        } else {
            team = TEAM_O;
        }

        Board board = game.getBoard();
        if (board.getCells().get(0).getTag() == team && board.getCells().get(3).getTag() == team && board.getCells().get(6).getTag() == team) {
            game.getWinningLines().setLeftVertical(1);
            gameLiveData.setValue(game);
            return true;
        } else if (board.getCells().get(1).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(7).getTag() == team) {
            game.getWinningLines().setCenterVertical(1);
            gameLiveData.setValue(game);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(5).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setRightVertical(1);
            gameLiveData.setValue(game);
            return true;
        } else {
            return false;
        }
    }


    public boolean checkDiagonals() {

        int team;

       if (game.getCurrentPlayer().equals(game.getHost().getName())) {
            team = TEAM_X;
        } else {
            team = TEAM_O;
        }

        Board board = game.getBoard();

        if (board.getCells().get(0).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(8).getTag() == team) {
            game.getWinningLines().setLeftRightDiagonal(1);
            gameLiveData.setValue(game);
            return true;
        } else if (board.getCells().get(2).getTag() == team && board.getCells().get(4).getTag() == team && board.getCells().get(6).getTag() == team) {
            game.getWinningLines().setRightLeftDiagonal(1);
            gameLiveData.setValue(game);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForWin() {
        game = gameLiveData.getValue();
        if (checkRows() || checkColumns() || checkDiagonals()) {
            if (game.getCurrentPlayer().equals(game.getHost().getName()))  {

                game.setGameResult(1);
                game.setHostScore(game.getHostScore() + 1);
                gameLiveData.setValue(game);

            } else {

                game.setGameResult(2);
                game.setGuestScore(game.getGuestScore() + 1);
                gameLiveData.setValue(game);

            }
            return true;
        } else {
            for (Cell cell : game.getBoard().getCells()) {
                if (cell.getTag() == 0) {
                    return false; // code exits if it finds an empty cell
                }
            }
            game.setGameResult(3); // draw
            gameLiveData.setValue(game);
            return true;
        }
    }


    public void gameEnded() {
        isGameInProgress.postValue(false);
        updateScore();
    }

    public void newRound() {

        game = gameLiveData.getValue();

        if (game != null) {
            game.setBoard(new Board());
            game.setWinningLines(new WinningLines());
            game.setGameResult(0);
            game.setRoundCount(game.getRoundCount() + 1);
            gameLiveData.setValue(game);
        }
    }

    public void resetGame() {

        game = gameLiveData.getValue();
        if (game != null) {
            game.setBoard(new Board());
            game.setWinningLines(new WinningLines());
            game.setGameResult(0);
            game.setRoundCount(0);
            gameLiveData.setValue(game);
            resetScore();
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
        if (game != null) {
            game.setGameResult(3);
            gameLiveData.setValue(game);
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public MutableLiveData<Game> getGameLiveData() {
        return gameLiveData;
    }

    public void setGameLiveData(MutableLiveData<Game> gameLiveData) {
        this.gameLiveData = gameLiveData;
    }

    public ComputerViewModel() {
        createNewGame();
    }

    public void createNewGame() {
        if (playerX != null && playerAI != null) {
            Game game = new Game();
            game.setHost(playerX);
            game.setGuest(playerAI);
            game.setCurrentPlayer(playerX.getName());
            game.setBoard(new Board());
            String userName = game.getHost().getUserName();
            game.setUserName(userName);
            gameLiveData.setValue(game);
            game.setCurrentPlayer(PLAYER_X);
        }
    }

    public void setHostPlayerName(String name) {
        game = gameLiveData.getValue();
        game.getHost().setName(name);
        game.setCurrentPlayer(name);
        gameLiveData.setValue(game);
    }
}
