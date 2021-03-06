package com.koconr.smspam.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.koconr.smspam.database.dao.MessageDao;
import com.koconr.smspam.model.Message;

@Database(entities = {Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
}
