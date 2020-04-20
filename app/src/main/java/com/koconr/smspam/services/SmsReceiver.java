package com.koconr.smspam.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {
    // DOCELOWY ADRES DOMENY
    private final static String BASE_URL = "https://smsspamaway.ew.r.appspot.com/";
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
        String smsToCheck = smsMessage.getMessageBody();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_URL + "isspam";
        final Notifications notification = new Notifications();

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(CONTENT_KEY, smsToCheck);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        Log.i("BODY FORMAT", requestBody);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // textView.setText("Response is: "+ response.substring(0,500));
                        Log.i("MESSAGE RECEIVED!", response);
                        if (notification.isSpam(response)) {
                            notification.displayNotification(smsMessage, context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE ERRORLY", new String(error.networkResponse.data));
                    }
                })  {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



}
