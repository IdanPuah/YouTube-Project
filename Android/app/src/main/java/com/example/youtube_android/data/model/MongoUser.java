package com.example.youtube_android.data.model;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import android.util.Base64;
import com.google.gson.annotations.SerializedName;

public class MongoUser {

    @SerializedName("_doc")
    private UserDoc doc;

    public UserDoc getDoc() {
        return doc;
    }

    public void setDoc(UserDoc doc) {
        this.doc = doc;
    }

    public static class UserDoc {
        @SerializedName("_id")
        private String id;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

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
    }

    // Method to create User from MongoUser
    public User createUserFromMongoUser(MongoUser mongoUser) {
        User user = new User();
        UserDoc doc = mongoUser.getDoc(); // Use getter method

        user.setId(doc.getId()); // Use getter method
        user.setUsername(doc.getUsername()); // Use getter method
        user.setEmail(doc.getEmail()); // Use getter method
        // Don't set the password in the User object for security reasons
        user.setUploads(doc.getUploads()); // Use getter method
        user.setSubscription(doc.getSubscription()); // Use getter method

        if (doc.getPhoto() != null) { // Use getter method
            User.Photo photo2 = new User.Photo();
            photo2.setContentType(doc.getPhoto().getContentType()); // Use getter method

            if (doc.getPhoto().getData() != null) { // Use getter method
                // Convert int array to byte array
                byte[] photoData = new byte[doc.photo.data.data.length];
                for (int i = 0; i < doc.photo.data.data.length; i++) {
                    photoData[i] = (byte) doc.photo.data.data[i];
                }
                // Encode to Base64 string and set to Photo
                photo2.setData(Base64.encodeToString(photoData, Base64.NO_WRAP)); // Use NO_WRAP to avoid line breaks
            }

            user.setPhoto(photo2);
        }

        return user;
    }
}

