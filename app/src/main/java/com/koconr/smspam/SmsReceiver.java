package com.koconr.smspam;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RECEIVING", "received message... I GUESS");
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                NotificationCompat.Builder builder = this.buildNotification(smsMessage, context);
                this.displayNotification(builder, context);
            }
        }
    }

    private NotificationCompat.Builder buildNotification(SmsMessage smsMessage, Context context) {
        String messageBody = smsMessage.getMessageBody();
        String messageHeader = smsMessage.getDisplayOriginatingAddress();
        String title = "New SMS from: " + messageHeader;

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
        int notificationId = 314;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }
}
