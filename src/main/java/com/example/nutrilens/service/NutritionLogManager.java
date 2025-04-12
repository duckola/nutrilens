package com.example.nutrilens.service;



import com.example.nutrilens.model.NutritionInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NutritionLogManager {
    private static final String LOG_FILE = "nutrition_log.ser";

    public static void addEntry(NutritionInfo entry) throws IOException {
        List<NutritionInfo> entries = loadEntries();
        entries.add(entry);
        saveEntries(entries);
    }

    public static List<NutritionInfo> loadEntries() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LOG_FILE))) {
            return (List<NutritionInfo>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static void saveEntries(List<NutritionInfo> entries) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOG_FILE))) {
            oos.writeObject(entries);
        }
    }

    public static List<NutritionInfo> getEntriesForDate(String date) {
        return loadEntries().stream()
                .filter(entry -> entry.getDate().equals(date))
                .collect(Collectors.toList());
    }
}
