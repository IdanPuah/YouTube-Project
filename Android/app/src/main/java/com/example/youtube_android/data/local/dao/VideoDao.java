package com.example.youtube_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.youtube_android.data.model.Video;

import java.util.Collections;
import java.util.List;
@Dao
public interface VideoDao {

    @Query("SELECT * FROM video")
    List<Video> getTwentyVideos();

    @Query("SELECT * FROM video WHERE _id = :id")
    Video getVideo(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Video> videoList);

    @Transaction
    default void insertIfNotExists(List<Video> videoList) {
        for (Video video : videoList) {
            if (getVideo(video.get_id()) == null) {
                insert(Collections.singletonList(video));
            }
        }
    }

    @Update
    void update(Video... videos);

    @Delete
    void  delete(Video... videos);
}
