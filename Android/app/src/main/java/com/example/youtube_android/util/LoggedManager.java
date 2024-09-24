package com.example.youtube_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.youtube_android.data.model.User;
import com.google.gson.Gson;

public class LoggedManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED = "key_is_logged";
    private static final String KEY_CURRENT_USER = "key_current_user";
    private static final String KEY_TOKEN = "key_token";

    private static LoggedManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private LoggedManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized LoggedManager getInstance() {
        if (instance == null) {
            instance = new LoggedManager(MyApplication.getAppContext().getApplicationContext());
        }
        return instance;
    }

    public boolean getIsLogged() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED, false);
    }

    public void setLogged(boolean logged) {
        editor.putBoolean(KEY_IS_LOGGED, logged);
        editor.apply();
    }

    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(KEY_CURRENT_USER, null);
        return gson.fromJson(userJson, User.class);
    }

    public void setCurrentUser(User currentUser) {
        String userJson = gson.toJson(currentUser);
        editor.putString(KEY_CURRENT_USER, userJson);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public void clearSession() {
        editor.remove(KEY_IS_LOGGED);
        editor.remove(KEY_CURRENT_USER);
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
