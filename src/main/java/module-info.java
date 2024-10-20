module com.example.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.minesweeper to javafx.fxml;
    exports com.example.minesweeper;
}