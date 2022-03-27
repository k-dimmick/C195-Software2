package com;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View/SignOn.fxml"));
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Sign On");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);


        JDBC.closeConnection();
    }
}
