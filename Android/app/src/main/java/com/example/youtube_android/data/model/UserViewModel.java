package com.example.youtube_android.data.model;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube_android.data.local.dao.UserDao;
import com.example.youtube_android.data.local.database.LocalAppDB;
import com.example.youtube_android.data.remote.retrofit.UserAPI;
import com.example.youtube_android.data.repository.UserRepository;
import com.example.youtube_android.util.MyApplication;

public class UserViewModel extends ViewModel {
    private LiveData<Integer> loginResult;
    private LiveData<User> currentUser;
    private UserRepository userRepository;
    private LiveData<Integer> signUpResult;

    public UserViewModel() {
        UserDao userDao = LocalAppDB.getInstance(MyApplication.getAppContext()).userDao();

        UserAPI userAPI = new UserAPI(MyApplication.getAppContext());
        userRepository = new UserRepository(userDao, userAPI);

        // Initialize loginResult as a MutableLiveData in UserRepository
        loginResult = userRepository.getLoginResult();
        currentUser = userRepository.getCurrentUser();

        // Initialize signUpResult from the repository
        signUpResult = userRepository.getSignUpResult();

    }

    public LiveData<Integer> getLoginResult() {
        return loginResult;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void validateLogin(String email, String password) {
        userRepository.login(email, password);
    }

    public boolean isUserLoggedIn() {
        return userRepository.isUserLoggedIn();
    }

    public void saveToken(String token) {
        userRepository.saveToken(token);
    }

    public String getToken() {
        return userRepository.getToken();
    }


    public void signUp(String username, String email, String password, Uri profileImg) {
        userRepository.signUp(username, email, password, profileImg);
    }

    public LiveData<Integer> getSignUpResult() {
        return signUpResult;
    }

    public LiveData<Integer> updateUser(String newUserName, String newMail, String newPassword){return  userRepository.updateUser(newUserName, newMail, newPassword);}

    public LiveData<Boolean> deleteUser(String token, String userId){return userRepository.deleteUser(token, userId);}

}
