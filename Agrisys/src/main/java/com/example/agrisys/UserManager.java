package com.example.agrisys;

import java.util.HashMap;

public class UserManager {

    private static UserManager instance;
    private HashMap<String, String> users;
    private HashMap<String, String> roles;
    private String currentUser; // Variable to store the current user

    private UserManager() {
        users = new HashMap<>();
        roles = new HashMap<>();

        // Add a superuser and a normal user
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

    // Set the current logged-in user
    public void setCurrentUser(String username) {
        if (username != null && users.containsKey(username)) {
            currentUser = username;
        }
    }

    // Get the current logged-in user
    public String getCurrentUser() {
        return currentUser != null ? currentUser : "";
    }

    // Reset the current user (e.g., on logout)
    public void resetCurrentUser() {
        currentUser = null;
    }
}