package com.hehnev.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    VBox leftPanel, rightPanel;


    public void exitAction() {
        Platform.exit();
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyBtnAction(ActionEvent actionEvent) {
    }
}
