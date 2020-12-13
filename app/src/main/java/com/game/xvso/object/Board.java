package com.game.xvso.object;

import java.util.ArrayList;

public class Board {

    private ArrayList<Cell> cells = new ArrayList<>();

    // constructor
    public Board() {
        initCellArrayList();
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    public Board(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    private void initCellArrayList() {
        for (int i = 0; i < 9; i++) {
            cells.add(new Cell());
        }
    }

}
