package com.game.xvso.object;

public class WinningLines {

    // variables used to show the winning lines for the winner online
    // = 0, hidden
    // = 1, shown
    private int leftVertical;
    private int centerVertical;
    private int rightVertical;
    private int topHorizontalLine;
    private int centerHorizontal;
    private int bottomHorizontal;
    private int leftRightDiagonal;
    private int rightLeftDiagonal;

    // empty constructor required for Firebase
    public WinningLines() {}

    public WinningLines(int leftVertical, int centerVertical, int rightVertical,
                        int topHorizontalLine, int centerHorizontal, int bottomHorizontal,
                        int leftRightDiagonal, int rightLeftDiagonal) {
        this.leftVertical = leftVertical;
        this.centerVertical = centerVertical;
        this.rightVertical = rightVertical;
        this.topHorizontalLine = topHorizontalLine;
        this.centerHorizontal = centerHorizontal;
        this.bottomHorizontal = bottomHorizontal;
        this.leftRightDiagonal = leftRightDiagonal;
        this.rightLeftDiagonal = rightLeftDiagonal;
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

    public int getTopHorizontalLine() {
        return topHorizontalLine;
    }

    public void setTopHorizontalLine(int topHorizontalLine) {
        this.topHorizontalLine = topHorizontalLine;
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
