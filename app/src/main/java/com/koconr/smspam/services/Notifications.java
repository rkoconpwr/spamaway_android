package com.koconr.smspam.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.gson.Gson;
import com.koconr.smspam.R;
import com.koconr.smspam.activities.SmsList;
import com.koconr.smspam.model.SpamProbabilityModel;

public class Notifications {

    public SpamProbabilityModel isSpam(String responseString) {
        Gson gson = new Gson();
        return gson.fromJson(responseString, SpamProbabilityModel.class);
    }

    public void displayNotification(SmsMessage smsMessage, Context context, float spamProbability) {
        NotificationCompat.Builder builder = this.buildNotification(smsMessage, context,spamProbability);
        this.displayNotification(builder, context);
    }

    private NotificationCompat.Builder buildNotification(SmsMessage smsMessage, Context context, double spamProbability) {
        String messageHeader = smsMessage.getDisplayOriginatingAddress();
        String title = "Probable SPAM detected!";
        String messageBody = String.format("Last message from %s is for %f spam", messageHeader, spamProbability);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, SmsList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.drawable.ic_warning_black)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder;
    }

    private void displayNotification(NotificationCompat.Builder builder, Context context) {
        int notificationId = 315;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Log.i("Wywalam powiadomienie", "asdasdasd");
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }
}
