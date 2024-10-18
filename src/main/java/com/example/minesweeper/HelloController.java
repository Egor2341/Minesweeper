package com.example.minesweeper;

import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HelloController {

    private HashMap<Integer, Cell> field = new HashMap<>();

    private void messageError(String error) {
        System.err.println(error);
    }

    private void createField(int height, int width) {
        if (height < 8 || width < 8) {
            messageError("Некорректное значение размера.");
        }
        for (int i = 0; i < width * height; i++) {
            ArrayList<Integer> neighbors = new ArrayList<>();
            if (i / width != 0) {
                neighbors.add(i - width);
                if (i % width != width - 1) {
                    neighbors.add(i - width + 1);
                }
            }
            if (i % width != width - 1) {
                neighbors.add(i + 1);
                if (i / width != height - 1) {
                    neighbors.add(i + 1 + width);
                }
            }
            if (i / width != height - 1) {
                neighbors.add(i + width);
                if (i % width != 0) {
                    neighbors.add(i + width - 1);
                }
            }
            if (i % width != 0) {
                neighbors.add(i - 1);
                if (i / width != 0) {
                    neighbors.add(i - width - 1);
                }
            }
            field.put(i, new Cell("None", neighbors));
        }
        mines(width * height);
    }

    private void mines(int size) {
        HashSet<Integer> minesId = new HashSet<>();
        int count = (int) (size * 0.15 + 0.5);
        while (minesId.size() < count) {
            minesId.add((int) (Math.random() * size));
        }

        for (int i : minesId) {
            field.get(i).setStatus("Mine");
        }
    }


    private void checkNeighbors(int id){
        if (!field.get(id).getStatus().equals("Closed") || field.get(id).getStatus().equals("Mine")){return;}
        int countOfMines = 0;
        ArrayList<Integer> neighbors = field.get(id).getNeighbors();
        for (int cell : neighbors){
            if (field.get(cell).getStatus().equals("Mine")){countOfMines += 1;}
        }
        if (countOfMines == 0) {
            field.get(id).setStatus("Opened");
            for (int cell : neighbors){
                checkNeighbors(cell);
            }
        }
        else {
            field.get(id).setStatus(""+countOfMines);
            return;}
    }

    private void clickOnCell(int id){
        if (field.get(id).getStatus().equals("Mine")){loss();}
        checkNeighbors(id);
    }

    private void loss(){
        System.out.println("Упс! Вы подорвались на мине!");
    }

}