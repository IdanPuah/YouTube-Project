package com.example.youtube_android.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String password;
    private String profileImg;
    private int profileImgRes;
    private Uri profileImgUri;

    // Existing constructors
    public User(int id, String name, String profileImg, String password) {
        this.id = id;
        this.name = name;
        this.profileImg = profileImg;
        this.password = password;
    }

    public User(int id, String name, Uri profileImg, String password) {
        this.id = id;
        this.name = name;
        this.profileImgUri = profileImg;
        this.password = password;
    }

    public User(int id, String name, int profileImg, String password) {
        this.id = id;
        this.name = name;
        this.profileImgRes = profileImg;
        this.password = password;
    }

    // Parcelable constructor
    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        password = in.readString();
        profileImg = in.readString();
        profileImgRes = in.readInt();
        profileImgUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(profileImg);
        dest.writeInt(profileImgRes);
        dest.writeParcelable(profileImgUri, flags);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    // Existing getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public int getProfileImgRes() {
        return profileImgRes;
    }

    public String getPassword() {
        return password;
    }

    public Uri getProfileImgUri() {
        return profileImgUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setProfileImgRes(int profileImgRes) {
        this.profileImgRes = profileImgRes;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}