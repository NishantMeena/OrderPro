package com.customer.orderproupdated.database;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.bean.RecentChat;
import com.customer.orderproupdated.chat.bean.XEP;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.chat.rooster.XmppConnect;
import com.customer.orderproupdated.chat.utility.DateFormatter;

import org.jivesoftware.smack.packet.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "orderpro.db";

    // chat table chat
    private static final String TABLE_CHAT = "chat";

    // Contacts table name
    private static final String TABLE_OPEN_CONVERSATION = "allOpenConversation";


    // filesInfo table file
    private static final String TABLE_FILES = "filesInfo";


    //  XEP-0184 Implementation
    private static final String TABLE_XEP = "XEP0184";


    //product table
    private static final String TABLE_PRODUCT = "product";

    private static final String JID = "JID";
    private static final String IMAGE = "Image";
    private static final String NAME = "Name";
    private static final String NUMBER = "Number";
    private static final String TYPE = "Type";
    private static final String OWNER = "Owner";
    private static final String KEY = "userkey";

    private static final String KEYA = "userkeyA";
    private static final String KEYB = "userkeyB";
    private static final String PRIORITY = "Priority";


    // Contacts Table Columns names
    //private static final String KEY_ID = "id";
    private static final String OWNER_JID = "MyJID";
    private static final String FRIEND_JID = "FriendJID";
    private static final String MESSAGE_ID = "MessageID";
    private static final String MESSAGE = "Message";
    private static final String MESSAGE_TYPE = "MessageType";
    private static final String IN_OUT_BOUND = "InOutBound";
    private static final String DATE = "DateParam";
    private static final String TIME_STAMP = "TimeStamp";
    private static final String PROGRESS = "Progress";
    private static final String STATUS = "Status";
    private static final String CATEGORY = "Category";


    private static final String GROUP_JID = "GroupJID";
    private static final String MESSAGE_FROM = "Msg_From";

    //User Presence Table
    private static final String PRESENCE_MODE = "PresenceMode";

    // File Table
    private static final String FILE_URL = "Url";
    private static final String LOCAL_URI = "LocalUri";
    private static final String THUMB = "Thumb";
    private static final String FILE_EXTENSION = "Extension";

    private static final String EVAP_DURATION = "EvapDuration";
    private static final String EVAP_TIME = "EvapTime";

    //product table coloumn names
    private static final String PRODUCT_ID = "P_id";
    private static final String PRODUCT_NAME = "P_name";
    private static final String PRODUCT_PRICE = "P_price";
    private static final String PRODUCT_IMAGES = "P_images";
    private static final String PRODUCT_DESCRIPTION = "P_decription";
    private static final String PRODUCT_SPECIFICATION = "P_specification";
    private static final String PRODUCT_IS_ADDED_TO_CART = "P_addedToCart";
    private static final String PRODUCT_IS_FAVOURITE = "P_addedToFavourite";




    SharedPrefrence share;
    Context ctx;

    public static HashMap<String, String> recipient = new HashMap<String, String>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //  super(context, makeDBFilePath(), null, DATABASE_VERSION);
        ctx = context;
        share = SharedPrefrence.getInstance(ctx);
    }

    public static String makeDBFilePath() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), PreferenceConstant.rootDirectory + File.separator + PreferenceConstant.subRootDBDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }

        String dbPath = (file.getPath() + File.separator + DATABASE_NAME);

        return dbPath;
    }

    // Creating Tables StrictMode.
    @Override
    public void onCreate(SQLiteDatabase db) {



        String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHAT + "("
                + OWNER_JID + " TEXT,"
                + FRIEND_JID + " TEXT,"
                + MESSAGE_ID + " TEXT,"
                + MESSAGE + " TEXT,"
                + MESSAGE_TYPE + " TEXT,"
                + IN_OUT_BOUND + " TEXT,"
                + PROGRESS + " TEXT,"
                + STATUS + " TEXT,"
                + CATEGORY + " TEXT,"
                + TIME_STAMP + " TEXT,"
                + DATE + " DATETIME,"
                + EVAP_DURATION + " TEXT,"
                + EVAP_TIME + " DATETIME,"
                + PRIORITY + " TEXT" + ")";
        db.execSQL(CREATE_CHAT_TABLE);


        String CREATE_OPEN_CONVERSATION = "CREATE TABLE " + TABLE_OPEN_CONVERSATION + "("
                + OWNER_JID + " TEXT,"
                + FRIEND_JID + " TEXT,"
                + NAME + " TEXT,"
                + MESSAGE + " TEXT,"
                + CATEGORY + " TEXT,"
                + DATE + " DATETIME,"
                + TIME_STAMP + " TEXT" + ")";
        db.execSQL(CREATE_OPEN_CONVERSATION);

        /*Create File Table*/
        String CREATE_FILES_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + MESSAGE_ID + " TEXT,"
                + MESSAGE_TYPE + " TEXT,"
                + PROGRESS + " TEXT,"
                + FILE_URL + " TEXT,"
                + LOCAL_URI + " TEXT,"
                + FILE_EXTENSION + " TEXT,"
                + THUMB + " TEXT,"
                + STATUS + " TEXT,"
                + IN_OUT_BOUND + " TEXT" + ")";
        db.execSQL(CREATE_FILES_TABLE);


        //  XEP-0184 Implementation
        String CREATE_TABLE_XEP = "CREATE TABLE " + TABLE_XEP + "("
                + MESSAGE_ID + " TEXT,"
                + FRIEND_JID + " TEXT, "
                + STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_XEP);

/*
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY "
                + PRODUCT_NAME + " TEXT,"
                + PRODUCT_PRICE + " TEXT,"
                + PRODUCT_DESCRIPTION + " TEXT,"
                + PRODUCT_SPECIFICATION + " TEXT,"
                + PRODUCT_IS_ADDED_TO_CART+ " TEXT,"
                + PRODUCT_IS_FAVOURITE + " TEXT," + ")";
        db.execSQL(CREATE_PRODUCT_TABLE);
        */


    }//public void onCreate(SQLiteDatabase db)

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPEN_CONVERSATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
//  XEP-0184 Implementation
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_XEP);


        onCreate(db);
    }//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion



    public String getUnreadCount(String MyJID, String ReceiverJid) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        int count = 0;

        /* get unread count from chat */
        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_CHAT + " WHERE " + OWNER_JID + " ='" + MyJID + "' AND " + FRIEND_JID + "='" + ReceiverJid + "' AND " + STATUS + "='unread'";
        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();


        db.close();
        return count + "";

    }

    public int getTotalUnread() {

        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_CHAT + " WHERE " + STATUS + " ='unread'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;

        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return count;
    }

    public String getOpenConversation_ofUnread() {

        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;

        try {
            String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_CHAT + " WHERE " + STATUS + " ='unread' GROUP BY " + OWNER_JID + " ," + FRIEND_JID;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        count = cursor.getCount();
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();





        } catch (Exception e) {
            e.printStackTrace();
        }


        db.close();
        return String.valueOf(count);
    }

    public ArrayList<ChatBody> getOpenConversationsForNotification() {

        //select * from chat where Status='unread' group by MyJID,FriendJID
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + STATUS + " ='unread' GROUP BY " + OWNER_JID + " ," + FRIEND_JID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<ChatBody> chatBodies = new ArrayList<ChatBody>();

        // looping through all rows and adding to list
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    do {
                        try {
                            ChatBody chat = new ChatBody();
                            chat.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));
                            chat.setFriendJID(cursor.getString(cursor.getColumnIndex(FRIEND_JID)));
                            chat.setMessageID(cursor.getString(cursor.getColumnIndex(MESSAGE_ID)));
                            String name;
                            //String name = Utility.getContactNameByNumber(ctx, chat.getFriendJID());

                            chat.setMessageType(cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE)));
                            chat.setInOutBound(cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND)));
                            chat.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                            chat.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));
                            chat.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));
                            chat.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                            chat.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));

                            chatBodies.add(chat);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } while (cursor.moveToNext());
                }
            } else {
            }
        }
        cursor.close();



        db.close();

        return chatBodies;
    }


    public void updateUnreadCount(String MyJID, String ReceiverJID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String update;
        update = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + PreferenceConstant.READ + "' WHERE " + IN_OUT_BOUND + "='" + PreferenceConstant.IN_BOUND + "' AND  " + OWNER_JID + "='" + MyJID + "' AND " + FRIEND_JID + "='" + ReceiverJID + "'";
        db.execSQL(update);
        db.close();
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addChatMessageToDB(ChatBody chat) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(OWNER_JID, chat.getOwnerJID());
            values.put(FRIEND_JID, chat.getFriendJID());
            values.put(MESSAGE_ID, chat.getMessageID());
            values.put(MESSAGE, chat.getMessage());
            values.put(MESSAGE_TYPE, chat.getMessageType());
            values.put(IN_OUT_BOUND, chat.getInOutBound());
            values.put(DATE, chat.getDate());
            values.put(TIME_STAMP, chat.getTimeStamp());
            values.put(PROGRESS, chat.getProgress());
            values.put(STATUS, chat.getStatus());
            values.put(CATEGORY, chat.getCategory());


            values.put(EVAP_DURATION, chat.getEvapDuration());
            values.put(EVAP_TIME, chat.getEvapTime());

            values.put(PRIORITY, chat.getPriority());

            addIntoOpenCon(chat, db);
            db.insert(TABLE_CHAT, null, values);

            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //========================================================================
    public void addIntoOpenCon(ChatBody chat, SQLiteDatabase db) {


        if (checkOpenConversation(chat.getFriendJID(), db)) {
            updateOpenCon(chat, db);
        } else {
            addEntryInOpenConversation(chat, db);
        }
    }

    public void updateOpenConversationUpdateName(String jid, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "UPDATE " + TABLE_OPEN_CONVERSATION + " SET " + NAME + "='" + name + "' WHERE " + FRIEND_JID + "='" + jid + "'";
        db.execSQL(delete);
        db.close();

    }


    public String getgroupnamebyid(String jid) {

        String name = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery;
        selectQuery = "SELECT  * FROM " + TABLE_OPEN_CONVERSATION + "  WHERE " + FRIEND_JID + "='" + jid + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex(NAME));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        db.close();

        return name;

    }


    public boolean checkOpenConversation(String friendJID, SQLiteDatabase db) {

        String selectQuery = "SELECT  * FROM " + TABLE_OPEN_CONVERSATION + " WHERE " + FRIEND_JID + " ='" + friendJID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean check = false;

        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    check = true;
                    break;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return check;
    }

    public void addEntryInOpenConversation(ChatBody chat, SQLiteDatabase db) {
        try {

            String category ="" ;
            ContentValues values = new ContentValues();

            String name = chat.getContactName();
            String friendJID = chat.getFriendJID();
            String message = chat.getMessage();

            if (name == null) {
                name = chat.getFriendJID();
            }

            if (chat.getCategory() == PreferenceConstant.ENCRYPTED) {
                message = "Encrypted Message";
                if(friendJID.contains("@broadcast"))
                {
                    category = PreferenceConstant.MESSAGE_TYPE_GROUP_CHAT;
                }else
                {
                    category = PreferenceConstant.MESSAGE_TYPE_CHAT;
                }
            }else
            {
                category = chat.getCategory();
            }

            values.put(NAME, name);
            values.put(OWNER_JID, chat.getOwnerJID());
            values.put(FRIEND_JID, friendJID);
            values.put(MESSAGE, message);
            values.put(CATEGORY, category);
            values.put(TIME_STAMP, chat.getTimeStamp());
            values.put(DATE, chat.getDate());
            db.insert(TABLE_OPEN_CONVERSATION, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void updateOpenCon(ChatBody chat, SQLiteDatabase db) {

        try {
            ContentValues values = new ContentValues();
            String type = chat.getMessageType();
            String message = chat.getMessage();

            if (chat.getCategory() == PreferenceConstant.ENCRYPTED) {
                message = "Encrypted Message";
            } else if ( type.equals(PreferenceConstant.IMAGE)){
                message = "Image";

            } else if ( type.equals(PreferenceConstant.VIDEO )) {
                message = "Video";

            } else if (type.equals(PreferenceConstant.AUDIO )) {
                message = "Audio";
            }
            else if (type.equals(PreferenceConstant.STICKER )) {
                message = "Sticker";
            }
            else if (type.equals(PreferenceConstant.LOCATION)) {
                message = "Location";
            }
            else if (type.equals(PreferenceConstant.DOCUMENT)) {
                message = "Doc";
            }

            values.put(OWNER_JID, chat.getOwnerJID());
            values.put(FRIEND_JID, chat.getFriendJID());
            values.put(MESSAGE, message);
            values.put(DATE, chat.getDate());
            values.put(TIME_STAMP, chat.getTimeStamp());
            db.update(TABLE_OPEN_CONVERSATION, values, "FriendJID='" + chat.getFriendJID() + "'", null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    // Getting chat
    public ArrayList<ChatBody> getChat(String me, String you, int limiter) {
        ArrayList<ChatBody> chatBodyLst = new ArrayList<ChatBody>();
        String currentDate = "";


        String selectQuery = "SELECT  * FROM " + TABLE_CHAT +
                " WHERE " + OWNER_JID + "='" + me +
                "' AND  " + FRIEND_JID + "='" + you + "'" +
                " ORDER BY " + TIME_STAMP +
                " DESC LIMIT " + limiter;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {

            int cc = cursor.getCount();
            cc--;

            for (int i = cc; i >= 0; i--) {
                cursor.moveToPosition(i);

                ChatBody chatBody = new ChatBody();

                chatBody.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));

                String friendJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                chatBody.setFriendJID(friendJid);

                String messageID = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                chatBody.setMessageID(messageID);

                String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
                chatBody.setMessage(message);

                String messageType = cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE));
                chatBody.setMessageType(messageType);

                String inOutBound = cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND));
                chatBody.setInOutBound(inOutBound);

                chatBody.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));

                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                chatBody.setStatus(status);

                chatBody.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                chatBody.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));

                String date = cursor.getString(cursor.getColumnIndex(DATE));
                chatBody.setDate(date);


                if (!currentDate.equals(date)) {
                    ChatBody timeSectionBody = new ChatBody();
                    timeSectionBody.setMessageType(PreferenceConstant.DATE_SECTION);
                    timeSectionBody.setMessageID(date);
                    timeSectionBody.setMessage(date);
                    String time = DateFormatter.getMessageTime(ctx, Long.valueOf(cursor.getString(cursor.getColumnIndex(TIME_STAMP))), date);
                    timeSectionBody.setMessage(time);
                    chatBodyLst.add(timeSectionBody);
                    currentDate = date;
                }


                chatBody.setPriority(cursor.getInt(cursor.getColumnIndex(PRIORITY)));

                long evap_duration = cursor.getLong(cursor.getColumnIndex(EVAP_DURATION));
                chatBody.setEvapDuration(evap_duration);

                long evap_time = cursor.getLong(cursor.getColumnIndex(EVAP_TIME));
                chatBody.setEvapTime(evap_time);

                if (evap_time == 0) {

                    chatBodyLst.add(chatBody);

                } else {

                    if ((messageType.equals(PreferenceConstant.AUDIO) ||
                            messageType.equals(PreferenceConstant.IMAGE) ||
                            messageType.equals(PreferenceConstant.VIDEO) ||
                            messageType.equals(PreferenceConstant.DOCUMENT)) && (inOutBound.equals(PreferenceConstant.IN_BOUND))) {

                        status = getFileInfo(messageID).getStatus();
                    } else if ((status.equalsIgnoreCase(PreferenceConstant.DELIVERED) || status.equalsIgnoreCase(PreferenceConstant.READ)) && (inOutBound.equals(PreferenceConstant.OUT_BOUND))) {
                        status = PreferenceConstant.SENT;
                    }

                    if ((inOutBound.equals(PreferenceConstant.OUT_BOUND) && status.equals(PreferenceConstant.SENT)) ||

                            ((messageType.equals(PreferenceConstant.TEXT) ||
                                    messageType.equals(PreferenceConstant.CONTACT) ||
                                    messageType.equals(PreferenceConstant.STICKER) ||
                                    messageType.equals(PreferenceConstant.LOCATION)) && (inOutBound.equals(PreferenceConstant.IN_BOUND) && status.equals(PreferenceConstant.READ))) ||

                            ((messageType.equals(PreferenceConstant.AUDIO) ||
                                    messageType.equals(PreferenceConstant.IMAGE) ||
                                    messageType.equals(PreferenceConstant.VIDEO)||
                                    messageType.equals(PreferenceConstant.DOCUMENT)) && (inOutBound.equals(PreferenceConstant.IN_BOUND) && status.equals(PreferenceConstant.fileDownloaded)))) {



                    } else {

                        chatBodyLst.add(chatBody);
                    }
                }

            }

//                } while (cursor.moveToNext());
//            }
        }
        cursor.close();
        db.close();
        return chatBodyLst;
    }


    public ArrayList<ChatBody> loadMoreUserChat(String me, String you, int limiter, int alreadyLoaded, ArrayList<ChatBody> populateLst) {

        ArrayList<ChatBody> chatBodyLst = new ArrayList<ChatBody>();

        HashMap<String, Integer> dateSections = new HashMap<String, Integer>();
        for (int i = 0; i < populateLst.size(); i++) {
            ChatBody chatBody = populateLst.get(i);
            if (chatBody.getMessageType().equals(PreferenceConstant.DATE_SECTION)) {
                dateSections.put(chatBody.getMessageID(), i);
            }
        }

        String currentDate = "";

        String selectQuery = "SELECT * FROM " + TABLE_CHAT +
                " WHERE " + OWNER_JID + "='" + me +
                "' AND  " + FRIEND_JID + "='" + you + "'" +
                " ORDER BY " + TIME_STAMP +
                " DESC LIMIT " + limiter;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {

            int cc = cursor.getCount();
            cc--;

            for (int i = cc; i >= (alreadyLoaded - 1); i--) {
                cursor.moveToPosition(i);

                ChatBody chatBody = new ChatBody();

                chatBody.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));

                String friendJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                chatBody.setFriendJID(friendJid);

                String messageID = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                chatBody.setMessageID(messageID);

                String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
                chatBody.setMessage(message);

                String messageType = cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE));
                chatBody.setMessageType(messageType);

                String inOutBound = cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND));
                chatBody.setInOutBound(inOutBound);

                chatBody.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));

                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                chatBody.setStatus(status);

                chatBody.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                chatBody.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));

                String date = cursor.getString(cursor.getColumnIndex(DATE));
                chatBody.setDate(date);

                if (!currentDate.equals(date)) {

                    if (dateSections.size() > 0) {
                        Iterator iter = dateSections.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) iter.next();
                            String key = (String) mapEntry.getKey();
                            int index = (int) mapEntry.getValue();

                            if (date.equals(key)) {
                                iter.remove();
                                populateLst.remove(index);
                                dateSections.remove(key);
                            }
                        }
                    }

                    ChatBody timeSectionBody = new ChatBody();
                    timeSectionBody.setMessageType(PreferenceConstant.DATE_SECTION);
                    timeSectionBody.setMessageID(date);
                    timeSectionBody.setMessage(date);
                    String time = DateFormatter.getMessageTime(ctx, Long.valueOf(cursor.getString(cursor.getColumnIndex(TIME_STAMP))), date);
                    timeSectionBody.setMessage(time);

                    chatBodyLst.add(timeSectionBody);

                    currentDate = date;
                }

                chatBody.setPriority(cursor.getInt(cursor.getColumnIndex(PRIORITY)));

                long evap_duration = cursor.getLong(cursor.getColumnIndex(EVAP_DURATION));
                chatBody.setEvapDuration(evap_duration);

                long evap_time = cursor.getLong(cursor.getColumnIndex(EVAP_TIME));
                chatBody.setEvapTime(evap_time);

                if (evap_time == 0) {

                    chatBodyLst.add(chatBody);

                } else {

                    if ((messageType.equals(PreferenceConstant.AUDIO) ||
                            messageType.equals(PreferenceConstant.IMAGE) ||
                            messageType.equals(PreferenceConstant.VIDEO)||
                            messageType.equals(PreferenceConstant.DOCUMENT)) && (inOutBound.equals(PreferenceConstant.IN_BOUND))) {

                        status = getFileInfo(messageID).getStatus();
                    } else if ((status.equalsIgnoreCase(PreferenceConstant.DELIVERED) || status.equalsIgnoreCase(PreferenceConstant.READ)) && (inOutBound.equals(PreferenceConstant.OUT_BOUND))) {
                        status = PreferenceConstant.SENT;
                    }

                    if ((inOutBound.equals(PreferenceConstant.OUT_BOUND) && status.equals(PreferenceConstant.SENT)) ||

                            ((messageType.equals(PreferenceConstant.TEXT) ||
                                    messageType.equals(PreferenceConstant.CONTACT) ||
                                    messageType.equals(PreferenceConstant.STICKER) ||
                                    messageType.equals(PreferenceConstant.LOCATION)) && (inOutBound.equals(PreferenceConstant.IN_BOUND) && status.equals(PreferenceConstant.READ))) ||

                            ((messageType.equals(PreferenceConstant.AUDIO) ||
                                    messageType.equals(PreferenceConstant.IMAGE) ||
                                    messageType.equals(PreferenceConstant.VIDEO)||
                                    messageType.equals(PreferenceConstant.DOCUMENT)) && (inOutBound.equals(PreferenceConstant.IN_BOUND) && status.equals(PreferenceConstant.fileDownloaded)))) {


                        chatBodyLst.add(chatBody);


                    } else {

                        chatBodyLst.add(chatBody);
                    }
                }

            }

//                } while (cursor.moveToNext());
//            }
        }
        cursor.close();

        chatBodyLst.addAll(populateLst);

        db.close();
        return chatBodyLst;
    }




    // Deleting single contact
    public void deleteChatCloseConversation(String me, String you) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM chat WHERE userNameMe='" + me + "' AND userNameYou='" + you + "'";
        db.execSQL(delete);
        db.close();
    }


    public void updateOpenConversationmsg(ChatBody chat) {

        SQLiteDatabase db = this.getWritableDatabase();


        try {
            ContentValues values = new ContentValues();
            String type = chat.getMessageType();
            String message = chat.getMessage();

            if (type.equals(type.equals(PreferenceConstant.IMAGE))) {
                message = "Image";

            } else if (type.equals(PreferenceConstant.VIDEO)) {
                message = "Video";

            } else if ( type.equals(PreferenceConstant.AUDIO)) {
                message = "Audio";

            } else if (type.equals(PreferenceConstant.STICKER)) {
                message = "Sticker";
            }
            else if (type.equals(PreferenceConstant.LOCATION)) {
                message = "Location";
            }
            else if (type.equals(PreferenceConstant.DOCUMENT)) {
                message = "Doc";
            }

            values.put(MESSAGE, message);
            values.put(TIME_STAMP, chat.getTimeStamp());

            db.update(TABLE_OPEN_CONVERSATION, values, "FriendJID='" + chat.getFriendJID() + "'", null);
            db.close();
        } catch (Exception e) {
        }
    }

    public void updateOpenConversationmsgdelete(String friendid) {


        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_OPEN_CONVERSATION + " WHERE " + FRIEND_JID + "='" + friendid + "'";
        db.execSQL(delete);
        db.close();
    }


    public void deleteChatRow(String me, String you, String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_CHAT + " WHERE " + MESSAGE_ID + "='" + id + "'";
        db.execSQL(delete);
        db.close();
    }


    // Clear Chat by conversationID
    public void deleteAllChat(String ownjid, String id, String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        String delete = "";

        if (type.equals(PreferenceConstant.SINGLE_USER_CHAT)) {
            delete = "DELETE FROM " + TABLE_CHAT + " WHERE " + FRIEND_JID + "='" + id + "'";
        }

        db.execSQL(delete);


        try {
            ContentValues values = new ContentValues();
            values.put(OWNER_JID, ownjid);
            values.put(FRIEND_JID, id);
            values.put(MESSAGE, "No Conversation");
            values.put(TIME_STAMP, String.valueOf(System.currentTimeMillis()));
            db.update(TABLE_OPEN_CONVERSATION, values, "FriendJID='" + id + "'", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        db.close();
    }



    public int getchatCount(String MyJID, String ReceiverJid) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_CHAT + " WHERE " + OWNER_JID + " ='" + MyJID + "' AND " + FRIEND_JID + "='" + ReceiverJid + "'";
        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return count;
    }

    /*
    *
    * Files Operational Methods
    *
    * */

    public void addFilesToDB(ChatBody fileInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MESSAGE_ID, fileInfo.getMessageID());
        values.put(MESSAGE_TYPE, fileInfo.getMessageType());
        values.put(PROGRESS, fileInfo.getProgress());
        values.put(FILE_URL, fileInfo.getUrl());
        values.put(LOCAL_URI, fileInfo.getLocalUri());
        values.put(FILE_EXTENSION, fileInfo.getExtension());
        values.put(THUMB, fileInfo.getThumb());
        values.put(STATUS, fileInfo.getStatus());
        values.put(IN_OUT_BOUND, fileInfo.getInOutBound());

        db.insert(TABLE_FILES, null, values);
        db.close(); // Closing database connection
    }

    public ChatBody getFileInfo(String messageID) {
        String selectQuery;

        ChatBody filesInfo = new ChatBody();

        selectQuery = "SELECT  * FROM " + TABLE_FILES + "  WHERE " + MESSAGE_ID + "='" + messageID + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    filesInfo.setMessageID(cursor.getString(cursor.getColumnIndex(MESSAGE_ID)));
                    filesInfo.setMessageType(cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE)));
                    filesInfo.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));
                    filesInfo.setUrl(cursor.getString(cursor.getColumnIndex(FILE_URL)));
                    filesInfo.setLocalUri(cursor.getString(cursor.getColumnIndex(LOCAL_URI)));
                    filesInfo.setExtension(cursor.getString(cursor.getColumnIndex(FILE_EXTENSION)));
                    String ext = cursor.getString(cursor.getColumnIndex(FILE_EXTENSION));
                    System.out.println("<<--ext-"+cursor.getString(cursor.getColumnIndex(FILE_EXTENSION)));
                    filesInfo.setThumb(cursor.getString(cursor.getColumnIndex(THUMB)));
                    filesInfo.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                    filesInfo.setInOutBound(cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND)));

                    // Adding contact to list
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        db.close();

        return filesInfo;
    }


    public void updateFileInfo(ChatBody filesInfo) {
        try {

            /* Update same on Main Tables */
            updateChatStatusOnMain(filesInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFileInfoFileUploaded(ChatBody filesInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String progress = filesInfo.getProgress();
            String url = filesInfo.getUrl();
            String localUri = filesInfo.getLocalUri();
            String status = filesInfo.getStatus();
            String query = "";

            query = "UPDATE " + TABLE_FILES + " SET "
                    + PROGRESS + "='" + progress +
                    "'," + FILE_URL + "='" + url +
                    "'," + LOCAL_URI + "='" + localUri +
                    "'," + STATUS + "='" + status +
                    "'  WHERE " + MESSAGE_ID + "='" + filesInfo.getMessageID() + "'";
            db.execSQL(query);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* End Files Operational Methods*/



    /* Send Offline Messages */

    // Getting All Contacts
    public ArrayList<ChatBody> getOfflineChats() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ChatBody> chatBodyLst = new ArrayList<ChatBody>();
        String InOutBound = PreferenceConstant.OUT_BOUND;

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + IN_OUT_BOUND + "='" + InOutBound + "' AND  " + STATUS + "='" + PreferenceConstant.SENT_LATER + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ChatBody chatBody = new ChatBody();

                        chatBody.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));
                        String contactJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                        chatBody.setFriendJID(contactJid);
                        chatBody.setMessageID(cursor.getString(cursor.getColumnIndex(MESSAGE_ID)));
                        chatBody.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                        chatBody.setMessageType(cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE)));
                        chatBody.setInOutBound(cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND)));
                        chatBody.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));
                        chatBody.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                        chatBody.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                        chatBody.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));
                        chatBody.setDate(cursor.getString(cursor.getColumnIndex(DATE)));


                        // Adding contact to list
                        chatBodyLst.add(chatBody);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();




        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatBodyLst;
    }


    public void updateChatStatusOnMain(ChatBody filesInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String status = filesInfo.getStatus();
            String query = "";

            try {
                //          Single User Chat
                query = "UPDATE " + TABLE_FILES + " SET "
                        + STATUS + "='" + filesInfo.getStatus() +
                        "'  WHERE " + MESSAGE_ID + "='" + filesInfo.getMessageID() + "'";
                db.execSQL(query);
            } catch (Exception e) {
            }

            try {
                //          Single User Chat
                query = "UPDATE " + TABLE_CHAT + " SET "
                        + STATUS + "='" + filesInfo.getStatus() +
                        "'  WHERE " + MESSAGE_ID + "='" + filesInfo.getMessageID() + "'";
                db.execSQL(query);
            } catch (Exception e) {
            }

            db.close();

            changeChatFileTransferStatus(filesInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeChatFileTransferStatus(ChatBody filesInfo) {
        Intent intentOne = new Intent(RoosterConnectionService.UPDATE_FILE);
        intentOne.setPackage(ctx.getPackageName());
        intentOne.putExtra(RoosterConnectionService.MESSAGE_ID, filesInfo.getMessageID());
        intentOne.putExtra(RoosterConnectionService.FILE_STATUS, filesInfo.getStatus());
        ctx.sendBroadcast(intentOne);
    }

    public boolean isMessagePresentInDB(String messageID) {
        boolean isMessageExist = false;
        String selectQuery;
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            selectQuery = "SELECT  * FROM " + TABLE_CHAT + "  WHERE " + MESSAGE_ID + "='" + messageID + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    isMessageExist = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
        }
        db.close();
        return isMessageExist;
    }




    public void deleteEntryFromDB(String messageID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_CHAT + " WHERE " + MESSAGE_ID + "='" + messageID + "'";
        db.execSQL(delete);
        db.close();
    }

    public void deletePrivateEntryFromDB(String messageID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_CHAT + " WHERE " + MESSAGE_ID + "='" + messageID + "'";
        db.execSQL(delete);
        db.close();
    }

    private void deleteFileFromStorage(String file_path) {
        try {
            File file = new File(file_path);
            boolean deleted = file.delete();
        } catch (Exception e) {
        }
    }



    // Single User Chat
    public ChatBody getChatByMessageID(String me, String you, String messageId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ChatBody chatBody = new ChatBody();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT +
                " WHERE " + OWNER_JID + "='" + me +
                "' AND  " + FRIEND_JID + "='" + you +
                "' AND  " + MESSAGE_ID + "='" + messageId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    chatBody.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));

                    String friendJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                    chatBody.setFriendJID(friendJid);

                    String messageID = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                    chatBody.setMessageID(messageID);

                    String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
                    chatBody.setMessage(message);

                    String messageType = cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE));
                    chatBody.setMessageType(messageType);

                    String inOutBound = cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND));
                    chatBody.setInOutBound(inOutBound);

                    chatBody.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));

                    String status = cursor.getString(cursor.getColumnIndex(STATUS));
                    chatBody.setStatus(status);

                    chatBody.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                    chatBody.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));
                    chatBody.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

                    chatBody.setPriority(cursor.getInt(cursor.getColumnIndex(PRIORITY)));


                    long evap_duration = cursor.getLong(cursor.getColumnIndex(EVAP_DURATION));
                    chatBody.setEvapDuration(evap_duration);

                    long evap_time = cursor.getLong(cursor.getColumnIndex(EVAP_TIME));
                    chatBody.setEvapTime(evap_time);

                    if (evap_time == 0) {

                        return chatBody;

                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return chatBody;
    }


    public ChatBody appendImageContentByMessageID(Context ctx, ChatBody filesInfo) {

        boolean isInternetWorking = Utility.checkInternetConn(ctx);

        ChatBody temp = getFileInfo(filesInfo.getMessageID());

        filesInfo.setUrl(temp.getUrl());
        filesInfo.setThumb(temp.getThumb());
        filesInfo.setLocalUri(temp.getLocalUri());

        String status = temp.getStatus();
        if (status != null) {
            if (status.equals(PreferenceConstant.fileUploding) || status.equals(PreferenceConstant.fileDownloading)) {
                if (!isInternetWorking) {
                    uploadingFailed(temp);

                    status = PreferenceConstant.fileFail;
                }
            } else if (status.equals(PreferenceConstant.fileUploded) || status.equals(PreferenceConstant.fileDownloaded)) {
                status = filesInfo.getStatus();
            }
        } else {
            status = filesInfo.getStatus();
        }

        filesInfo.setStatus(status);

        return filesInfo;
    }

    public void uploadingFailed(ChatBody temp) {

        temp.setStatus(PreferenceConstant.fileFail);
        updateFileInfo(temp);
    }

    //  XEP-0184 Implementation
    public void addEntryForXEP(ChatBody chat) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MESSAGE_ID, chat.getMessageID());

            String friendJid = chat.getFriendJID().replace("/Shield", "");
            values.put(FRIEND_JID, friendJid);
            values.put(STATUS, PreferenceConstant.MSG_DELIVERED);

            db.insert(TABLE_XEP, null, values);
            db.close();

            try {
                /* Send IM Message Of XEP*/
                XEP xep = new XEP();
                xep.setMessageId(chat.getMessageID());
                xep.setStatus(PreferenceConstant.MSG_DELIVERED);
                xep.setFriendJID(chat.getFriendJID());
                xep.setSubject(PreferenceConstant.XEP_DELIVERED_RECEIPT);
                XmppConnect.getInstance().sendXEPReceipt(xep);
            } catch (Exception e) {
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteXEPRow(String MessageId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String delete = "DELETE FROM " + TABLE_XEP + "  WHERE " + MESSAGE_ID + "='" + MessageId + "' AND " + STATUS + "='" + PreferenceConstant.READ + "'";
            db.execSQL(delete);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getXepByFriendJid(String friendJID) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_XEP + " WHERE " + FRIEND_JID + "='" + friendJID +
                "' AND " + STATUS + "='" + PreferenceConstant.MSG_DELIVERED + "' OR " + STATUS + "='" + PreferenceConstant.MSG_DELIVERED_SUCCESS + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String messageId = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));

                    try {
                /* Send IM Message Of XEP*/
                        XEP xep = new XEP();
                        xep.setMessageId(messageId);
                        xep.setStatus(PreferenceConstant.READ);
                        xep.setFriendJID(friendJID);
                        xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                        XmppConnect.getInstance().sendXEPReceipt(xep);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
    }

    public void sendPendingXEP() {

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_XEP + " WHERE " + STATUS + "='" + PreferenceConstant.MSG_DELIVERED + "' OR " + STATUS + "='" + PreferenceConstant.READ + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String messageId = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                    String friendJID = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                    String status = cursor.getString(cursor.getColumnIndex(STATUS));

                    try {
                /* Send IM Message Of XEP*/
                        XEP xep = new XEP();
                        xep.setMessageId(messageId);
                        xep.setStatus(status);
                        xep.setFriendJID(friendJID);

                        if (status.equals(PreferenceConstant.MSG_DELIVERED)) {
                            xep.setSubject(PreferenceConstant.XEP_DELIVERED_RECEIPT);
                        } else if (status.equals(PreferenceConstant.READ)) {
                            xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                        }

                        XmppConnect.getInstance().sendXEPReceipt(xep);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
    }


    public void updateXEPStatusInMsgTables(XEP xep) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String updateSingleChat = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + xep.getStatus() +
                    "' WHERE " + MESSAGE_ID + "='" + xep.getMessageId() +
                    "' AND " + FRIEND_JID + "='" + xep.getFriendJID() + "'";
            db.execSQL(updateSingleChat);

            if (xep.getStatus().equals(PreferenceConstant.READ)) {

               /* String updateAllChat = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + xep.getStatus() +
                        "' WHERE " + FRIEND_JID + "='" + xep.getFriendJID() +
                        "' AND " + STATUS + "='" + PreferenceConstant.DELIVERED + "'";
               */
                //db.execSQL(updateAllChat);

                String updateAllChat = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + xep.getStatus() +
                        "' WHERE " + FRIEND_JID + "='" + xep.getFriendJID() +
                        "' AND " + STATUS + "='" + PreferenceConstant.DELIVERED + "' OR " + STATUS + "='" + PreferenceConstant.SENT + "'";
                db.execSQL(updateAllChat);

            }

            db.close();

            notifyChatWindows(xep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyChatWindows(XEP xep) {
        Intent intentOne = new Intent(RoosterConnectionService.UPDATE_XEP);
        intentOne.setPackage(ctx.getPackageName());
        if (xep.getMessageId() == null) {
            intentOne.putExtra(RoosterConnectionService.MESSAGE_ID, "ALL");

        } else {
            intentOne.putExtra(RoosterConnectionService.MESSAGE_ID, xep.getMessageId());

        }
        intentOne.putExtra(RoosterConnectionService.XEP_STATUS, xep.getStatus());
        ctx.sendBroadcast(intentOne);
    }


    public void updateStatus(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            String updateSingleChat = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + PreferenceConstant.SENT + "' WHERE " + MESSAGE_ID + "='" + message.getPacketID() + "' AND " + STATUS + "='" + PreferenceConstant.SENT_LATER + "'";
            db.execSQL(updateSingleChat);


            String updateFileChat = "UPDATE " + TABLE_FILES + " SET " + STATUS + "='" + PreferenceConstant.SENT + "' WHERE " + MESSAGE_ID + "='" + message.getPacketID() + "' AND " + STATUS + "='" + PreferenceConstant.SENT_LATER + "'";
            db.execSQL(updateFileChat);

            String updateXEPStatusForDelivered = "UPDATE " + TABLE_XEP + " SET " + STATUS + "='" + PreferenceConstant.MSG_DELIVERED_SUCCESS + "' WHERE " + MESSAGE_ID + "='" + message.getPacketID() + "' AND " + STATUS + "='" + PreferenceConstant.MSG_DELIVERED + "'";
            db.execSQL(updateXEPStatusForDelivered);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();

        }
    }

    public void updateXEP(XEP xep) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String updateXEPStatusForDelivered = "UPDATE " + TABLE_XEP + " SET " + STATUS + "='" + xep.getStatus() + "' WHERE " + MESSAGE_ID + "='" + xep.getMessageId() + "'";
            db.execSQL(updateXEPStatusForDelivered);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Getting chat
    public ArrayList<ChatBody> getEncryptedChat(String me, String you) {
        ArrayList<ChatBody> chatBodyLst = new ArrayList<ChatBody>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + OWNER_JID + "='" + me + "' AND  " + FRIEND_JID + "='" + you + "' AND  " + CATEGORY + "='" + PreferenceConstant.ENCRYPTED + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    ChatBody chatBody = new ChatBody();

                    chatBody.setOwnerJID(cursor.getString(cursor.getColumnIndex(OWNER_JID)));

                    String friendJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                    chatBody.setFriendJID(friendJid);

                    String messageID = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                    chatBody.setMessageID(messageID);

                    String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
                    chatBody.setMessage(message);

                    String messageType = cursor.getString(cursor.getColumnIndex(MESSAGE_TYPE));
                    chatBody.setMessageType(messageType);

                    String inOutBound = cursor.getString(cursor.getColumnIndex(IN_OUT_BOUND));
                    chatBody.setInOutBound(inOutBound);

                    chatBody.setProgress(cursor.getString(cursor.getColumnIndex(PROGRESS)));

                    String status = cursor.getString(cursor.getColumnIndex(STATUS));
                    chatBody.setStatus(status);

                    chatBody.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                    chatBody.setTimeStamp(cursor.getString(cursor.getColumnIndex(TIME_STAMP)));
                    chatBody.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                    chatBody.setPriority(cursor.getInt(cursor.getColumnIndex(PRIORITY)));
                    chatBodyLst.add(chatBody);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return chatBodyLst;
    }


    public void UpdateChatMessageToDB(ChatBody chat) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(OWNER_JID, chat.getOwnerJID());
            values.put(FRIEND_JID, chat.getFriendJID());
            values.put(MESSAGE_ID, chat.getMessageID());
            values.put(MESSAGE, chat.getMessage());
            values.put(MESSAGE_TYPE, chat.getMessageType());
            values.put(IN_OUT_BOUND, chat.getInOutBound());
            values.put(DATE, chat.getDate());
            values.put(TIME_STAMP, chat.getTimeStamp());
            values.put(PROGRESS, chat.getProgress());
            values.put(STATUS, chat.getStatus());
            values.put(CATEGORY, chat.getCategory());
            values.put(EVAP_DURATION, chat.getEvapDuration());
            values.put(EVAP_TIME, chat.getEvapTime());
            values.put(PRIORITY, chat.getPriority());
            updateIntoOpenCon(chat, db);
            db.update(TABLE_CHAT, values, "MessageID='" + chat.getMessageID() + "'", null);

            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void updateIntoOpenCon(ChatBody chat, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            String type = chat.getMessageType();
            String message = chat.getMessage();

            if (chat.getCategory() == PreferenceConstant.ENCRYPTED) {
                message = "Encrypted Message";
            } else if (type.equals(PreferenceConstant.IMAGE)) {
                message = "Image";

            } else if (type.equals(PreferenceConstant.VIDEO)) {
                message = "Video";

            } else if (type.equals(PreferenceConstant.AUDIO)) {
                message = "Audio";

            } else if (type.equals(PreferenceConstant.STICKER)) {
                message = "Sticker";
            }
            else if (type.equals(PreferenceConstant.LOCATION)) {
                message = "Location";
            }
            else if (type.equals(PreferenceConstant.DOCUMENT)) {
                message = "Doc";
            }

            values.put(MESSAGE, message);
            db.update(TABLE_OPEN_CONVERSATION, values, "FriendJID='" + chat.getFriendJID() + "'", null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void updateAllXEPStatusInMsgTables(XEP xep) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String updateSingleChat = "UPDATE " + TABLE_CHAT + " SET " + STATUS + "='" + xep.getStatus() +
                    "' WHERE (" + FRIEND_JID + "='" + xep.getFriendJID() + "') AND ("
                    + STATUS + "='" + PreferenceConstant.SENT + "' OR " + STATUS + "='" + PreferenceConstant.DELIVERED + "')";
            db.execSQL(updateSingleChat);
            db.close();

            notifyChatWindows(xep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAllReadXEP(String friendJID) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_XEP + " WHERE " + FRIEND_JID + "='" + friendJID + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        String messageId = "";
        boolean isExist = false;

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                try {

                    if (cursor.moveToFirst()) {
                        do {
                            messageId = cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                            isExist = true;

                            break;
                        } while (cursor.moveToNext());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();
        db.close();


        if (isExist) {
            /* Send IM Message Of XEP*/
            XEP xep = new XEP();
            xep.setStatus(PreferenceConstant.READ);
            xep.setFriendJID(friendJID);
            xep.setMessageId(messageId);
            xep.setSubject(PreferenceConstant.XEP_ALL_READ_RECEIPT);
            XmppConnect.getInstance().sendXEPReceipt(xep);
        }
    }


    public void deleteXEPRowForJID(String FriendJID) {
        try {
            FriendJID = Utility.getBareJID(FriendJID);

            SQLiteDatabase db = this.getWritableDatabase();
            String delete = "DELETE FROM " + TABLE_XEP + "  WHERE " + FRIEND_JID + "='" + FriendJID + "'";
            db.execSQL(delete);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteAllopenConversation() {

        String delete;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            delete = "DELETE FROM " + TABLE_OPEN_CONVERSATION;
            db.execSQL(delete);
            db.close();
        } catch (Exception e) {
            db.close();
        }

    }
    //  XEP-0184 Implementation
    public void addEntryAndSendReadXEP(ChatBody chat) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MESSAGE_ID, chat.getMessageID());

            String friendJid = chat.getFriendJID().replace("/Shield", "");
            values.put(FRIEND_JID, friendJid);
            values.put(STATUS, PreferenceConstant.MSG_DELIVERED);

            db.insert(TABLE_XEP, null, values);
            db.close();

            String messageId = chat.getMessageID();

            try {
            /* Send IM Message Of XEP*/
                XEP xep = new XEP();
                xep.setMessageId(messageId);
                xep.setStatus(PreferenceConstant.READ);
                xep.setFriendJID(friendJid);
                xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                XmppConnect.getInstance().sendXEPReceipt(xep);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public ArrayList<RecentChat> getChatByGroup() {
        ArrayList<RecentChat> recentLst = new ArrayList<RecentChat>();
        String ownjid = share.getValue(SharedPrefrence.JID);

        String selectQuery;
        selectQuery = "SELECT  * FROM " + TABLE_OPEN_CONVERSATION + "  GROUP BY " + OWNER_JID + " ," + FRIEND_JID + " ORDER BY " + TIME_STAMP + " DESC";
        DatabaseHandler dbHandler = new DatabaseHandler(ctx);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        RecentChat recentChat = new RecentChat();

                        String friendJid = cursor.getString(cursor.getColumnIndex(FRIEND_JID));
                        recentChat.setFriendJID(friendJid);

                        String name = cursor.getString(cursor.getColumnIndex(NAME));
                        String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
                        recentChat.setCategory(category);
                       // name = getPersonName(cursor.getString(cursor.getColumnIndex(NAME)));



                        recentChat.setName(name);
                        recentChat.setChatCount(getUnreadCount(ownjid, friendJid));
                        String message = cursor.getString(cursor.getColumnIndex(MESSAGE));


                        try {
                            String date = cursor.getString(cursor.getColumnIndex(DATE));
                            date = DateFormatter.getRecentMessageTime(ctx, Long.valueOf(cursor.getString(cursor.getColumnIndex(TIME_STAMP))), date);
                            recentChat.setTime(date);
                        } catch (Exception e) {}

                        recentChat.setLastMessage(message);

                        // Adding contact to list
                        recentLst.add(recentChat);



                      //  MessageTab.searchForAll.put(name.toLowerCase(Locale.getDefault()), recentChat);

                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();

        return recentLst;
    }

}
