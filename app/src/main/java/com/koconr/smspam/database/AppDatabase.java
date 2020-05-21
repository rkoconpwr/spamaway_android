package com.koconr.smspam.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.koconr.smspam.database.dao.MessageDao;
import com.koconr.smspam.model.Message;

@Database(entities = {Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
}
