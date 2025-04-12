package com.example.nutrilens.controller;

import com.example.nutrilens.api.JsonParserHelper;
import com.example.nutrilens.api.LogMealService;
import com.example.nutrilens.model.NutritionInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CaptureController {

    @FXML private ImageView previewImage;
    @FXML private TextArea resultArea;

    private File selectedImageFile;

    public void handleChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Food Image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        selectedImageFile = chooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image img = new Image(selectedImageFile.toURI().toString());
            previewImage.setImage(img);
        }
    }
    public void handleDetectFood() {
        if (selectedImageFile == null) {
            resultArea.setText("Please select an image first.");
            return;
        }

        new Thread(() -> {
            try {
                String segmentationResponse = LogMealService.segmentMultipleFoods(selectedImageFile);
                List<NutritionInfo> detectedFoods = JsonParserHelper.parseLogMealSegmentationResponse(segmentationResponse);

                String nutritionResponse = LogMealService.getNutritionForFoods(selectedImageFile);
                List<NutritionInfo> fullNutritionList = JsonParserHelper.parseNutritionResponse(nutritionResponse);

                updateResultAreaMultiple(fullNutritionList);

            } catch (IOException e) {
                e.printStackTrace();
                updateResultAreaError("Error: " + e.getMessage());
            }
        }).start();
    }

    private void updateResultAreaMultiple(List<NutritionInfo> nutritionList) {
        StringBuilder result = new StringBuilder("🍽️ Detected Food Items:\n\n");
        for (NutritionInfo info : nutritionList) {
            result.append("• Food: ").append(info.getFoodName()).append("\n")
                    .append("  - Calories: ").append(info.getCalories()).append(" kcal\n")
                    .append("  - Protein: ").append(info.getProtein()).append(" g\n")
                    .append("  - Fat: ").append(info.getFat()).append(" g\n")
                    .append("  - Carbs: ").append(info.getCarbs()).append(" g\n\n");
        }

        javafx.application.Platform.runLater(() -> resultArea.setText(result.toString()));
    }

    private void updateResultAreaError(String message) {
        javafx.application.Platform.runLater(() -> resultArea.setText(message));
    }


    private void updateResultArea(NutritionInfo nutritionInfo) {
        String resultText = "🍴 Food Detected:\n"
                + "Food: " + nutritionInfo.getFoodName() + "\n"
                + "Calories: " + nutritionInfo.getCalories() + "\n"
                + "Protein: " + nutritionInfo.getProtein() + "g\n"
                + "Fat: " + nutritionInfo.getFat() + "g\n"
                + "Carbs: " + nutritionInfo.getCarbs() + "g\n"
                + "Date: " + nutritionInfo.getDate();
        resultArea.setText(resultText);
    }


}
