package com.example.nutrilens.controller;


import com.example.nutrilens.model.NutritionInfo;
import com.example.nutrilens.service.NutritionLogManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.List;

public class LogController {
    @FXML private ListView<String> logList;

    public void initialize() {
        loadLog();
    }

    public void loadLog() {
        String today = LocalDate.now().toString();
        List<NutritionInfo> entries = NutritionLogManager.getEntriesForDate(today);

        logList.getItems().clear();
        for (NutritionInfo entry : entries) {
            logList.getItems().add(entry.getFoodName() + " - " +
                    entry.getCalories() + " kcal, " +
                    entry.getProtein() + "g protein");
        }
    }
}
