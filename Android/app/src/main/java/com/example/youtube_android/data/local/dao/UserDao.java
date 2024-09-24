package com.example.youtube_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube_android.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    // Insert a user; replace if the user already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateUser(User user);

    // Get the current logged-in user
    @Query("SELECT * FROM user WHERE isLogged = 1 LIMIT 1")
    User getCurrentUser();

    // Update the logged-in status of a specific user
    @Query("UPDATE user SET isLogged = :isLogged WHERE id = :userId")
    void updateUserLoginStatus(String userId, boolean isLogged);

    // Delete a user
    @Delete
    void deleteUser(User user);
}

