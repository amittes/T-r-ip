package com.example.t_r_ip.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.t_r_ip.model.entities.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post where isDeleted = :isDeleted")
    LiveData<List<Post>> getAll(Boolean isDeleted);

    @Query("select * from Post where id = :postId")
    LiveData<Post> getPostById(String postId);

    @Query("select * from Post where authorId = :authorId")
    LiveData<List<Post>> getPostsByAuthorId(String authorId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);
}

