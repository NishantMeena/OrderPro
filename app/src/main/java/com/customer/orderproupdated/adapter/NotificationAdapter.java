package com.customer.orderproupdated.adapter;
/**
 * Created by AKASH on 29-Aug-16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.bean.NotificationData;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    ArrayList<NotificationData> notificationlist;
    Context context;
    public NotificationAdapter(Context context, ArrayList<NotificationData> notificationlist) {

        this.notificationlist = notificationlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notificationlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater lif= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= lif.inflate(R.layout.list_item_notification, null);
        TextView notification_title,notification_time,notification_content;
        notification_time =(TextView)v.findViewById(R.id.notification_time);
        notification_title =(TextView)v.findViewById(R.id.notification_title);
        notification_content =(TextView)v.findViewById(R.id.notification_content);

        notification_time.setText(notificationlist.get(position).getItem_time());
        notification_title.setText(notificationlist.get(position).getItem_title());
        notification_content.setText(notificationlist.get(position).getItem_content());
        return v;
    }


}