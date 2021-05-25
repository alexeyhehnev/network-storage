package com.hehnev.client;

import com.hehnev.client.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setTitle("network storage");
        primaryStage.setScene(new Scene(root, 1280, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
