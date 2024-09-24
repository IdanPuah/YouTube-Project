package com.example.youtube_android.data.remote.retrofit;

import android.util.Base64;

import com.example.youtube_android.data.model.MongoUser;
import com.example.youtube_android.data.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    // Enum to represent the status of the login response
    public enum Status {
        SUCCESS,
        FAILURE_INVALID_CREDENTIALS,
        FAILURE_OUT_OF_MEMORY,
        FAILURE_OTHER,
        FAILURE_NETWORK
    }

    // The status of the login response
    private Status status;

    @SerializedName("user")
    private JsonObject userJson;

    @SerializedName("token")
    private String token;

    // Constructor for failure cases where only status is needed
    public LoginResponse(Status status) {
        this.status = status;
    }

    // Constructor for success cases where status, userJson, and token are provided
    public LoginResponse(Status status, JsonObject userJson, String token) {
        this.status = status;
        this.userJson = userJson;
        this.token = token;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        if (userJson != null) {
            Gson gson = new Gson();
            MongoUser mongoUser = gson.fromJson(userJson, MongoUser.class);
            return convertMongoUserToUser(mongoUser);
        }
        return null;
    }

    private User convertMongoUserToUser(MongoUser mongoUser) {
        User user = new User();
        MongoUser.UserDoc doc = mongoUser.getDoc(); // Use getter method

        user.setId(doc.getId()); // Use getter method
        user.setUsername(doc.getUsername()); // Use getter method
        user.setEmail(doc.getEmail()); // Use getter method
        user.setUploads(doc.getUploads()); // Use getter method
        user.setSubscription(doc.getSubscription()); // Use getter method

        if (doc.getPhoto() != null) { // Use getter method
            User.Photo photo2 = new User.Photo();
            photo2.setContentType(doc.getPhoto().getContentType()); // Use getter method

            if (doc.getPhoto().getData() != null) { // Use getter method
                // Handle the Base64 encoding and conversion
                byte[] photoData = convertIntArrayToByteArray(doc.getPhoto().getData().getData()); // Use getter method
                photo2.setData(Base64.encodeToString(photoData, Base64.NO_WRAP));
            }

            user.setPhoto(photo2);
        }

        return user;
    }

    private byte[] convertIntArrayToByteArray(int[] data) {
        byte[] byteArray = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            byteArray[i] = (byte) data[i];
        }
        return byteArray;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
