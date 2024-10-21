package com.example.minesweeper;

import java.util.ArrayList;

public class Cell<B> {
    private boolean isClosed = true;
    private boolean isMine = false;
    private boolean isFlag = false;
    private int number;
    private final ArrayList<Integer> neighbors;
    private final B button;

    public Cell(ArrayList<Integer> neighbors, B button) {
        this.neighbors = neighbors;
        this.button = button;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }


    public B getButton() {
        return button;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
