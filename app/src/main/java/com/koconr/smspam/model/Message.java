package com.koconr.smspam.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.koconr.smspam.services.ContactsProvider;

import java.io.Serializable;

@Entity
public class Message implements Serializable {

    public Message(String sender, String content, float spamProbability, long date) {
        this.spamProbability = spamProbability;
        this.content = content;
        this.sender = sender;
        this.date = date;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "spamProbability")
    public float spamProbability;

    @ColumnInfo(name = "date")
    public long date;

    public String getSenderName() {
        String telNumber = this.sender;
        return ContactsProvider.getContactName(telNumber);
    }

}
