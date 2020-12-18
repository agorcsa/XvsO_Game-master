package com.game.xvso.object;

public class Game {

    // game status constants
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_GAME_FINISHED = 2;
    public static final int STATUS_ROUND_FINISHED = 3;
    public static final int STATUS_GAME_RESET = 4;
    public static final int STATUS_USER_EXIT = 5;
    public static final int STATUS_PLAY_AGAIN = 6;

    private Board board;
    private WinningLines winningLines = new WinningLines(0, 0, 0, 0, 0, 0, 0, 0);

    private User host;
    private User guest;
    private int status;
    private int playAgain;

    private int picture;
    private String gameNumber;
    private String userName;

    private String key;

    private int acceptedRequest;

    private String currentPlayer; // host or guest uid
    private int gameResult; // 0 = initial state - 1 = host win - 2 = guest win - 3 = draw

    private int hostScore;
    private int guestScore;

    private int roundCount;

    private int exitUser;

    // empty constructor
    public Game() {

    }

    public Game(Board board, User host, User guest,
                int status, int picture,
                String gameNumber, String userName,
                String key, int acceptedRequest,
                String currentPlayer, int gameResult,
                int guestScore, int hostScore,
                int roundCount,
                WinningLines winningLines,
                int exitUser, int playAgain) {

        this.board = board;
        this.host = host;
        this.guest = guest;
        this.status = status;
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.key = key;
        this.acceptedRequest = acceptedRequest;
        this.currentPlayer = currentPlayer;
        this.gameResult = gameResult;
        this.guestScore = guestScore;
        this.hostScore = hostScore;
        this.roundCount = roundCount;
        this.winningLines = winningLines;
        this.exitUser = exitUser;
        this.playAgain = playAgain;
    }

    public Game(int picture, String gameNumber, String userName, int acceptedRequest, int roundCount, WinningLines winningLines, int exitUser, int playAgain) {

        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.acceptedRequest = acceptedRequest;
        this.roundCount = roundCount;
        this.winningLines = winningLines;
        this.exitUser = exitUser;
        this.playAgain = playAgain;
    }

    // constructor
    public Game(Board board,
                User host, User guest,
                int status, int picture,
                String gameNumber, String userName,
                String key, int acceptedRequest,
                int roundCount,
                WinningLines winningLines,
                int exitUser, int playAgain) {

        this.board = board;
        this.host = host;
        this.guest = guest;
        this.status = status;
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.key = key;
        this.acceptedRequest = acceptedRequest;
        this.roundCount = roundCount;
        this.winningLines = winningLines;
        this.exitUser = exitUser;
        this.playAgain = playAgain;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(String gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAcceptedRequest() {
        return acceptedRequest;
    }

    public void setAcceptedRequest(int acceptedRequest) {
        this.acceptedRequest = acceptedRequest;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public WinningLines getWinningLines() {
        return winningLines;
    }

    public void setWinningLines(WinningLines winningLines) {
        this.winningLines = winningLines;
    }

    public int getHostScore() {
        return hostScore;
    }

    public void setHostScore(int hostScore) {
        this.hostScore = hostScore;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public int getExitUser() {
        return exitUser;
    }

    public void setExitUser(int exitUser) {
        this.exitUser = exitUser;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public int getPlayAgain() {
        return playAgain;
    }

    public void setPlayAgain(int playAgain) {
        this.playAgain = playAgain;
    }
}

