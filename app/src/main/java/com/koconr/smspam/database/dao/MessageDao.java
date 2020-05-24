package com.koconr.smspam.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.koconr.smspam.model.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM Message")
    List<Message> getAll();

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM Message WHERE uid = :messageId")
    void delete(int messageId);
}
