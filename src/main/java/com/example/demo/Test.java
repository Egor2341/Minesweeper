package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {

    static HashMap<Integer, Cell> field = new HashMap<>();

    public static void main(String[] args) {
        createField(8, 8);
    }

    private static void messageError(String error) {
        System.err.println(error);
    }

    private static void createField(int height, int width) {
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
            field.put(i, new Cell("safely", neighbors));
            }
        mines(width * height);
    }

    private static void mines(int size) {
        HashSet<Integer> minesId = new HashSet<>();
        int count = (int) (size * 0.15 + 0.5);
        while (minesId.size() < count){
            minesId.add((int) (Math.random() * size));
        }

        for (int i : minesId){
            field.get(i).setStatus("Mine");
        }
    }

}
