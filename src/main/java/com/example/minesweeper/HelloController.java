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
    private int numberOfMines;
    private ArrayList<Integer> arrayIDMines = new ArrayList<>();

    @FXML
    public void star(ActionEvent event) {
        createField(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
    }

    private void messageError(String error) {
        System.err.println(error);
        gridPane.add(new Button(), 8, 0);
    }
    public void clear(){
        numberOfMines=0;
        arrayIDMines.clear();
        field.clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.disableProperty().setValue(false);
    }
    public void createField(int width, int height) {
        if (height < 8 || width < 8 || height > 52 || width > 52) {
            messageError("Некорректное значение размера.");
            return;
        }
        clear();
        numberOfMines = (int) (height * width * 0.15 + 0.5);
        for (int k = 0; k < width; k++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(gridPane.getPrefWidth() / width));
        }
        for (int k = 0; k < height; k++) {
            gridPane.getRowConstraints().add(new RowConstraints(gridPane.getPrefHeight() / height));
        }
        for (int i = 0; i < width * height; i++) {
            ArrayList<Integer> neighbors = getIntegers(height, width, i);
            Button bt = new Button();
            bt.setId(String.valueOf(i));
            bt.setStyle("-fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 5px; -fx-border-style: solid;");
            bt.setPrefWidth(gridPane.getPrefWidth() / width);
            bt.setPrefHeight(gridPane.getPrefHeight() / height);
            bt.setOnAction(_ -> clickOnCell(Integer.parseInt(bt.getId())));
            gridPane.add(bt, i % (width), i / width);
            System.err.println(bt.getId() + " " + i % (width) + " " + i / width);
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
        while (minesId.size() < numberOfMines) {
            minesId.add((int) (Math.random() * size));
        }

        for (int id : minesId) {
            field.get(id).setStatus("Mine");
            arrayIDMines.add(id);
        }
    }


    private void checkNeighbors(int id) {
        if (!field.get(id).getStatus().equals("Closed") | field.get(id).getStatus().equals("Mine")) {
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
            loss();
        }
        checkNeighbors(id);
    }

    private void loss() {
        for (int id : arrayIDMines) {
            Button bt = field.get(id).getButton();
            bt.setDisable(true);
            Image mineIco = new Image("file:src/main/java/com/example/minesweeper/mine.png", bt.getPrefWidth() - 10, bt.getPrefHeight() - 10, true, true);
            ImageView mine = new ImageView(mineIco);
            bt.setGraphic(mine);
        }
        gridPane.disableProperty().setValue(true);
        System.out.println("Упс! Вы подорвались на мине!");
    }

    @FXML
    void initialize() {
        assert width != null : "fx:id=\"width\" was not injected: check your FXML file 'game.fxml'.";
        assert height != null : "fx:id=\"height\" was not injected: check your FXML file 'game.fxml'.";
        assert gridPane != null : "fx:id=\"gridPane\" was not injected: check your FXML file 'game.fxml'.";
        createField(8, 10);
    }

}