package com.example.youtube_android.data.model;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

public class SignUpUserResponse {

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("uploads")
    private String uploads;

    @SerializedName("subscription")
    private String subscription;

    @SerializedName("photo")
    private Photo photo;

    @SerializedName("__v")
    private int version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class Photo {
        @SerializedName("data")
        private PhotoData data;

        @SerializedName("contentType")
        private String contentType;

        public PhotoData getData() {
            return data;
        }

        public void setData(PhotoData data) {
            this.data = data;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public static class PhotoData {
            @SerializedName("type")
            private String type;

            @SerializedName("data")
            private int[] data;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int[] getData() {
                return data;
            }

            public void setData(int[] data) {
                this.data = data;
            }
        }
    }


    // Method to create User from MongoUser
    public User createUserFromMongoUser() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setUploads(this.uploads);
        user.setSubscription(this.subscription);

        if (this.photo != null) {
            User.Photo photo2 = new User.Photo();
            photo2.setContentType(this.photo.getContentType());

            if (this.photo.getData() != null && this.photo.getData().getData() != null) {
                byte[] photoData = new byte[this.photo.getData().getData().length];
                for (int i = 0; i < this.photo.getData().getData().length; i++) {
                    photoData[i] = (byte) this.photo.getData().getData()[i];
                }
                photo2.setData(Base64.encodeToString(photoData, Base64.NO_WRAP));
            }

            user.setPhoto(photo2);
        }

        return user;
    }
}