package com.example.minesweeper;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class HelloController {

    @FXML
    public TextField width;
    @FXML
    public TextField height;
    @FXML
    private GridPane gridPane;

    private HashMap<Integer, Cell<Button>> field = new HashMap<>();

    @FXML
    public void star(ActionEvent event) {
        createField(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()));
        System.out.println(event);
    }

    private void messageError(String error) {
        System.err.println(error);
    }

    public void createField(int height, int width) {
        if (height < 8 || width < 8) {
            messageError("Некорректное значение размера.");
        }
        field.clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        for (int k = 0; k < width; k++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(gridPane.getPrefWidth() / width));
        }
        for (int k = 0; k < height; k++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(gridPane.getPrefWidth() / height));
        }
        for (int i = 0; i < width * height; i++) {
            ArrayList<Integer> neighbors = getIntegers(height, width, i);
            Button bt = new Button();
            bt.setId(String.valueOf(i));
            bt.setStyle("-fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 5px; -fx-border-style: solid;");
            bt.setPrefWidth(gridPane.getPrefWidth() / width);
            bt.setPrefHeight(gridPane.getPrefHeight() / height);
            bt.setOnAction(_ -> clickOnCell(Integer.parseInt(bt.getId())));
            gridPane.add(bt, i % width, i / height);

            field.put(i, new Cell<>("Closed", neighbors, bt));
        }
        mines(width * height);
    }

    private ArrayList<Integer> getIntegers(int height, int width, int i) {
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
        return neighbors;
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


    private void checkNeighbors(int id) {
        if (!field.get(id).getStatus().equals("Closed") | field.get(id).getStatus().equals("Mine")) {
            System.err.println(field.get(id).getStatus());
            return;
        }
        int countOfMines = 0;
        ArrayList<Integer> neighbors = field.get(id).getNeighbors();
        for (int cell : neighbors) {
            if (field.get(cell).getStatus().equals("Mine")) {
                countOfMines += 1;
            }
        }
        if (countOfMines == 0) {
            field.get(id).setStatus("Opened");
            Button bt = field.get(id).getButton();
            bt.setStyle("");
            bt.setDisable(true);
            for (int cell : neighbors) {
                checkNeighbors(cell);
            }
        } else {
            field.get(id).setStatus("" + countOfMines);
            Button bt = field.get(id).getButton();
            bt.setText("" + countOfMines);
            bt.setDisable(true);
            bt.setStyle("-fx-font-size: 20px");
            return;
        }
    }

    private void clickOnCell(int id) {
        Button bt = field.get(id).getButton();
        bt.setStyle("");
        bt.setDisable(true);
        if (field.get(id).getStatus().equals("Mine")) {
            Image mineIco = new Image("file:src/main/java/com/example/minesweeper/mine.png", bt.getPrefWidth() - 10, bt.getPrefHeight() - 10, true, true);
            ImageView mine = new ImageView(mineIco);
            bt.setGraphic(mine);
            loss();
        }
        checkNeighbors(id);
    }

    private void loss() {
        System.out.println("Упс! Вы подорвались на мине!");
    }

    @FXML
    void initialize() {
        assert width != null : "fx:id=\"width\" was not injected: check your FXML file 'game.fxml'.";
        assert height != null : "fx:id=\"height\" was not injected: check your FXML file 'game.fxml'.";
        assert gridPane != null : "fx:id=\"gridPane\" was not injected: check your FXML file 'game.fxml'.";
        createField(8, 8);
    }

}