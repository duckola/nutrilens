package com.example.nutrilens.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {
    private void switchScene(String fxml) throws Exception {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/example/nutrilens/view/" + fxml))));
        stage.show();
    }

    public void openCapture() throws Exception { switchScene("capture.fxml"); }
    public void openProfile() throws Exception { switchScene("profile.fxml"); }
    public void openLogs() throws Exception { switchScene("history.fxml"); }
    public void openSuggestion() throws Exception { switchScene("suggestion.fxml"); }
}
