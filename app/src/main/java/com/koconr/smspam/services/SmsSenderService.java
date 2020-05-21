package com.koconr.smspam.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.koconr.smspam.model.Message;
import com.koconr.smspam.params.Params;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SmsSenderService {

    public static void postSmsToServerDatabase(Context context, Message message, boolean isSpam) {
        String url;
        try {
            url = Params.getUrl(Params.SEND_TO_DATABASE);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("content", message.content);
            jsonBody.put("isSpam", isSpam);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("POSITIVELY SENT TO DATABASE!", response);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
