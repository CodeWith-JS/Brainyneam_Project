package com.example.brainyneamproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class UserInfo {

    private static final String PREF_NAME = "user_store";
    private final SharedPreferences prefs;

    public UserInfo(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private String key(String email, String field) {
        return email.trim().toLowerCase() + "_" + field;
    }

    public boolean userExists(String email) {
        return prefs.contains(key(email, "password"));
    }

    public void saveUser(String name, String contact, String email, String password) {
        prefs.edit()
                .putString(key(email, "name"), name)
                .putString(key(email, "contact"), contact)
                .putString(key(email, "password"), hash(password))
                .apply();
    }

    public boolean checkLogin(String email, String password) {
        String saved = prefs.getString(key(email, "password"), null);
        return saved != null && saved.equals(hash(password));
    }

    public void updatePassword(String email, String newPassword) {
        prefs.edit().putString(key(email, "password"), hash(newPassword)).apply();
    }

    public String getName(String email) {
        return prefs.getString(key(email, "name"), "");
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
