package com.koconr.smspam.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.VisibleForTesting;


import com.koconr.smspam.database.dao.MessageDao;
import com.koconr.smspam.model.Message;

import java.util.Date;
import java.util.List;

public class DataBaseCache {

    private static DataBaseCache dataBaseCache;

    public static DataBaseCache getDataBaseCache(Context context) {
        if (dataBaseCache == null) dataBaseCache = new DataBaseCache(context);
        return dataBaseCache;
    }

    private AppDatabase db;
    private MessageDao messageDao;
    private List<Message> messages;

    @VisibleForTesting
    public DataBaseCache(AppDatabase db, MessageDao messageDao, List<Message> messages) {
        this.db = db;
        this.messageDao = messageDao;
        this.messages = messages;
    }

    private DataBaseCache(Context context) {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name").build();
        messageDao = db.messageDao();
        messages = messageDao.getAll();
    }

    public List<Message> getAllMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messageDao.insert(message);
        messages.add(message);
    }

    public void addMessage(String sender, String content, float spamProbability, Date date) {
        Message message = new Message(sender, content, spamProbability, date.getTime());
        this.addMessage(message);
    }

    public void deleteMessage(Message message) {
        messageDao.delete(message);
        messages.remove(message);
    }

    public void deleteMessage(int id) {
        messageDao.delete(id);

        messages.stream()
                .filter(message -> message.uid == id)
                .findFirst()
                .ifPresent(message -> messages.remove(message));
    }




}
