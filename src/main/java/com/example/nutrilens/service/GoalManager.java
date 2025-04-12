package com.example.nutrilens.service;


import java.io.*;

public class GoalManager {
    private static final String GOAL_FILE = "user_goals.ser";

    public static class Goal implements Serializable {
        public double calories, protein, fat, carbs;

        public Goal(double calories, double protein, double fat, double carbs) {
            this.calories = calories;
            this.protein = protein;
            this.fat = fat;
            this.carbs = carbs;
        }
    }

    public static void saveGoal(Goal goal) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GOAL_FILE))) {
            oos.writeObject(goal);
        }
    }

    public static Goal loadGoal() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GOAL_FILE))) {
            return (Goal) ois.readObject();
        } catch (Exception e) {
            return new Goal(2000, 75, 70, 250); // default goal
        }
    }
}
