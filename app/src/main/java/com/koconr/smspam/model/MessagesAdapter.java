package com.koconr.smspam.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.koconr.smspam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MessagesAdapter extends ArrayAdapter<Message> {

    private Context context;
    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        this.context = context;
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
            TextView smsDateView = convertView.findViewById(R.id.smsPrimaryDate);
            Locale locale =  context.getResources().getConfiguration().getLocales().get(0);
            Date date = new Date(message.date);

            smsPrimaryView.setText(message.getSenderName());
            smsSecondaryView.setText(message.content);
            smsDateView.setText(new SimpleDateFormat("dd MMM, HH:mm", locale).format(date));
        }

        return convertView;
    }
}
