package com.example.minesweeper;

import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


public class HelloController {

    @FXML
    public TextField textFieldWidth;
    @FXML
    public TextField textFieldHeight;
    @FXML
    public Pane head;
    @FXML
    public Pane body;
    @FXML
    public AnchorPane anchor;
    @FXML
    public Button smile;
    @FXML
    private GridPane gridPane;

    private final HashMap<Integer, Cell<Button>> field = new HashMap<>();
    private final ArrayList<Integer> arrayIDMines = new ArrayList<>();
    private final HashSet<Integer> opened = new HashSet<>();
    private int numberOfMines;
    private final int sqr = 55;
    private int flags;

    @FXML
    public void check(int index) {
        TextField field = new TextField[]{textFieldWidth, textFieldHeight}[index];
        field.setText(field.getText().replaceAll("\\D", ""));
        if (field.getLength() > 2) field.deleteText(2, field.getLength());
        field.end();
    }

    @FXML
    public void star() {
        int newWidth = 8;
        int newHeight = 8;
        try {
            newWidth = Integer.parseInt(textFieldWidth.getText());
            newHeight = Integer.parseInt(textFieldHeight.getText());
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        createField(newWidth, newHeight);
    }

    private void messageError() {
        System.err.println("Некорректное значение размера.");
    }

    private void setting(int width, int height) {
        Image stateIco = new Image("file:src/main/java/com/example/minesweeper/state.png", 30, 30, true, true);
        smile.graphicProperty().setValue(new ImageView(stateIco));
        head.setPrefWidth((sqr + 1) * width);
        head.setLayoutX((double) sqr / 2);
        head.setLayoutY((double) sqr / 2);
        gridPane.setPrefWidth((sqr + 1) * width);
        gridPane.setPrefHeight((sqr + 1) * height);
        gridPane.setLayoutX((double) sqr / 2);
        gridPane.setLayoutY(sqr + head.getPrefHeight());
        body.setPrefWidth(gridPane.getPrefWidth() + sqr);
        body.setPrefHeight(gridPane.getPrefHeight() + head.getPrefHeight() + 1.5 * sqr);
        anchor.setPrefWidth(gridPane.getPrefWidth() + sqr * 2);
        for (int k = 0; k < width; k++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(sqr));
        }
        for (int k = 0; k < height; k++) {
            gridPane.getRowConstraints().add(new RowConstraints(sqr));
        }
    }

    public void clear() {
        numberOfMines = 0;
        flags = 0;
        arrayIDMines.clear();
        opened.clear();
        field.clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.disableProperty().setValue(false);
    }

    private void createField(int width, int height) {
        if (height < 8 || width < 8 || height > 52 || width > 52) {
            messageError();
            return;
        }
        clear();
        setting(width, height);
        numberOfMines = (int) (height * width * 0.15 + 0.5);
        for (int i = 0; i < width * height; i++) {
            ArrayList<Integer> neighbors = getIntegers(height, width, i);
            Button bt = new Button();
            bt.setId(String.valueOf(i));
            bt.setStyle("-fx-background-color: #bdbdbd;-fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 5px; -fx-border-style: solid;");
            bt.setPrefWidth(sqr);
            bt.setPrefHeight(sqr);
            bt.setOnMouseClicked(e -> clickOnCell(Integer.parseInt(bt.getId()), e));
            gridPane.add(bt, i % (width), i / width);
            field.put(i, new Cell<>(neighbors, bt));
        }
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

    private void mines(int id, ArrayList<Integer> neighbors) {
        HashSet<Integer> minesId = new HashSet<>();
        neighbors.add(id);

        while (minesId.size() < numberOfMines) {
            int mineId = (int) (Math.random() * field.size());
            boolean addMine = true;
            for (int n : neighbors) {
                if (n == mineId) {
                    addMine = false;
                    break;
                }
            }
            if (addMine) {
                minesId.add(mineId);
                field.get(mineId).setMine(true);
                arrayIDMines.add(mineId);
            }
        }

    }


    private void checkNeighbors(int id) {
        String[] colors = {"blue", "green", "red", "yellow", "purple", "orange", "black", "black"};
        if (!field.get(id).isClosed() | field.get(id).isMine() | opened.contains(id)) {
            return;
        }
        opened.add(id);
        int countOfMines = 0;
        ArrayList<Integer> neighbors = field.get(id).getNeighbors();
        for (int cell : neighbors) {
            if (field.get(cell).isMine()) {
                countOfMines += 1;
            }
        }
        Button bt = field.get(id).getButton();

        bt.setOpacity(1);
        if (countOfMines == 0) {
            field.get(id).setClosed(false);
            field.get(id).setMines(0);
            bt.setStyle("-fx-border-color: #7b7b7b rgba(0,0,0,0) rgba(0,0,0,0) #7b7b7b ; -fx-border-TextFieldWidth: 2px; -fx-border-style: solid;");
            bt.setDisable(true);
            for (int cell : neighbors) {
                checkNeighbors(cell);
            }
        } else {
            field.get(id).setClosed(false);
            field.get(id).setMines(countOfMines);

            bt.setText("" + countOfMines);
            bt.setStyle("-fx-background-color: #bdbdbd;-fx-text-fill:" + colors[countOfMines - 1] + ";-fx-border-color: #7b7b7b rgba(0,0,0,0) rgba(0,0,0,0) #7b7b7b ; -fx-border-width: 2px; -fx-border-style: solid; -fx-font-size:" + (sqr / 2 - 2) + "px; -fx-font-weight:900");
        }
    }

    private void clickOnNumber(int id){
        int flagsAround = 0; // число флажков вокруг клетки
        boolean possibleLoss = false;
        int minesAround = 0;
        for (int n : field.get(id).getNeighbors()){
            if (field.get(n).isFlag()) {
                flagsAround++;
                if (!field.get(n).isMine()){
                    possibleLoss = true;
                }
            }
            if (field.get(n).isMine()){
                minesAround++;
            }
        }
        if (flagsAround == minesAround){
            if (possibleLoss){
                loss();
            }
            else {
                for (int n : field.get(id).getNeighbors()){
                    checkNeighbors(n);
                }
            }
        }
    }

    private void clickOnCell(int id, MouseEvent event) {
        if (field.get(id).getMines() != 0){
            clickOnNumber(id);
            return;
        }
        Button bt = field.get(id).getButton();
        if (event.getButton() == MouseButton.PRIMARY & !field.get(id).isFlag()) {
            if (arrayIDMines.isEmpty()) {
                mines(id, field.get(id).getNeighbors());
            }
            if (field.get(id).isMine()) {
                loss();
            }
            checkNeighbors(id);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            Cell<Button> cell = field.get(id);
            if (cell.isFlag()) {
                bt.setGraphic(null);
                cell.setFlag(false);
                flags--;
            } else if (cell.isClosed()) {
                Image flagIco = new Image("file:src/main/java/com/example/minesweeper/flag.png", 30, 30, true, true);
                bt.graphicProperty().setValue(new ImageView(flagIco));
                cell.setFlag(true);
                flags++;
            }
        }
        if (flags == numberOfMines && opened.size() == field.size() - numberOfMines) win();
    }

    private void loss() {
        for (int id : arrayIDMines) {
            Button bt = field.get(id).getButton();
            bt.setDisable(true);
            bt.setOpacity(1);
            Image mineIco = new Image("file:src/main/java/com/example/minesweeper/mine.png", 30, 30, true, true);
            bt.graphicProperty().setValue(new ImageView(mineIco));
        }
        gridPane.disableProperty().setValue(true);
        Image loseIco = new Image("file:src/main/java/com/example/minesweeper/loss.png", 30, 30, true, true);
        smile.graphicProperty().setValue(new ImageView(loseIco));
        System.out.println("Упс! Вы подорвались на мине!");
    }

    private void win() {
        Image winIco = new Image("file:src/main/java/com/example/minesweeper/win.png", 30, 30, true, true);
        smile.graphicProperty().setValue(new ImageView(winIco));
        gridPane.disableProperty().setValue(true);
        System.out.println("Респект! Вы разминировали поле!");
    }

    @FXML
    void initialize() {
        assert textFieldWidth != null : "fx:id=\"textFieldWidth\" was not injected: check your FXML file 'game.fxml'.";
        assert textFieldHeight != null : "fx:id=\"textFieldHeight\" was not injected: check your FXML file 'game.fxml'.";
        assert gridPane != null : "fx:id=\"gridPane\" was not injected: check your FXML file 'game.fxml'.";
        assert body != null : "fx:id=\"body\" was not injected: check your FXML file 'game.fxml'.";
        assert anchor != null : "fx:id=\"anchor\" was not injected: check your FXML file 'game.fxml'.";
        assert head != null : "fx:id=\"head\" was not injected: check your FXML file 'game.fxml'.";
        textFieldWidth.setOnKeyTyped(_ -> check(0));
        textFieldHeight.setOnKeyTyped(_ -> check(1));
        createField(8, 10);
    }
}