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

    private int hostScore = 0;
    private int guestScore = 0;

    // variables used to show the winning lines for the winner online
    // = 0, hidden
    // = 1, shown
    private int leftVertical = 0;
    private int centerVertical = 0;
    private int rightVertical = 0;

    private int topHorizontalLine = 0;
    private int centerHorizontal = 0;
    private int bottomHorizontal = 0;

    private int leftRightDiagonal = 0;
    private int rightLeftDiagonal = 0;

    // empty constructor
    public Game() {

    }

    public Game(Board board, User host, User guest,
                int status, int picture,
                String gameNumber, String userName,
                String key, int acceptedRequest,
                String currentPlayer, int gameResult,
                int guestScore, int hostScore,
                int leftVertical, int centerVertical, int rightVertical,
                int topHorizontalLine, int centerHorizontal, int bottomHorizontal,
                int leftRightDiagonal, int rightLeftDiagonal) {

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
        this.leftVertical = leftVertical;
        this.centerVertical = centerVertical;
        this.rightVertical = rightVertical;
        this.topHorizontalLine = topHorizontalLine;
        this.centerHorizontal = centerHorizontal;
        this.bottomHorizontal = bottomHorizontal;
        this.leftRightDiagonal = leftRightDiagonal;
        this.rightLeftDiagonal = rightLeftDiagonal;
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
    public Game(Board board,
                User host, User guest,
                int status, int picture,
                String gameNumber, String userName,
                String key, int acceptedRequest) {
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

    public int getHostScore() {
        return hostScore;
    }

    public void setHostScore(int hostScore) {
        this.hostScore = hostScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getLeftVertical() {
        return leftVertical;
    }

    public void setLeftVertical(int leftVertical) {
        this.leftVertical = leftVertical;
    }

    public int getCenterVertical() {
        return centerVertical;
    }

    public void setCenterVertical(int centerVertical) {
        this.centerVertical = centerVertical;
    }

    public int getRightVertical() {
        return rightVertical;
    }

    public void setRightVertical(int rightVertical) {
        this.rightVertical = rightVertical;
    }

    public int getTopHorizontal() {
        return topHorizontalLine;
    }

    public void setTopHorizontal(int topHorizontal) {
        this.topHorizontalLine = topHorizontal;
    }

    public int getCenterHorizontal() {
        return centerHorizontal;
    }

    public void setCenterHorizontal(int centerHorizontal) {
        this.centerHorizontal = centerHorizontal;
    }

    public int getBottomHorizontal() {
        return bottomHorizontal;
    }

    public void setBottomHorizontal(int bottomHorizontal) {
        this.bottomHorizontal = bottomHorizontal;
    }

    public int getLeftRightDiagonal() {
        return leftRightDiagonal;
    }

    public void setLeftRightDiagonal(int leftRightDiagonal) {
        this.leftRightDiagonal = leftRightDiagonal;
    }

    public int getRightLeftDiagonal() {
        return rightLeftDiagonal;
    }

    public void setRightLeftDiagonal(int rightLeftDiagonal) {
        this.rightLeftDiagonal = rightLeftDiagonal;
    }
}


