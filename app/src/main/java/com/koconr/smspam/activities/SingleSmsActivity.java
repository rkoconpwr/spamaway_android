package com.koconr.smspam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.koconr.smspam.R;
import com.koconr.smspam.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SingleSmsActivity extends AppCompatActivity {

    private Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sms);

        message = (Message) getIntent().getSerializableExtra("myMessage");
        this.setupElements();
    }

    private void setupElements() {


        if (message != null) {
            TextView smsTitle = findViewById(R.id.smsTextView1);
            TextView smsDate = findViewById(R.id.smsTextView2);
            TextView smsMessage = findViewById(R.id.smsTextView3);
            Locale locale =  this.getResources().getConfiguration().getLocales().get(0);
            Date date = new Date(message.date);

            smsTitle.setText(message.sender);
            smsDate.setText(new SimpleDateFormat("dd MMM, HH:mm", locale).format(date));
            smsMessage.setText(message.content);
        }
    }
}
