package com.example.nutrilens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NutriLensController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}