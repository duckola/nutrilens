package com.example.nutrilens.controller;

import com.example.nutrilens.service.GoalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GoalController {
    @FXML private TextField caloriesField, proteinField, fatField, carbField;
    @FXML private Label statusLabel;

    public void handleSaveGoal() {
        try {
            double cal = Double.parseDouble(caloriesField.getText());
            double pro = Double.parseDouble(proteinField.getText());
            double fat = Double.parseDouble(fatField.getText());
            double carb = Double.parseDouble(carbField.getText());

            GoalManager.Goal goal = new GoalManager.Goal(cal, pro, fat, carb);
            GoalManager.saveGoal(goal);

            statusLabel.setText("✅ Goals saved!");
        } catch (NumberFormatException e) {
            statusLabel.setText("❌ Invalid input");
        } catch (Exception ex) {
            statusLabel.setText("❌ Error: " + ex.getMessage());
        }
    }
}
