package com.example.youtube_android.data.remote.retrofit;


public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String profileImg;

    public SignUpRequest(String username, String email, String password, String profileImg) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
    }

}

