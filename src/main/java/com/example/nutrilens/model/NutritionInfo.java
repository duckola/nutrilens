package com.example.nutrilens.model;


import java.io.Serializable;

public class NutritionInfo implements Serializable {
    private String foodName;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private String date; // Format: YYYY-MM-DD

    public NutritionInfo(String foodName, double calories, double protein, double fat, double carbs, String date) {
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.date = date;
    }

    // Getters
    public String getFoodName() { return foodName; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
    public String getDate() { return date; }
}
