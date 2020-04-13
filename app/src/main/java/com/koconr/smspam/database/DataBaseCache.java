package com.koconr.smspam.database;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Room;

import com.koconr.smspam.database.dao.MessageDao;
import com.koconr.smspam.model.Message;

import java.util.List;

public class DataBaseCache {
    private Context context;
    private AppDatabase db;
    private MessageDao messageDao;
    private List<Message> messages;

    @VisibleForTesting
    public DataBaseCache(Context context, AppDatabase db, MessageDao messageDao, List<Message> messages) {
        this.context = context;
        this.db = db;
        this.messageDao = messageDao;
        this.messages = messages;
    }

    public DataBaseCache(Context context) {
        this.context = context;
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

    public void addMessage(String sender, String content) {
        Message message = new Message(findLastId(), sender, content);
        messageDao.insert(message);
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

    private int findLastId() {
        return messages.stream()
                .map(message -> message.uid)
                .max(Integer::compareTo)
                .orElse(0);
    }


}
