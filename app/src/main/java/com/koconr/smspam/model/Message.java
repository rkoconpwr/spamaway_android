package com.koconr.smspam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

    public Message(int uid, String sender, String content) {
        this.uid = uid;
        this.sender = sender;
        this.content = content;
    }

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "content")
    public String content;
}
