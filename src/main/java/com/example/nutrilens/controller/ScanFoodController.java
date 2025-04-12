package com.example.nutrilens.controller;

import com.example.nutrilens.service.NutritionAPIService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;

import java.io.File;

public class ScanFoodController {

    @FXML private ImageView imageView;
    @FXML private TextArea resultArea;

    private File selectedImage;

    @FXML
    public void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Food Image");
        selectedImage = fileChooser.showOpenDialog(null);

        if (selectedImage != null) {
            imageView.setImage(new Image(selectedImage.toURI().toString()));
            resultArea.setText("Image selected: " + selectedImage.getName());
        }
    }

    @FXML
    public void handleDetect() {
        if (selectedImage == null) {
            showAlert("Please upload an image first.");
            return;
        }

        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return NutritionAPIService.analyzeImage(selectedImage);
            }
        };

        task.setOnSucceeded(e -> resultArea.setText(task.getValue()));
        task.setOnFailed(e -> resultArea.setText("Failed to analyze image."));

        new Thread(task).start();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
