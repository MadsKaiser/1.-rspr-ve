package com.example.agrisys;

import java.util.HashMap;
// Benjamin
public class UserManager {

    private static UserManager instance;
    private HashMap<String, String> users;
    private HashMap<String, String> roles;
    private String currentUser; // Variable to store the current user

    private UserManager() {
        // Laver en liste over brugere og deres roller
        users = new HashMap<>();
        roles = new HashMap<>();
        // Tilføjer brugere og deres roller
        users.put("s", "s");
        roles.put("s", "SUPERUSER");

        users.put("b", "b");
        roles.put("b", "USER");
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public HashMap<String, String> getRoles() {
        return roles;
    }

    // Set current user hvis username findes i users
    public void setCurrentUser(String username) {
        if (username != null && users.containsKey(username)) {
            currentUser = username;
        }
    }

    // får current user
    public String getCurrentUser() {
        return currentUser != null ? currentUser : "";
    }

}