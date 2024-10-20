package com.example.minesweeper;

import java.util.ArrayList;

public class Cell<B> {
    private String status;
    private ArrayList<Integer> neighbors;
    private B button;

    public Cell(String status, ArrayList<Integer> neighbors, B button){
        this.status = status;
        this.neighbors = neighbors;
        this.button = button;
    }public Cell(String status, ArrayList<Integer> neighbors){
        this.status = status;
        this.neighbors = neighbors;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public B getButton() {
        return button;
    }

}
