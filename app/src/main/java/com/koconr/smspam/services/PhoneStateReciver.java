package com.koconr.smspam.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class PhoneStateReciver extends BroadcastReceiver {

    private String blockingNumber = "XX-XXX-XXXXX";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        //blocking sms for matched number

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }

            final String numberSms = smsMessage[0].getOriginatingAddress();
            //final String messageSms = smsMessage[0].getDisplayMessageBody();
            //long dateTimeSms = smsMessage[0].getTimestampMillis();

            //block sms if number is matched to our blocking number
            if (numberSms.equals(blockingNumber)) {
                abortBroadcast();
            }
        }
    }
}
