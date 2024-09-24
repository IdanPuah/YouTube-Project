package com.example.youtube_android.data.remote.retrofit;


import android.util.Base64;

import com.example.youtube_android.data.model.SignUpUserResponse;
import com.example.youtube_android.data.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    // Enum to represent the status of the login response
    public enum Status {
        SUCCESS,
        FAILURE_EMAIL_EXISTS,
        FAILURE_USER_EXISTS,
        FAILURE_NETWORK,
        FAILURE_OUT_OF_MEMORY,
        FAILURE_OTHER
    }

    // The status of the login response
    private SignUpResponse.Status status;

    @SerializedName("user")
    private JsonObject userJson;

    @SerializedName("token")
    private String token;

    // Constructor for failure cases where only status is needed
    public SignUpResponse(SignUpResponse.Status status) {
        this.status = status;
    }

    // Constructor for success cases where status, userJson, and token are provided
    public SignUpResponse(SignUpResponse.Status status, JsonObject userJson, String token) {
        this.status = status;
        this.userJson = userJson;
        this.token = token;
    }

    public SignUpResponse.Status getStatus() {
        return status;
    }

    public void setStatus(SignUpResponse.Status status) {
        this.status = status;
    }

    public User getUser() {
        if (userJson != null) {
            Gson gson = new Gson();
            SignUpUserResponse serverUser = gson.fromJson(userJson, SignUpUserResponse.class);
            return convertMongoUserToUser(serverUser);
        }
        return null;
    }

    private User convertMongoUserToUser(SignUpUserResponse serverUser) {
        if (serverUser != null) {
            User user = new User();

            user.setId(serverUser.getId()); // Use getter method
            user.setUsername(serverUser.getUsername()); // Use getter method
            user.setEmail(serverUser.getEmail()); // Use getter method
            user.setUploads(serverUser.getUploads()); // Use getter method
            user.setSubscription(serverUser.getSubscription()); // Use getter method

            if (serverUser.getPhoto() != null) { // Use getter method
                User.Photo photo2 = new User.Photo();
                photo2.setContentType(serverUser.getPhoto().getContentType()); // Use getter method

                if (serverUser.getPhoto().getData() != null) { // Use getter method
                    // Handle the Base64 encoding and conversion
                    byte[] photoData = convertIntArrayToByteArray(serverUser.getPhoto().getData().getData()); // Use getter method
                    photo2.setData(Base64.encodeToString(photoData, Base64.NO_WRAP));
                }

                user.setPhoto(photo2);
            }
            return user;

        } else {
            return null;
        }
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
