package com.example.nutrilens.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String name;
    private int age;
    private String gender;
    private double height; // in cm
    private double weight; // in kg
    private String goal; // e.g., lose weight, gain muscle

    public UserProfile() {}

    public UserProfile(String name, int age, String gender, double height, double weight, String goal) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
}
