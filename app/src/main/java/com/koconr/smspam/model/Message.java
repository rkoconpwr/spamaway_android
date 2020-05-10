package com.koconr.smspam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

    public Message(float spamProbability, String content) {
          this.spamProbability = spamProbability;
        this.content = content;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "spamProbability")
    public float spamProbability;

}
