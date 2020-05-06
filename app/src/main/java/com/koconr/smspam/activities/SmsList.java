package com.koconr.smspam.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.koconr.smspam.R;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;
import java.util.Arrays;

public class SmsList extends ListActivity implements SwipeActionAdapter.SwipeActionListener {
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    protected SwipeActionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkPermissionValidity();

        this.initListView();
    }

    private void initListView() {

        // Create an Adapter for your content
        String[] content = new String[20];
        for (int i=0;i<20;i++) content[i] = "Row "+(i+1);
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(
                this,
                R.layout.content_sms_list,
                R.id.text,
                new ArrayList<String>(Arrays.asList(content))
        );

        // Wrap your content in a SwipeActionAdapter
        mAdapter = new SwipeActionAdapter(stringAdapter);

        Log.e("SCREW YOU", "SCREW YOU, ANDROID");
        // Pass a reference of your ListView to the SwipeActionAdapter
        mAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(getListView());

        // Set the SwipeActionAdapter as the Adapter for your ListView
        setListAdapter(mAdapter);
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
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    this.MY_PERMISSIONS_REQUEST_READ_CONTACTS);

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
        if(direction.isLeft()) return true;
        if(direction.isRight()) return true;
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
                case DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    break;
                case DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    break;
                case DIRECTION_NORMAL_RIGHT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
                    dir = "Right";
                    break;
            }
            Toast.makeText(
                    this,
                    dir + " swipe Action triggered on " + mAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            mAdapter.notifyDataSetChanged();
        }
    }
}
