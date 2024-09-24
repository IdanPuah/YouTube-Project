package com.example.youtube_android.data.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.net.URL;

@Entity
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    @NonNull

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
    private Photo photo; // Integrating User2.Photo

    // Local fields
    private int profileImgRes;
    private String profileImgUri;
    private boolean isLogged;

    public static class Photo implements Parcelable {
        @SerializedName("data")
        private String data;

        @SerializedName("contentType")
        private String contentType;

        public Photo() {
            // Empty constructor
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        // Convert Base64 data to Bitmap
        public Bitmap getBitmap() {
            if (data != null) {
                byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            return null;
        }

        // Parcelable implementation for Photo
        protected Photo(Parcel in) {
            data = in.readString();
            contentType = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(data);
            dest.writeString(contentType);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Photo> CREATOR = new Creator<Photo>() {
            @Override
            public Photo createFromParcel(Parcel in) {
                return new Photo(in);
            }

            @Override
            public Photo[] newArray(int size) {
                return new Photo[size];
            }
        };
    }

    // Constructor that matches the server response
    public User(String id, String username, String email, String photoBase64, String contentType, String uploads, String subscription) {
        this.id = id;
        this.username = username;
        this.email = email;

        // Create a new Photo object and set its fields
        this.photo = new Photo();
        this.photo.setData(photoBase64); // Set Base64 data

        if (contentType != null) {
            this.photo.setContentType(contentType); // Set content type
        } else {
            this.photo.setContentType("png"); // Set content type
        }

        if (this.uploads != null) {
            this.uploads = uploads;
        } else {
            this.uploads = "0";
        }

        if (this.subscription != null) {
            this.subscription = subscription;
        } else {
            this.subscription = "0";
        }

        this.isLogged = false; // Default to false, change as needed
    }

    public User() {
        // Empty constructor for Room Database and other uses
    }

    // Another constructor to handle JSON initialization (preserving old functionality)
    public User(JsonObject jsonObject, String packageName, Context context) {
        this.username = jsonObject.get("username").getAsString();
        this.email = jsonObject.get("email").getAsString();
        this.id = jsonObject.get("id").getAsString();

        // Handle photo as a resource or Base64 string
        String imgResourceId = jsonObject.has("photo") ? jsonObject.get("photo").getAsString() : null;
        if (imgResourceId != null) {
            int intResourceId = context.getResources().getIdentifier(imgResourceId, "drawable", context.getPackageName());
            this.profileImgUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + intResourceId).toString();
        }
    }

    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        uploads = in.readString();
        subscription = in.readString();
        photo = new Photo();
        photo.setData(in.readString()); // Read Base64 string
        photo.setContentType(in.readString()); // Read content type
        profileImgRes = in.readInt();
        profileImgUri = in.readString();
        isLogged = in.readByte() != 0; // Read boolean
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(photo != null ? photo.getData() : null); // Write Base64 string
        dest.writeString(photo != null ? photo.getContentType() : null); // Write content type
        dest.writeInt(profileImgRes);
        dest.writeString(profileImgUri);
        dest.writeString(uploads);
        dest.writeString(subscription);
        dest.writeByte((byte) (isLogged ? 1 : 0)); // Write boolean
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

    // Getter and Setter methods
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

    // Local fields getter and setter methods
    public int getProfileImgRes() {
        return profileImgRes;
    }

    public void setProfileImgRes(int profileImgRes) {
        this.profileImgRes = profileImgRes;
    }

    public String getProfileImgUri() {
        return profileImgUri;
    }

    public void setProfileImgUri(String profileImgUri) {
        this.profileImgUri = profileImgUri;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    // Convert photo Base64 to Bitmap
    public Bitmap getPhotoBitmap() {
        return photo != null ? photo.getBitmap() : null;
    }

    // Helper method to load image from URI
    private Bitmap loadImageFromUri(String uri) {
        try {
            return BitmapFactory.decodeStream(new URL(uri).openStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
