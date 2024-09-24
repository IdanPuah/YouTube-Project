package com.example.youtube_android.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube_android.data.local.dao.UserDao;
import com.example.youtube_android.data.remote.retrofit.LoginResponse;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.remote.retrofit.SignUpResponse;
import com.example.youtube_android.data.remote.retrofit.UserAPI;
import com.example.youtube_android.util.LoggedManager;
import com.example.youtube_android.util.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private UserAPI userApi;
    private SharedPreferences sharedPreferences;
    private LoggedManager loggedManager;
    private MutableLiveData<Integer> loginResult = new MutableLiveData<>();
    private MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // For background tasks
    private MutableLiveData<Integer> signUpResult = new MutableLiveData<>();

    public UserRepository(UserDao userDao, UserAPI userApi) {
        this.userDao = userDao;
        this.userApi = userApi;
        this.sharedPreferences = MyApplication.getAppContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        this.loggedManager = LoggedManager.getInstance();
        refreshCurrentUser(); // Initialize current user

    }

    public LiveData<Integer> getLoginResult() {
        return loginResult;
    }

    public LiveData<User> getCurrentUser() {
        return currentUserLiveData;
    }


    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();
        loggedManager.setToken(token); // Update token in LoggedManager
    }

    public String getToken() {
        return sharedPreferences.getString("jwt_token", null);
    }

    public boolean isUserLoggedIn() {
        return loggedManager.getIsLogged();
    }

    public void refreshCurrentUser() {
        executorService.execute(() -> {
            User currentUser = userDao.getCurrentUser();
            currentUserLiveData.postValue(currentUser);
        });
    }

    public LiveData<Integer> getSignUpResult() {
        return signUpResult;
    }

    public void signUp(String username, String email, String password, Uri profileImg) {
        MutableLiveData<SignUpResponse> signUpResponseLiveData = new MutableLiveData<>();
        userApi.signUp(username, email, password, profileImg, signUpResponseLiveData);

        signUpResponseLiveData.observeForever(signUpResponse -> {
            if (signUpResponse != null) {
                if (signUpResponse.getStatus() == SignUpResponse.Status.SUCCESS) {
                    User user = signUpResponse.getUser();
                    String token = signUpResponse.getToken();
                    saveToken(token);

                    // Store the user details locally in Room
                    executorService.execute(() -> {
                        userDao.insertOrUpdateUser(user);
                        loggedManager.setLogged(true);
                        loggedManager.setCurrentUser(user);

                        signUpResult.postValue(0); // Indicate success
                    });
                } else if (signUpResponse.getStatus() == SignUpResponse.Status.FAILURE_EMAIL_EXISTS) {
                    signUpResult.postValue(1); // Email already exists
                } else if (signUpResponse.getStatus() == SignUpResponse.Status.FAILURE_USER_EXISTS) {
                    signUpResult.postValue(2);
                } else if (signUpResponse.getStatus() == SignUpResponse.Status.FAILURE_NETWORK) {
                    signUpResult.postValue(3); // Network error or null response Out of memory
                } else if (signUpResponse.getStatus() == SignUpResponse.Status.FAILURE_OUT_OF_MEMORY) {
                    signUpResult.postValue(4); // Out of memory General failure

                } else {
                    signUpResult.postValue(5); // General failure
                }
            }
        });
    }

    public void login(String email, String password) {
        MutableLiveData<LoginResponse> loginResponseLiveData = new MutableLiveData<>();
        userApi.login(email, password, loginResponseLiveData);

        loginResponseLiveData.observeForever(loginResponse -> {
            if (loginResponse != null) {
                if (loginResponse.getStatus() == LoginResponse.Status.SUCCESS) {
                    User user = loginResponse.getUser();
                    String token = loginResponse.getToken();
                    saveToken(token);

                    // Update logged-in status and current user in Room
                    executorService.execute(() -> {
                        user.setLogged(true);
                        userDao.insertOrUpdateUser(user);

                        loggedManager.setLogged(true);
                        loggedManager.setCurrentUser(user);
                        loggedManager.setToken(token);

                        currentUserLiveData.postValue(user); // Update LiveData with current user
                        loginResult.postValue(0); // Indicate success
                    });
                } else if (loginResponse.getStatus() == LoginResponse.Status.FAILURE_INVALID_CREDENTIALS) {
                    loginResult.postValue(1);

                } else if (loginResponse.getStatus() == LoginResponse.Status.FAILURE_OUT_OF_MEMORY) {
                    loginResult.postValue(2);
                } else if (loginResponse.getStatus() == LoginResponse.Status.FAILURE_OTHER) {
                    loginResult.postValue(3);
                } else {
                    loginResult.postValue(4); // Indicate unknown failure
                }
            }
        });
    }

    public LiveData<Integer> updateUser(String newUserName, String newMail, String newPassword) {
        MutableLiveData<Integer> isUpdate = new MutableLiveData<>();
        userApi.updateUser(loggedManager.getToken(), loggedManager.getCurrentUser().getId(), newUserName,
                newMail, newPassword, isUpdate);

        loggedManager.getCurrentUser().setUsername(newUserName);
        return isUpdate;
    }

    public LiveData<Boolean> deleteUser(String token, String userId) {
        MutableLiveData<Boolean> userDelete = new MutableLiveData<>();
        userApi.deleteUser(token, userId, userDelete);

        if (Boolean.TRUE.equals(userDelete.getValue())) {
            saveToken(null);
        }
        return userDelete;
    }

}

