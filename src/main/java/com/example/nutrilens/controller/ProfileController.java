package com.example.nutrilens.controller;


import com.example.nutrilens.model.UserProfile;
import com.example.nutrilens.service.ProfileManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileController {

    @FXML private TextField nameField, ageField, heightField, weightField;
    @FXML private ChoiceBox<String> genderBox, goalBox;

    @FXML
    public void initialize() {
        genderBox.getItems().addAll("Male", "Female", "Other");
        goalBox.getItems().addAll("Lose Weight", "Maintain", "Gain Muscle");

        try {
            UserProfile profile = ProfileManager.loadProfile();
            if (profile != null) {
                nameField.setText(profile.getName());
                ageField.setText(String.valueOf(profile.getAge()));
                genderBox.setValue(profile.getGender());
                heightField.setText(String.valueOf(profile.getHeight()));
                weightField.setText(String.valueOf(profile.getWeight()));
                goalBox.setValue(profile.getGoal());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveProfile() {
        try {
            UserProfile profile = new UserProfile(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    genderBox.getValue(),
                    Double.parseDouble(heightField.getText()),
                    Double.parseDouble(weightField.getText()),
                    goalBox.getValue()
            );
            ProfileManager.saveProfile(profile);
            showAlert("Success", "Profile saved successfully.");
        } catch (Exception e) {
            showAlert("Error", "Failed to save profile: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
