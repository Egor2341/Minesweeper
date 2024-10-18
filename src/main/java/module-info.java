module com.example.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.minesweeper to javafx.fxml;
    exports com.example.minesweeper;
}