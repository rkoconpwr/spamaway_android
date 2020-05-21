package com.koconr.smspam.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.koconr.smspam.R;
import com.koconr.smspam.database.AppExecutors;
import com.koconr.smspam.database.DataBaseCache;
import com.koconr.smspam.model.Message;
import com.koconr.smspam.model.MessagesAdapter;
import com.koconr.smspam.params.Params;
import com.koconr.smspam.services.RequestQueueSingleton;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmsListActivity extends ListActivity implements SwipeActionAdapter.SwipeActionListener {
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private DataBaseCache dataBaseCache;
    private ArrayList<Message> spamList;
    private Context context;
    protected SwipeActionAdapter mAdapter;
    private boolean hasPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        this.checkPermissionValidity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        AppExecutors.getInstance().databaseThread().execute(
                () -> {
                    dataBaseCache = DataBaseCache.getDataBaseCache(context);
                    runOnUiThread(this::initListView);
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.hasPaused) {
            AppExecutors.getInstance().databaseThread().execute(
                    () -> {
                        dataBaseCache = DataBaseCache.getDataBaseCache(context);
                        ArrayList<Message> newSpamList = new ArrayList<>(dataBaseCache.getAllMessages());
                        spamList.clear();
                        spamList.addAll(newSpamList);
                        runOnUiThread(() -> mAdapter.notifyDataSetChanged());
                    }
            );
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.hasPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.hasPaused = false;
    }

    private void initListView() {

        // Create an Adapter for your content
        spamList = new ArrayList<>(dataBaseCache.getAllMessages());
        MessagesAdapter messagesAdapter = new MessagesAdapter(
                this,
                spamList
        );

        // Wrap your content in a SwipeActionAdapter
        mAdapter = new SwipeActionAdapter(messagesAdapter);

        // Pass a reference of your ListView to the SwipeActionAdapter
        mAdapter.setSwipeActionListener(this)
                .setListView(getListView());

        // Set the SwipeActionAdapter as the Adapter for your ListView
        setListAdapter(mAdapter);

        // Set backgrounds for the swipe directions
        mAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
                .addBackground(SwipeDirection.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT,R.layout.row_bg_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermissionValidity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            new AlertDialog.Builder(context)
                    .setTitle(R.string.permission_request)
                    .setMessage(R.string.permission_description)
                    .setPositiveButton(R.string.permission_yes, (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.RECEIVE_SMS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    })
                    .setNegativeButton(R.string.permission_no, (dialog, which) -> {
                        this.finishAffinity();
                    })
                    .setIcon(R.mipmap.ic_launcher_round)
                    .create().show();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(
                    this,
                    "Spam Away can't work without SMS permission",
                    Toast.LENGTH_SHORT
                ).show();
                this.finishAffinity();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        Toast.makeText(
                this,
                "Clicked "+mAdapter.getItem(position),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public boolean hasActions(int position, SwipeDirection direction){
        if(direction.equals(SwipeDirection.DIRECTION_NORMAL_LEFT)) return true;
        if(direction.equals(SwipeDirection.DIRECTION_NORMAL_RIGHT)) return true;
        return false;
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction){
        return direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
    }

    @Override
    public void onSwipe(int[] positionList, SwipeDirection[] directionList){
        for(int i=0;i<positionList.length;i++) {
            SwipeDirection direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    this.swipeLeftNotSpam(position);
                    break;
                case DIRECTION_NORMAL_RIGHT:
                    dir = "Right";
                    this.swipeRightIsSpam(position);
                    break;
            }
            Toast.makeText(
                    this,
                    dir + " swipe Action triggered on " + position,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void swipeLeftNotSpam(int position) {
        this.deleteFromDatabase(position, false);
    }

    private void swipeRightIsSpam(int position) {
        this.deleteFromDatabase(position, true);
    }

    private void deleteFromDatabase(int position, boolean isSpam) {
        AppExecutors.getInstance().databaseThread().execute(
                () -> {
                    ArrayList<Message> messages = new ArrayList<>(dataBaseCache.getAllMessages());
                    Message message = messages.get(position);
                    this.postSMSToServer(message, isSpam);
                    dataBaseCache.deleteMessage(message);
                }
        );
        spamList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private void postSMSToServer(Message message, boolean isSpam) {
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
