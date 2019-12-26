package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.orderproupdated.Fragments.ChatFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.bean.RecentChat;
import com.customer.orderproupdated.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Administrator on 7/7/2016.
 */
public class RecentAdapter extends BaseAdapter {

    String[] result;
    private ArrayList<RecentChat> arrayLst;// = new ArrayList<>();
    private static LayoutInflater inflater = null;
    ChatFragment chatFragment;
    String Type;
    ArrayList<RecentChat> newlist = new ArrayList<>();
    DatabaseHandler db;

    public RecentAdapter(ChatFragment chatFragment, ArrayList<RecentChat> recentLst, String Type) {
        arrayLst = recentLst;
        this.newlist.addAll(recentLst);
        db = new DatabaseHandler(chatFragment.getActivity());
        this.chatFragment = chatFragment;
        inflater = (LayoutInflater) chatFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Type = Type;
    }

    @Override
    public int getCount() {

        return arrayLst.size();
    }

    @Override
    public Object getItem(int position) {

        return arrayLst.get(position);

    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public class Holder {
        TextView contactName;
        TextView chatCount;

        ImageView iv_profile;
        TextView contactNameAll;
        TextView lastStatus;
        TextView time;
        TextView recent_msg;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView = null;

        rowView = inflater.inflate(R.layout.list_item_recent, null);
        holder.contactName = (TextView) rowView.findViewById(R.id.tv_contactname);
        holder.time = (TextView) rowView.findViewById(R.id.time);
        holder.recent_msg = (TextView) rowView.findViewById(R.id.lastStatus);
        String sss = (arrayLst.get(position).getName());
        holder.contactName.setText(Utility.capitalize(sss.split("@")[0]));
        holder.time.setText(arrayLst.get(position).getTime());
        holder.recent_msg.setText(arrayLst.get(position).getLastMessage());




        holder.chatCount = (TextView) rowView.findViewById(R.id.chatCount);
        if (getCount(arrayLst.get(position).getChatCount()).equals("VISIBLE")) {
            holder.chatCount.setText(arrayLst.get(position).getChatCount());
        } else {
            holder.chatCount.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }



    public String getCount(String count) {
        try {
            int validCount = Integer.parseInt(count);
            if (validCount > 0)
                return "VISIBLE";
            else
                return "INVISIBLE";
        } catch (Exception exception) {
            return "INVISIBLE";
        }
    }
    public void getFilter(String str) {
        arrayLst.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            arrayLst.addAll(newlist);

        } else {
            for (RecentChat recentChatData : newlist) {
                if (recentChatData.getName().toLowerCase().contains(txt) ) {
                    arrayLst.add(recentChatData);

                }
            }
        }
        notifyDataSetChanged();
    }

}
