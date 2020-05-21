package com.koconr.smspam.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Message {

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

}
