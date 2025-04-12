package com.example.nutrilens.controller;


import com.example.nutrilens.model.NutritionInfo;
import com.example.nutrilens.service.GoalManager;
import com.example.nutrilens.service.NutritionLogManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {
    @FXML private ProgressBar caloriesBar, proteinBar, fatBar, carbBar;
    @FXML private Label caloriesLabel, proteinLabel, fatLabel, carbLabel;
    @FXML private TextArea aiSuggestionArea;

    public void initialize() {
        updateDashboard();
    }

    public void updateDashboard() {
        List<NutritionInfo> entries = NutritionLogManager.getEntriesForDate(LocalDate.now().toString());
        GoalManager.Goal goal = GoalManager.loadGoal();

        double totalCal = 0, totalPro = 0, totalFat = 0, totalCarb = 0;
        for (NutritionInfo n : entries) {
            totalCal += n.getCalories();
            totalPro += n.getProtein();
            totalFat += n.getFat();
            totalCarb += n.getCarbs();
        }

        caloriesBar.setProgress(totalCal / goal.calories);
        proteinBar.setProgress(totalPro / goal.protein);
        fatBar.setProgress(totalFat / goal.fat);
        carbBar.setProgress(totalCarb / goal.carbs);

        caloriesLabel.setText("Calories: " + totalCal + "/" + goal.calories);
        proteinLabel.setText("Protein: " + totalPro + "g / " + goal.protein + "g");
        fatLabel.setText("Fat: " + totalFat + "g / " + goal.fat + "g");
        carbLabel.setText("Carbs: " + totalCarb + "g / " + goal.carbs + "g");
    }

    public void generateSuggestion() {
        GoalManager.Goal goal = GoalManager.loadGoal();
        List<NutritionInfo> entries = NutritionLogManager.getEntriesForDate(LocalDate.now().toString());

        double consumedCal = entries.stream().mapToDouble(NutritionInfo::getCalories).sum();

        StringBuilder sb = new StringBuilder("🧠 AI Suggestion:\n");

        if (consumedCal < goal.calories * 0.8) {
            sb.append("You're under your calorie goal. Consider eating energy-dense foods like bananas, oats, or peanut butter.\n");
        } else if (consumedCal > goal.calories * 1.1) {
            sb.append("You're over your calorie goal. Consider a lighter dinner with veggies or soup.\n");
        } else {
            sb.append("You're on track! Maintain balance for the rest of the day. 🎉\n");
        }

        aiSuggestionArea.setText(sb.toString());
    }
}
