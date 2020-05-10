package com.koconr.smspam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

    public Message(String sender, String content, float spamProbability) {
        this.spamProbability = spamProbability;
        this.content = content;
        this.sender = sender;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "spamProbability")
    public float spamProbability;

}
