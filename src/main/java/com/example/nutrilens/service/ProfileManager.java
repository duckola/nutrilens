package com.example.nutrilens.service;


import com.example.nutrilens.model.UserProfile;

import java.io.*;

public class ProfileManager {
    private static final String PROFILE_PATH = "user_profile.ser";

    public static void saveProfile(UserProfile profile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFILE_PATH))) {
            oos.writeObject(profile);
        }
    }

    public static UserProfile loadProfile() throws IOException, ClassNotFoundException {
        File file = new File(PROFILE_PATH);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (UserProfile) ois.readObject();
        }
    }
}
