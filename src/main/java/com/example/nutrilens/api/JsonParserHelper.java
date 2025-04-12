package com.example.nutrilens.api;

import com.example.nutrilens.model.NutritionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonParserHelper {

    // Extract food names from segmentation response
    public static List<NutritionInfo> parseLogMealSegmentationResponse(String jsonResponse) {
        List<NutritionInfo> foodList = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray foodArray = root.getJSONArray("foodItems");

        for (int i = 0; i < foodArray.length(); i++) {
            JSONObject foodObj = foodArray.getJSONObject(i);
            String name = foodObj.getString("foodName");
            foodList.add(new NutritionInfo(name, 0, 0, 0, 0, LocalDate.now().toString()));
        }
        return foodList;
    }

    // Extract full nutritional values from nutrition estimation response
    public static List<NutritionInfo> parseNutritionResponse(String jsonResponse) {
        List<NutritionInfo> nutritionList = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray foodArray = root.getJSONArray("dishes");

        for (int i = 0; i < foodArray.length(); i++) {
            JSONObject foodObj = foodArray.getJSONObject(i);
            String name = foodObj.getString("name");

            JSONObject nutrients = foodObj.getJSONObject("nutritional_info_per_100g");
            double calories = nutrients.optDouble("energy-kcal", 0);
            double protein = nutrients.optDouble("proteins", 0);
            double fat = nutrients.optDouble("fat", 0);
            double carbs = nutrients.optDouble("carbohydrates", 0);

            nutritionList.add(new NutritionInfo(name, calories, protein, fat, carbs, LocalDate.now().toString()));
        }

        return nutritionList;
    }

    // Extract food IDs from segmentation (if needed for /foodId nutrition)
    public static List<Integer> extractFoodIdsFromSegmentation(String jsonResponse) {
        List<Integer> foodIds = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray foodArray = root.getJSONArray("foodItems");

        for (int i = 0; i < foodArray.length(); i++) {
            JSONObject foodObj = foodArray.getJSONObject(i);
            int id = foodObj.optInt("id", -1);
            if (id != -1) {
                foodIds.add(id);
            }
        }

        return foodIds;
    }

    // Parse Spoonacular fallback response
    public static NutritionInfo parseSpoonacularResponse(String jsonResponse) {
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray results = root.getJSONArray("results");

        if (results.length() == 0) {
            return new NutritionInfo("Unknown", 0, 0, 0, 0, LocalDate.now().toString());
        }

        JSONObject firstResult = results.getJSONObject(0);
        String name = firstResult.getString("name");

        // Spoonacular does not return full macros in this endpoint, just placeholder
        return new NutritionInfo(name, 0, 0, 0, 0, LocalDate.now().toString());
    }

    // Parse LogMeal nutrition JSON (for single foodId-based parsing)
    public static NutritionInfo parseLogMealNutrition(String jsonResponse) {
        JSONObject root = new JSONObject(jsonResponse);
        JSONObject info = root.getJSONObject("nutritional_info_per_100g");

        String name = root.optString("name", "Unknown");
        double calories = info.optDouble("energy-kcal", 0);
        double protein = info.optDouble("proteins", 0);
        double fat = info.optDouble("fat", 0);
        double carbs = info.optDouble("carbohydrates", 0);

        return new NutritionInfo(name, calories, protein, fat, carbs, LocalDate.now().toString());
    }
}
