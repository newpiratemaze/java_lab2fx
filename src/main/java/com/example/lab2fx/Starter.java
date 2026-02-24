package com.example.lab2fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 867, 691);
        stage.setTitle("MVVM");
        stage.setScene(scene);
        stage.show();

    }
}
