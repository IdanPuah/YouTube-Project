package com.example.youtube_android.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube_android.data.Converters;
import com.example.youtube_android.data.local.dao.UserDao;
import com.example.youtube_android.data.local.dao.VideoDao;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.Video;

@Database(entities = {User.class, Video.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class LocalAppDB extends RoomDatabase {

    private static LocalAppDB instance;

    public abstract UserDao userDao();
    public abstract VideoDao videoDao();

    public static synchronized LocalAppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalAppDB.class, "DB")
                    .build();
        }
        return instance;
    }

    public static synchronized LocalAppDB getInstance() {
        return instance;
    }
}
