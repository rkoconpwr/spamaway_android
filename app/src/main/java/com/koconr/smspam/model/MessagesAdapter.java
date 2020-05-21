package com.koconr.smspam.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.koconr.smspam.R;

import java.util.ArrayList;


public class MessagesAdapter extends ArrayAdapter<Message> {

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_sms_list, parent, false);

        }
        Message message = getItem(position);

        if (message != null) {
            TextView smsPrimaryView = convertView.findViewById(R.id.smsPrimaryText);
            TextView smsSecondaryView = convertView.findViewById(R.id.smsSecondaryText);
            smsPrimaryView.setText(message.sender);
            smsSecondaryView.setText(message.content);
        }

        return convertView;
    }
}
