package com.koconr.smspam.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koconr.smspam.database.AppExecutors;
import com.koconr.smspam.database.DataBaseCache;
import com.koconr.smspam.model.SpamProbabilityModel;
import com.koconr.smspam.params.Params;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {
    private final static String CONTENT_KEY = "content";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                this.getIsSpamResponse(smsMessage, context);
            }
        }
    }

    private void getIsSpamResponse(SmsMessage smsMessage, Context context) {
        HttpsTrustManager.allowAllSSL();
        String smsToCheck = smsMessage.getMessageBody();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url;
        try {
            url = Params.getUrl(Params.CHECK_IF_SPAM);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
        final Notifications notification = new Notifications();

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(CONTENT_KEY, smsToCheck);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        Log.i("BODY FORMAT", requestBody);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    // textView.setText("Response is: "+ response.substring(0,500));
                    Log.i("MESSAGE RECEIVED!", response);
                    SpamProbabilityModel spamProbability = notification.isSpam(response);
                    if (spamProbability.isSpam()) {
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String content = smsMessage.getMessageBody();

                        AppExecutors.getInstance().databaseThread().execute(
                                () -> {
                                    final DataBaseCache dataBaseCache = DataBaseCache.getDataBaseCache(context);
                                    dataBaseCache.addMessage(sender, content, spamProbability.getSpamPropability());
                                }
                        );
                        notification.displayNotification(smsMessage, context, spamProbability.getSpamPropability());
                    }
                },
                error -> Log.e("RESPONSE ERRORLY", new String(error.networkResponse != null ? error.networkResponse.data : new byte[]{}))) {
            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}
