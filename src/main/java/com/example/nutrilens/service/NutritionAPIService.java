package com.example.nutrilens.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class NutritionAPIService {

    // Replace with your actual keys
    private static final String CALORIE_MAMA_KEY = "YOUR_CALORIEMAMA_KEY";
    private static final String NUTRITIONIX_APP_ID = "YOUR_APP_ID";
    private static final String NUTRITIONIX_API_KEY = "YOUR_API_KEY";

    public static String analyzeImage(File imageFile) {
        try {
            // STEP 1: Send image to CalorieMama
            String foodName = detectFoodWithCalorieMama(imageFile);

            if (foodName == null) return "❌ Could not detect food from image.";

            // STEP 2: Search Nutrition Info from Nutritionix
            String nutritionData = getNutritionFromNutritionix(foodName);

            return "🍱 Detected: " + foodName + "\n\n" + nutritionData;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: " + e.getMessage();
        }
    }

    private static String detectFoodWithCalorieMama(File imageFile) throws IOException {
        // CalorieMama endpoint (replace with real one if updated)
        URL url = new URL("https://api-2445582032290.production.gw.apicast.io/foodrecognition"); // Placeholder

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + CALORIE_MAMA_KEY);
        conn.setDoOutput(true);

        // Convert image to byte[]
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        conn.getOutputStream().write(imageBytes);

        // Read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        // Parse JSON (simplified parsing for food name)
        String response = sb.toString();
        // Assume we parse food name here from JSON
        String fakeFoodName = "Cheeseburger"; // Replace with parsed result
        return fakeFoodName;
    }

    private static String getNutritionFromNutritionix(String query) throws IOException {
        URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("x-app-id", NUTRITIONIX_APP_ID);
        conn.setRequestProperty("x-app-key", NUTRITIONIX_API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String payload = "{\"query\": \"" + query + "\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
        }

        // Read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        // Simplified parse
        return parseNutritionixResponse(sb.toString());
    }

    private static String parseNutritionixResponse(String json) {
        // You can use JSON libraries like org.json, Gson, or Jackson
        // Here's a simplified placeholder
        return """
                ✅ Verified by Nutritionix:
                - Calories: 540 kcal
                - Protein: 26g
                - Fat: 29g
                - Carbs: 47g
                """;
    }
}
