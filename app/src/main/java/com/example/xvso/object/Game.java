package com.example.xvso.object;

public class Game {

    // game status constants
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_FINISHED = 2;

    private Board board;
    private User host;
    private User guest;
    private int status;

    private int picture;
    private String gameNumber;
    private String userName;

    private String key;

    private int acceptedRequest;

    private String currentPlayer; // host or guest uid
    private int gameResult; // 0 = initial state - 1 = host win - 2 = guest win - 3 = draw

    // empty constructor
    public Game() {

    }

    public Game(Board board, User host, User guest, int status, int picture, String gameNumber, String userName, String key, int acceptedRequest, String currentPlayer, int gameResult) {
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

    public Game(int picture, String gameNumber, String userName, int acceptedRequest) {
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.acceptedRequest = acceptedRequest;
    }

    // constructor
    public Game(Board board, User host, User guest, int status, int picture, String gameNumber, String userName, String key, int acceptedRequest) {
        this.board = board;
        this.host = host;
        this.guest = guest;
        this.status = status;
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.key = key;
        this.acceptedRequest = acceptedRequest;
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
}


