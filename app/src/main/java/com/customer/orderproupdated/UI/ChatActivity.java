package com.customer.orderproupdated.UI;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.database.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by Sony on 12/29/2016.
 */
public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout sendchat_layout,delete;
    ImageView img_delete,img_security,img_requestkey;
    TextView tv_mobile_number;
    ListView message_list;
    EditText message_ET;
    LinearLayout right_layout;
    ImageView sendmessagebutton;
    String mContactnumber, mContactName, ownPhonenumber, mNumber;
   // ChatListAdapter chatListAdapter;
    ArrayList<ChatBody> populateChat = new ArrayList<ChatBody>();
    SharedPrefrence share;
    DatabaseHandler db;
    private BroadcastReceiver receiveSMS;
    String merchantNAme="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_interface);
    }
        /*share=SharedPrefrence.getInstance(this);
        //Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
           DialogUtility .showLOG("GetSupportActionBar returned null.");
        }
        merchantNAme=share.getMerchantDetail(SharedPrefrence.MERCHANT_DETAIL).getUsername();

        if(!merchantNAme.equalsIgnoreCase(""))
        {
            actionBar.setTitle(merchantNAme);
        }else
        {
            actionBar.setTitle("Chat");

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share = SharedPrefrence.getInstance(this);
        db = new DatabaseHandler(this);

        sendchat_layout = (LinearLayout) findViewById(R.id.sendchat_layout);

        // getting_id
        message_list = (ListView) findViewById(R.id.message_list);
        message_ET = (EditText) findViewById(R.id.message_ET);
        sendmessagebutton = (ImageView) findViewById(R.id.sendmessagebutton);
        right_layout = (LinearLayout) findViewById(R.id.right_layout);
*//*
        mContactnumber = getIntent().getExtras().getString("mobile");
        mContactName = getIntent().getExtras().getString("user_name");
*//*

        mContactnumber ="JK Enterprise";
        mContactName = "Nupur";
        mNumber = mContactnumber;

        cancelNotification();
        updateUnreadCount();

      //  populateChat = db.getSMS(ownPhonenumber, mContactnumber);



        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String msg = message_ET.getText().toString().trim();

                if (msg.length() > 0) {

                    completeEncryptedMsg(msg, mContactnumber);
                } else {

                    Toast.makeText(ChatActivity.this, "Please enter message.", Toast.LENGTH_SHORT).show();

                }
                }

        });



        message_ET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        showListContent();

        receiveSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                cancelNotification();
                updateUnreadCount();
                showListContent();

            }
        };



        try {

            IntentFilter intentFiltersms = new IntentFilter("com.orderpro.chat");
            registerReceiver(receiveSMS, intentFiltersms);

        }catch (Exception e)
        {

        }


    }

    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(receiveSMS);
        }catch (Exception e)
        {}
    }

    public void showListContent() {
       // populateChat = db.getSMS(ownPhonenumber, mContactnumber);
        updateChatList();
    }

    public void updateChatList() {

        chatListAdapter = new ChatListAdapter(populateChat);
        message_list.setAdapter(chatListAdapter);
    }

    public void updateUnreadCount() {
        //db.updateSMSUnreadCount(ownPhonenumber, mContactnumber);
    }

    public class ChatListAdapter extends BaseAdapter {
        ArrayList<ChatBody> populateChat;

        public ChatListAdapter(ArrayList<ChatBody> populateChat) {
            this.populateChat = populateChat;
        }

        public int getCount() {
            return populateChat.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ChatBody populate = populateChat.get(position);
            LayoutInflater inflater = getLayoutInflater();

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {
                convertView = inflater.inflate(R.layout.outgoing_view, null);

            } else if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {
                convertView = inflater.inflate(R.layout.incoming_view, null);

            }else if (populate.getInOutBound().equals(PreferenceConstant.DATE_SECTION))
            {
                convertView = inflater.inflate(R.layout.section_header, null);

                TextView  sectionHeader = (TextView) convertView.findViewById(R.id.sectionHeader);
                sectionHeader.setText(populate.getMessage());
            }


            TextView message_body = (TextView) convertView.findViewById(R.id.message_body);
            message_body.setText(populate.getMessage());

            TextView time = (TextView) convertView.findViewById(R.id.time);
            ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_clock);

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND))
            {
                iv_clock.setVisibility(View.VISIBLE);
                if(populate.getStatus().equals(PreferenceConstant.DELIVERED))
                {
                    iv_clock.setBackgroundResource(R.drawable.tick_single);
                }else
                {
                    iv_clock.setBackgroundResource(R.drawable.evap_clock);

                }

            }

            time.setText(Utility.getFormattedTime(Long.parseLong(populate.getTimeStamp())));

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND) ||populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {

                RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.chatBubble);

                relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        final CharSequence[] items = {"Copy", "Delete"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    CopyText(populate.getMessage());
                                } else if (item == 1) {
                                    populateChat.remove(position);
                                    deleteInDB(populate.getMessageID(), populate.getFriendJID());
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }
                });
            }
            return convertView;
        }
    }

    private void completeEncryptedMsg(String message, String phone) {

        addMsgToDb(message);

    }

    private void addMsgToDb(String msg) {
        ChatBody chatbody = new ChatBody();
        chatbody.setFriendJID(mContactnumber);
        chatbody.setOwnerJID(ownPhonenumber);
        chatbody.setMessageID(String.valueOf(System.currentTimeMillis()));
        chatbody.setMessage(msg);
        chatbody.setInOutBound(PreferenceConstant.OUT_BOUND);
        chatbody.setDate(Utility.getCurentDateIFormate());
        chatbody.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        chatbody.setStatus(String.valueOf(System.currentTimeMillis()));
        //db.addSMSMessageToDB(chatbody);

        populateChat.add(chatbody);
        showListContent();
        message_ET.setText("");

    }



    public void cancelNotification() {

        try {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
            nMgr.cancel(15231);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void CopyText(String copiedchat)
    {

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            @SuppressWarnings("deprecation")
            android.text.ClipboardManager clipboardMgr = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if(copiedchat.equals(""))
            {
                clipboardMgr.setText(copiedchat);
            }
            else
            {
                clipboardMgr.setText(copiedchat);
            }
        }
        else {
            // this api requires SDK version 11 and above, so suppress warning for now
            android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if(copiedchat.equals(""))
            {

                ClipData clip = ClipData.newPlainText("Copied text", copiedchat);
                clipboardMgr.setPrimaryClip(clip);
            }
            else
            {

                ClipData clip = ClipData.newPlainText("Copied text", copiedchat);
                clipboardMgr.setPrimaryClip(clip);
            }

        }
    }

    private void deleteInDB(String id,String userid)
    {
      //  db.deleteSMS(id, userid);
        // showListContent();
        chatListAdapter.notifyDataSetChanged();
    }*/
}
