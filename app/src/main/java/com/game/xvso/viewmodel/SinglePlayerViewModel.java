package com.game.xvso.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.game.xvso.object.Board;
import com.game.xvso.object.Cell;
import com.game.xvso.object.Game;
import com.game.xvso.object.User;
import com.game.xvso.object.WinningLines;

public class SinglePlayerViewModel extends BaseViewModel {

    private Game game = new Game();

    private MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private static final String PLAYER_X ="playerX";
    private static final String PLAYER_O ="playerO";

    private static final int TEAM_X = 1;
    private static final int TEAM_O = 2;

    private User playerXUser = new User();
    private User playerOUser = new User();


    public SinglePlayerViewModel() {
        createNewGame();
    }

    public void saveCell(int position) {
        game = gameLiveData.getValue();
        if (game != null) {
            // set the cell for an X or O
            if (game.getCurrentPlayer().equals(PLAYER_X)) {
                game.getBoard().getCells().get(position).setTag(1);
            } else {
                game.getBoard().getCells().get(position).setTag(2);
            }
            // update the game
            gameLiveData.setValue(game);
            checkForWin();
            // switch the player (no matter the current player)
            if (game.getCurrentPlayer().equals(PLAYER_X)) {
                game.setCurrentPlayer(PLAYER_O);
            } else {
                game.setCurrentPlayer(PLAYER_X);
            }
        }
    }

    public boolean checkRows() {

        int team;

        if (game.getCurrentPlayer().equals(PLAYER_X))  {
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

        if (game.getCurrentPlayer().equals(PLAYER_X))  {
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

        if (game.getCurrentPlayer().equals(PLAYER_X))  {
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
            if (game.getCurrentPlayer().equals(PLAYER_X)) {
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
            for (Cell cell: game.getBoard().getCells()) {
                if (cell.getTag() == 0) {
                    return false; // code exits if it finds an empty cell
                }
            }
            game.setGameResult(3); // draw
            gameLiveData.setValue(game);
            return false;
        }
    }

    public void createNewGame() {
        if (playerXUser != null && playerOUser != null) {
            Game game = new Game();
            game.setHost(playerXUser);
            game.setGuest(playerOUser);
            game.setCurrentPlayer(playerXUser.getName());
            game.setBoard(new Board());
            String userName = game.getHost().getUserName();
            game.setUserName(userName);
            gameLiveData.setValue(game);
            game.setCurrentPlayer(PLAYER_X);
        }
    }


    public void gameEnded(){
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

    public void resetGame(){

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

    public void setGuestPlayerName(String name) {
        game = gameLiveData.getValue();
        game.getGuest().setName(name);
        gameLiveData.setValue(game);
    }

    public void setHostPlayerName(String name) {
        game = gameLiveData.getValue();
        game.getHost().setName(name);
        gameLiveData.setValue(game);
    }

    public void timeUp() {
        game = gameLiveData.getValue();
        if (game != null) {
            game.setGameResult(3);
            gameLiveData.setValue(game);
        }
    }
}
