package com.example.minesweeper;

import java.util.ArrayList;

public class Cell {
    private String status;
    private ArrayList<Integer> neighbors;

    public Cell(String status, ArrayList<Integer> neighbors){
        this.status = status;
        this.neighbors = neighbors;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
