package com.example.youtube_android.entities;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private static UserList instance;
    private List<User> usersList;

    public UserList(){
        this.usersList = new ArrayList<>();
    }
    public static synchronized UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    public List<User> getUsersList() {
        return this.usersList;
    }
    public int getUsersListSize() {
        return this.usersList.size();
    }

    public void addUser(User user){
        this.usersList.add(user);
    }

    public void deleteUser(String name, int id){
        for (User user: this.usersList){
            if (user.getName().equals(name) || user.getId() == id){
                this.usersList.remove(user);
                return;
            }
        }
    }

    public int checkLogin(String name, String password) {
        for (User user : this.usersList) {
            if (user.getName().equals(name)) {
                if (user.getPassword().equals(password)) {
                    return 1;  // Login successful
                } else {
                    return 2;  // Incorrect password
                }
            }
        }
        return 0;  // Username does not exist
    }
    public User searchForTheUser(String name, String password) {
        for (User user : this.usersList) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;  // Username does not exist
    }

}
