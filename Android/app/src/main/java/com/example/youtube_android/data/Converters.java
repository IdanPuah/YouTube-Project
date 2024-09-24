package com.example.youtube_android.data;

import android.net.Uri;

import androidx.room.TypeConverter;

import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    @TypeConverter
    public static String fromPhoto(User.Photo photo) {
        if (photo == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(photo);
    }

    @TypeConverter
    public static User.Photo toPhoto(String photoString) {
        if (photoString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<User.Photo>() {
        }.getType();
        return gson.fromJson(photoString, type);
    }

    // New converters for User object
    @TypeConverter
    public static String fromUser(User user) {
        if (user == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    @TypeConverter
    public static User toUser(String userString) {
        if (userString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        return gson.fromJson(userString, type);
    }

    @TypeConverter
    public static String fromCommentList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(comments);
    }

    @TypeConverter
    public static List<Comment> toCommentList(String commentsString) {
        if (commentsString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.fromJson(commentsString, type);
    }
}
