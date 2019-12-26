package com.customer.orderproupdated.chat.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.BuildConfig;
import com.customer.orderproupdated.DownloadFileAsyn;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.HomeActivity;
import com.customer.orderproupdated.UI.SplashActivity;
import com.customer.orderproupdated.UploadFileToServer;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.chat.rooster.XmppConnect;
import com.customer.orderproupdated.chat.utility.DialogUtility;
import com.customer.orderproupdated.database.DatabaseHandler;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ChatWindow extends AppCompatActivity implements TextWatcher {
    ChatBody downloadChatBody = null;
    boolean isTakePhotoClicked = false, isAudioRecorderClicked = false, isSendDrawingClicked = false,isfileClicked = false;

    boolean isInternetWorking = false;

    boolean loadShowListContent = true;

    HashMap<String, String> chatTextList = new HashMap<String, String>();
    
    boolean doublePressToScrollBottom = false;

    ImageView scroller;

    int selectionIndex = 0;
    int limiter = 10;
    int ascendingBy = 30;
    int sectionsCount = 0;
    int priorityType = 1;
    String lastMessageId = "";
    String lastUpdateFile = "";
    String lastUpdateStatus = "";
    String lastMessageIdXep = "";
    String lastMessageIdXepStatus = "";
    String lastPacketId = "";

    private BroadcastReceiver mBroadcastUpdateChat;
    private BroadcastReceiver mBroadcastUpdateFile;
    private BroadcastReceiver mBroadcastXEP;

    HashMap<String, Bitmap> bitmapHashMap = new HashMap<String, Bitmap>();
    HashMap<String, Integer> packetPosition = new HashMap<String, Integer>();


    private RelativeLayout footerLayout = null;



    // File upload url (replace the ip with your server address)
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "OrderProUpload";
    public static final int EDITED_IMAGE = 2001;
    public static final int TRACK_LOCATION = 2002;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    //private ChatView mChatView;
    private static final String TAG = "ChatWindow";

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int GALLERY_REQUEST_CODE_IMAGE = 300;
    private static final int GALLERY_REQUEST_CODE_VIDEO = 400;
    private static final int PICK_CONTACT_REQUEST_CODE = 500;
    private static final int FILE_REQUEST_CODE = 600;

    private static final int GALLERY_REQUEST_CODE = 1010;



    boolean recentClicked = false;
    Bitmap contactBitmap = null;


    ImageView sendMessagebutton;
    com.customer.orderproupdated.custom_fonts.CustomEditTxt inputEditText;
    ListView chatlist;
    RelativeLayout ll_list;


    //TextView tv_user_name;
   // com.procrypt.chat.view.OpenLightTextView tv_user_status;
    ChatListAdapter chatListAdapter;
    ArrayList<ChatBody> populateChat = new ArrayList<ChatBody>();
    ArrayList<ChatBody> encryptedChat = new ArrayList<ChatBody>();
    ArrayList<ChatBody> templist = new ArrayList<ChatBody>();

    SharedPrefrence share;
    DatabaseHandler db;


    LinearLayout ll_attachement;
    MediaPlayer mMediaPlayer = null;
    Handler mHandler;
    ImageView chat_bg;

    ProgressBar progress;

    private String contactJid, user_name, ownjid;
    private BroadcastReceiver mBroadcastReceiver;
    private BroadcastReceiver presenceReceiver;
    private BroadcastReceiver mfiledownloadBroadcast;


    private Uri fileUri = null; // file url to store image
    long sizeLimitinKb = 10;
    private boolean isfromnotification = false;
    private boolean forwardmessage = false;
    ChatBody mchatBody;
    String m_name="";
    private ActionMode mActionMode;
    int viewSelected = 0;


    Toolbar toolbar;
    ActionBar actionBar;
    ImageView iv_add_icon_black;
    boolean is_merchant_switched;
    RelativeLayout empty_layout;
    private static final int SELECT_PICTURE = 1;
    MerchantDetail merchantDetail;
    Menu menu;
    private  android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_interface);
        share=SharedPrefrence.getInstance(this);
        //----for image open in android-N
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progress = (ProgressBar) findViewById(R.id.progress);
        footerLayout = (RelativeLayout) findViewById(R.id.footer);
        ll_attachement = (LinearLayout) findViewById(R.id.ll_attachement);
        iv_add_icon_black = (ImageView) findViewById(R.id.iv_add_icon);
        empty_layout=(RelativeLayout) findViewById(R.id.empty_layout_chat);
        ll_list = (RelativeLayout) findViewById(R.id.ll_list);
        is_merchant_switched = share.getBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED);
        chatlist = (ListView) findViewById(R.id.chatlist);
        //Prepare toolbar
         toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
         actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG("GetSupportActionBar returned null.");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isfromnotification) {
                    Intent in = new Intent(ChatWindow.this, HomeActivity.class);
                    startActivity(in);
                }
                finish();
            }
        });



        if(getIntent()!=null)
        {
            if(getIntent().hasExtra("username"))
            {
                user_name=getIntent().getStringExtra("username");
            }
        }

        if(user_name.contains("@"))
        {
             m_name = user_name.split("@")[0];
            actionBar.setTitle(Utility.capitalize(m_name));
            contactJid= m_name+PreferenceConstant.SERVERATTHERSTE;

        }
        else
        {
            if(!user_name.equalsIgnoreCase(""))
            {
//                m_name=user_name;
                actionBar.setTitle(Utility.capitalize(user_name));
                contactJid= Utility.lowercaseAllLetters(user_name)+PreferenceConstant.SERVERATTHERSTE;

            }else
            {
                actionBar.setTitle("Chat");

                if(populateChat.size()==0)
                {
                    footerLayout.setVisibility(View.INVISIBLE);
                    empty_layout.setVisibility(View.VISIBLE);
                }
            }
        }

       // actionBar.setTitle("Chat");

        if (is_merchant_switched==true) {

            footerLayout.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        }

//-----------------------------------------------------------------------------

        cancelNotification();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        SplashActivity.screenWidth = displaymetrics.widthPixels;
        SplashActivity.screenWidth = (int) (SplashActivity.screenWidth / 2);




        mHandler = new Handler();
        share = SharedPrefrence.getInstance(this);
        db = new DatabaseHandler(this);

        ownjid= share.getValue(SharedPrefrence.JID);





        if (getIntent().hasExtra("CHAT")) {

            forwardmessage = true;
            mchatBody = (ChatBody) getIntent().getSerializableExtra("CHAT");
        }

        if (getIntent().hasExtra("fromnotification")) {

            isfromnotification = true;
        }



        sendMessagebutton = (ImageView) findViewById(R.id.sendmessagebutton);

        inputEditText = (com.customer.orderproupdated.custom_fonts.CustomEditTxt) findViewById(R.id.message_ET);
        scroller = (ImageView) findViewById(R.id.scroller);
        attachmentIconDeselector();


        inputEditText.addTextChangedListener(this);
        inputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                attachmentIconDeselector();
                return false;
            }
        });

        chatTextList = share.getHash(SharedPrefrence.CHAT_TEXT_USERS);
        if (chatTextList.get(contactJid) != null) {
            inputEditText.setText(chatTextList.get(contactJid));

            chatTextList.remove(contactJid);
            share.setHash(SharedPrefrence.CHAT_TEXT_USERS, chatTextList);
        }

        scroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doublePressToScrollBottom) {

                    chatlist.setSelection(populateChat.size());
                }

                doublePressToScrollBottom = true;

                chatlist.smoothScrollToPosition(populateChat.size());

                try {
                    new android.os.Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {doublePressToScrollBottom = false;
                        }
                    }, 2000);
                } catch (Exception e) {

                }
            }
        });

        scroller.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                chatlist.setSelection(populateChat.size());
                return false;
            }
        });

        chatlist.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if (firstVisibleItem == 0 && totalItemCount == (limiter + sectionsCount)) {

                    try {
                        int cursorCount = db.getchatCount(ownjid, contactJid);

                        if ((totalItemCount - sectionsCount) < cursorCount) {

                            selectionIndex = limiter;

                            limiter = limiter + ascendingBy;

                            selectionIndex = (limiter + sectionsCount) - selectionIndex;

//                            showListContent();

                            populateChat = db.loadMoreUserChat(ownjid, contactJid, limiter, totalItemCount, populateChat);
                            appendImageContent();

                            updateChatList();
                            chatlist.setSelection(selectionIndex);
                        }

                    } catch (Exception e) {
                    }
                }

                if (firstVisibleItem < (totalItemCount - (visibleItemCount + 5))) {
                    if (scroller.getVisibility() == View.GONE) {
                        scroller.setAnimation(AnimationUtils.loadAnimation(ChatWindow.this, android.R.anim.fade_in));
                        scroller.setVisibility(View.VISIBLE);
                    }
                } else {
                    scroller.setAnimation(AnimationUtils.loadAnimation(ChatWindow.this, android.R.anim.fade_out));
                    scroller.setVisibility(View.GONE);
                }
            }
        });

      //  actionBar.setTitle(user_name);

        sendMessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMsgBody();
            }
        });
        ll_attachement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PermissionFramework.isDirectAccessSinglePermission(ChatWindow.this, PermissionFramework.PG_STORAGE)) {

                 //   galleryOperation();
                    selectImage();

                } else {
                    // Check Permission
                    Intent gotoPermissionRequest = new Intent(ChatWindow.this, PermissionFramework.class);
                    gotoPermissionRequest.putExtra(PermissionFramework.PERMISSION_GROUP, PermissionFramework.PG_STORAGE);
                    startActivityForResult(gotoPermissionRequest, PermissionFramework.PERMISSION_REQUEST_CODE);
                }

            }
        });

        hideKeyBoard();

    }

    public void hideKeyBoard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ll_attachement.getWindowToken(), 0);
    }

    public void vTakePhotoVideo() {
        optionsCamera();
    }



    protected void optionsCamera() {

        final CharSequence[] items = {"Take Photo", "Take Video"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatWindow.this, R.style.MyDialogTheme);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {


                if (position == 0) {
                    dialog.dismiss();
                    takeImageFromCamera();
                } else if (position == 1) {
                    dialog.dismiss();
                    takeVideoFromCamera();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void takeImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void takeVideoFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }




    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public void vGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
      //nup  Intent goToGallery = new Intent(ChatWindow.this, GalleryTab.class);
       // startActivityForResult(goToGallery, GALLERY_REQUEST_CODE);
    }



    @Override
    public void onBackPressed() {


            if (isfromnotification) {
                Intent in = new Intent(ChatWindow.this, HomeActivity.class);
                startActivity(in);
            }
            finish();
        }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         /* CAPTURE IMAGE FROM CAMERA */
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                double fileSizeInMB = Utility.getFileSize(fileUri.getPath());

                if (fileSizeInMB > 10) {
                    DialogUtility.showToast(this,  getString(R.string.alter_file_limit));
                } else {
                    ChatBody chatBody = getChatBodyForFile(fileUri.getPath(), PreferenceConstant.IMAGE);

                    if (chatBody != null) {
                        new UploadFileToServer(this, chatBody).execute();
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.alter_capure_image), Toast.LENGTH_SHORT).show();
            }

        }



 /* CAPTURE IMAGE FROM GALLERY */
       else if (requestCode == GALLERY_REQUEST_CODE_IMAGE) {

            try {
                if (resultCode == RESULT_OK) {

                    String filePath = data.getDataString();
                    filePath = getImageUrlWithAuthority(getApplicationContext(), Uri.parse(filePath));

                    if (filePath != null) {

                        String converted_Path = Utility.getRealPathFromURI(getApplicationContext(), filePath);

                        double fileSizeInMB = Utility.getFileSize(converted_Path);

                        if (fileSizeInMB > 10) {
                            DialogUtility.showToast(this, getString(R.string.alter_file_limit));
                        } else {
                            fileUri = Uri.parse(data.getDataString());
                            ChatBody chatBody = getChatBodyForFile(converted_Path, PreferenceConstant.IMAGE);
                            if (chatBody != null) {
                                new UploadFileToServer(this, chatBody).execute();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.alert_imageformat), Toast.LENGTH_SHORT).show();
                    }

                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled recording
                    //  Toast.makeText(getApplicationContext(), "User cancelled gallery", Toast.LENGTH_SHORT).show();

                } else {
                    // failed to record video
                    Toast.makeText(getApplicationContext(), getString(R.string.alert_take_resourecs), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),getString(R.string.alert_take_resourecs), Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PermissionFramework.PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String permissionResult = data.getExtras().getString(PermissionFramework.PERMISSION_RESULT);

                String pName = data.getExtras().getString(PermissionFramework.PERMISSION_NAME);
                String pGroup = data.getExtras().getString(PermissionFramework.PERMISSION_GROUP);

                if (permissionResult.equals(PermissionFramework.PERMISSION_SUCCESS)) {


                     if (pGroup.equals(PermissionFramework.PG_STORAGE)) {


                         if (isTakePhotoClicked) {

                             Intent gotoPermissionRequest = new Intent(ChatWindow.this, PermissionFramework.class);
                             gotoPermissionRequest.putExtra(PermissionFramework.PERMISSION_GROUP, PermissionFramework.PG_CAMERA);
                             startActivityForResult(gotoPermissionRequest, PermissionFramework.PERMISSION_REQUEST_CODE);

                             isTakePhotoClicked = false;

                         }
                         else {
                             if (downloadChatBody == null) {

                                 // galleryOperation();
                                 selectImage();

                             } else {

                                 downloadFileOperation();
                             }
                         }

                    }
                }
            }
        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    public ChatBody getChatBodyForFile(String localUri, String messageType) {

        ChatBody chatBody = null;


            String msg = "";
            String progress = "0";
            Message message = new Message();
            if (messageType.equals(PreferenceConstant.IMAGE)) {
                msg = "Image";
            } else if (messageType.equals(PreferenceConstant.AUDIO)) {
                msg = "Audio";
            } else if (messageType.equals(PreferenceConstant.VIDEO)) {
                msg = "Video";
            }
            else if (messageType.equals(PreferenceConstant.DOCUMENT)) {
                msg = "Document";
            }

                chatBody = Utility.getChatBodyForChatType(msg.trim(), ownjid, contactJid,
                        messageType, PreferenceConstant.OUT_BOUND, PreferenceConstant.fileUploding, message,
                        PreferenceConstant.MESSAGE_TYPE_CHAT, priorityType);



//        }

            db.addChatMessageToDB(chatBody);


            chatBody.setLocalUri(localUri);
            chatBody.setProgress("0");

            chatBody.setExtension(Utility.getFileExtension(new File(localUri)));


            if (messageType.equals(PreferenceConstant.IMAGE)) {
                chatBody.setThumb(Utility.getImageThumbDynamicSize(localUri, ChatWindow.this));
            }


            db.addFilesToDB(chatBody);

            populateChat.add(chatBody);
            // showListContent();
            appendImageContent();
            updateChatList();



        return chatBody;
    }


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(presenceReceiver);
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mfiledownloadBroadcast);
        updateUnreadCount();
        unregisterReceiver(mBroadcastUpdateChat);
        unregisterReceiver(mBroadcastUpdateFile);
        unregisterReceiver(mBroadcastXEP);

    }

    public void updateUnreadCount() {
        db.updateUnreadCount(ownjid, contactJid);
    }

    public void showListContent() {
        populateChat = db.getChat(ownjid, contactJid, limiter);
        appendImageContent();

        updateChatList();

//        db.getXepByFriendJid(contactJid);
        db.sendAllReadXEP(contactJid);
    }

    public void appendImageContent() {

        boolean isInternetWorking = Utility.checkInternetConn(ChatWindow.this);

        sectionsCount = 0;

        try {
            for (int i = 0; i < populateChat.size(); i++) {

                ChatBody filesInfo = populateChat.get(i);

                if ((filesInfo.getMessageType()).equals(PreferenceConstant.DATE_SECTION)) {
                    sectionsCount++;
                }

                filesInfo = fullFillParams(filesInfo, isInternetWorking);

                populateChat.set(i, filesInfo);
            }
        } catch (Exception e) {

        }

    }

    public ChatBody fullFillParams(ChatBody filesInfo, boolean isInternetWorking) {

        ChatBody temp = db.getFileInfo(filesInfo.getMessageID());

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
        db = new DatabaseHandler(this);
        temp.setStatus(PreferenceConstant.fileFail);
        db.updateFileInfo(temp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetWorking = Utility.checkInternetConn(ChatWindow.this);
        PreferenceConstant.RESTRICT_NOTIFICATION = contactJid;
        attachmentIconDeselector();
        chat_bg = (ImageView) findViewById(R.id.chat_bg);
//        int index = Integer.parseInt(share.getValue(SharedPrefrence.CHAT_BG));
        showListContent();
        updatePresence();
        db.sendAllReadXEP(contactJid);


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case RoosterConnectionService.NEW_MESSAGE:

                        updateUnreadCount();

                        String MessageId = intent.getStringExtra(RoosterConnectionService.BUNDLE_MESSAGE_ID) + "";
                        String Messagefrom = intent.getStringExtra(RoosterConnectionService.BUNDLE_FROM_JID);


                        if (Messagefrom != null && Messagefrom.equals(contactJid)) {
                            if (MessageId.length() > 0) {
                                if (MessageId.equals(lastPacketId)) {
                                    return;
                                }

                                lastPacketId = MessageId;

                                ChatBody chatBody = db.getChatByMessageID(ownjid, contactJid, MessageId);
                                chatBody = fullFillParams(chatBody, isInternetWorking);
                                populateChat.add(chatBody);
                                chatListAdapter.notifyDataSetChanged();

                                db.getXepByFriendJid(contactJid);
                            }
                        }


//                        showListContent();
//                        GetEncryptedChat();

                        return;
                }

            }
        };

        mfiledownloadBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case RoosterConnectionService.FILE_DOWNLOAD_RECEIVER:
                        updateUnreadCount();
                        showListContent();
                        return;
                }

            }
        };


        presenceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePresence();
            }
        };

        IntentFilter filter = new IntentFilter(RoosterConnectionService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);

        IntentFilter presence = new IntentFilter(RoosterConnectionService.PRESENCE_CHANGE);
        registerReceiver(presenceReceiver, presence);

        IntentFilter filterfile = new IntentFilter(RoosterConnectionService.FILE_DOWNLOAD_RECEIVER);
        registerReceiver(mfiledownloadBroadcast, filterfile);

        updateUnreadCount();

        mBroadcastUpdateChat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case RoosterConnectionService.UPDATE_CHAT:

                        String MessageId = intent.getStringExtra(RoosterConnectionService.MESSAGE_ID);
                        if (MessageId.equals(lastMessageId)) {
                            return;
                        }

                        updateViewOfListView(MessageId);

                        return;
                }
            }
        };

        IntentFilter updateChat = new IntentFilter(RoosterConnectionService.UPDATE_CHAT);
        registerReceiver(mBroadcastUpdateChat, updateChat);


        mBroadcastUpdateFile = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case RoosterConnectionService.UPDATE_FILE:

                        String MessageId = intent.getStringExtra(RoosterConnectionService.MESSAGE_ID);
                        String FileStatus = intent.getStringExtra(RoosterConnectionService.FILE_STATUS);

                        if (MessageId.equals(lastUpdateFile) && FileStatus.equals(lastUpdateStatus)) {
                            return;
                        }

                        updateFileOfListView(MessageId, FileStatus);

                        return;
                }
            }
        };

        IntentFilter updateFile = new IntentFilter(RoosterConnectionService.UPDATE_FILE);
        registerReceiver(mBroadcastUpdateFile, updateFile);


        mBroadcastXEP = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case RoosterConnectionService.UPDATE_XEP:

                        String MessageId = intent.getStringExtra(RoosterConnectionService.MESSAGE_ID);
                        String XepStatus = intent.getStringExtra(RoosterConnectionService.XEP_STATUS);

                        if (MessageId.equals(lastMessageIdXep)) {
                            return;
                        }

                        if (MessageId.equals("ALL")) {
                            showListContent();
                        } else {
                            updateViewOfListViewForXEP(MessageId, XepStatus);

                        }


                        return;
                }
            }
        };

        IntentFilter updateChatXep = new IntentFilter(RoosterConnectionService.UPDATE_XEP);
        registerReceiver(mBroadcastXEP, updateChatXep);

    }


    public void updateFileOfListView(String packet, String fileStatus) {

        lastUpdateFile = packet;
        lastUpdateStatus = fileStatus;

        try {
            int index = packetPosition.get(packet);
            populateChat.set(index, db.getChatByMessageID(ownjid, contactJid, packet));
            updatefileAtPosition(index, fileStatus);
        } catch (Exception e) {
            lastUpdateFile = "";
            lastUpdateStatus = "";
        }
    }

    // mListView is an instance variable
    private void updatefileAtPosition(int position, String status) {

        final ChatBody populate = populateChat.get(position);
        populate.setStatus(status);
        appendImageContent();

        int visiblePosition = chatlist.getFirstVisiblePosition();
        View view = chatlist.getChildAt(position - visiblePosition);
        chatlist.getAdapter().getView(position, view, chatlist);

        updateFileViewOnPosition(view, populate, position);
    }

    public void updateViewOfListView(String packet) {

        lastMessageId = packet;

        try {
            int index = packetPosition.get(packet);
            populateChat.set(index, db.getChatByMessageID(ownjid, contactJid, packet));
            updateItemAtPosition(index);
        } catch (Exception e) {
            lastMessageId = "";
        }
    }

    // mListView is an instance variable
    private void updateItemAtPosition(int position) {

        ChatBody populate = populateChat.get(position);
        populate = db.appendImageContentByMessageID(ChatWindow.this, populate);

        populate.setStatus(PreferenceConstant.SENT);

        int visiblePosition = chatlist.getFirstVisiblePosition();
        View view = chatlist.getChildAt(position - visiblePosition);
        chatlist.getAdapter().getView(position, view, chatlist);

        if (populate.getMessageType().equals(PreferenceConstant.EDIT_SINGLE_USER_TEXT)) {
            LayoutInflater inflater = LayoutInflater.from(ChatWindow.this);
            if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {
                view = inflater.inflate(R.layout.chat_view_edit_text_incoming_bubble, null);
            } else {
                view = inflater.inflate(R.layout.chat_view_edit_text_outgoing_bubble, null);
            }
        }

        try {
            TextView message_body = (TextView) view.findViewById(R.id.message_body);
            message_body.setText(populate.getMessage());

            RelativeLayout chatBubble = (RelativeLayout) view.findViewById(R.id.chatBubble);
            setBubbleBackground(populate, chatBubble);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView iv_clock = (ImageView) view.findViewById(R.id.iv_clock);
        iv_clock.setImageResource(R.drawable.delivered_right_tick);

        chatListAdapter.notifyDataSetChanged();
    }


    public void updateViewOfListViewForXEP(String packet, String XepStatus) {

        lastMessageIdXep = packet;

        lastMessageIdXepStatus = packet + XepStatus;

        try {
            int index = packetPosition.get(packet);
            populateChat.set(index, db.getChatByMessageID(ownjid, contactJid, packet));
            updateItemAtPositionForXEP(index, XepStatus);
        } catch (Exception e) {
            lastMessageIdXep = "";
        }
    }

    // mListView is an instance variable
    private void updateItemAtPositionForXEP(int position, String XepStatus) {

        populateChat.get(position).setStatus(XepStatus);

        int visiblePosition = chatlist.getFirstVisiblePosition();
        View view = chatlist.getChildAt(position - visiblePosition);
        chatlist.getAdapter().getView(position, view, chatlist);

        ImageView iv_clock = (ImageView) view.findViewById(R.id.iv_clock);
        if (XepStatus.equals(PreferenceConstant.DELIVERED)) {
            iv_clock.setImageResource(R.drawable.delivered_tick);
            lastMessageIdXep = "";
        } else if (XepStatus.equals(PreferenceConstant.READ)) {
            iv_clock.setImageResource(R.drawable.read_tick);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (inputEditText.getText().length() > 0) {

            sendMessagebutton.setVisibility(View.VISIBLE);
            // Chat_Keyboard.setVisibility(View.GONE);
        } else {

            //  sendMessagebutton.setVisibility(View.GONE);
            //  Chat_Keyboard.setVisibility(View.VISIBLE);
        }
    }



    public void updateChatList() {

        packetPosition.clear();

        chatListAdapter = new ChatListAdapter(populateChat);
        chatlist.setAdapter(chatListAdapter);
    }

    public void sendMessage(String messageType) {
        ChatBody chat = null;

        String msg = inputEditText.getText().toString();
        msg = msg.trim();

        if (msg.trim().length() != 0) {



            chat = Utility.getChatBodyForChatType(msg, ownjid, contactJid,
                    PreferenceConstant.TEXT, PreferenceConstant.OUT_BOUND, messageType, new Message(),
                    PreferenceConstant.MESSAGE_TYPE_CHAT, priorityType);
            db.addChatMessageToDB(chat);

            Intent intent = new Intent(RoosterConnectionService.SEND_MESSAGE);
            intent.putExtra("CHAT", chat);
            sendBroadcast(intent);

            inputEditText.setText("");

            if (loadShowListContent) {

                showListContent();

                loadShowListContent = false;
            } else {

                populateChat.add(chat);
                appendImageContent();
                updateChatList();


            }
        }

    }


    public void createMsgBody() {
        sendMessage(PreferenceConstant.SENT_LATER);
    }

    public void updatePresence() {

        String status = "Offline";

        try {

            if (XmppConnect.mConnection.isConnected()) {

                Presence userFromServer = XmppConnect.mConnection.getRoster().getPresence(contactJid);

                if (userFromServer.isAvailable()) {
                    status = "Online";
                } else if (userFromServer.isAway()) {
                    status = "Away";
                }
            }

        } catch (Exception e) {
        }

      //  actionBar.setSubtitle(status);


    }





    private SparseBooleanArray mSelectedItemsIds;

    public class ChatListAdapter extends BaseAdapter {
        ArrayList<ChatBody> populateChat;

        public ChatListAdapter(ArrayList<ChatBody> populateChat) {
            this.populateChat = populateChat;
            mSelectedItemsIds = new SparseBooleanArray();

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
        public View getView(final int position, View convertView,
                            ViewGroup parent) {


            final ChatBody populate = populateChat.get(position);

            packetPosition.put(populate.getMessageID(), position);

            return updateChatRowItems(populate, position, convertView);

        }


        //Toggle selection methods
        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
            notifyDataSetChanged();
        }


        //Remove selected selections
        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }


        //Put or delete selected position into SparseBooleanArray
        public void selectView(int position, boolean value) {
            if (value) {
                mSelectedItemsIds.put(position, value);


            } else {
                mSelectedItemsIds.delete(position);
                // chatlist.getChildAt(position).setSelected(false);
                // chatlist.getChildAt(position).setBackgroundColor(Color.WHITE);


            }


            notifyDataSetChanged();
        }

        //Get total selected count
        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }

        //Return all selected ids
        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }


    }

    public void setBubbleBackground(ChatBody populate, RelativeLayout chatBubble) {

        if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {
             chatBubble.setBackgroundResource(R.drawable.bubble_normal_incoming);
        } else {
            chatBubble.setBackgroundResource(R.drawable.bubble_normal_outgoing);
        }
    }


    public void attachmentIconDeselector() {
        iv_add_icon_black.setSelected(false);
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



    private long Getfilesize(long sizeInBytes) {
        long sizeInMb = sizeInBytes / (1024 * 1024);
        return sizeInMb;
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_chat, menu);

        MenuItem item_plus = menu.findItem(R.id.action_plus);
        item_plus.setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView sv = new SearchView((this).getSupportActionBar().getThemedContext());
        sv.setQueryHint("Search");

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_NEVER);
        MenuItemCompat.setActionView(item, sv);


        int searchImgId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView v = (ImageView) sv.findViewById(searchImgId);


        int searchclose = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView close = (ImageView) sv.findViewById(searchclose);
        close.setImageResource(R.drawable.ic_action_search);


        int searcheditId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editv = (EditText) sv.findViewById(searcheditId);
        editv.setText("Search");


        sv.setOnQueryTextListener(ChatWindow.this);
        sv.setIconifiedByDefault(false);
        // sv.setBackgroundResource(R.color.tab_cond_text);
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("", "Clicked: ");
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                sv.clearFocus();
                sv.setQuery("", false);
                footerLayout.setVisibility(View.VISIBLE);
                populateChat.clear();
                templist = db.getChat(ownjid, contactJid, limiter);
                populateChat.addAll(templist);
                appendImageContent();
                chatListAdapter.notifyDataSetChanged();//notify adapter
                Log.e("", "Closed: ");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(sendMessagebutton, InputMethodManager.SHOW_IMPLICIT);

                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                templist.clear();
                templist.addAll(populateChat);
                footerLayout.setVisibility(View.GONE);
                Log.e("", "Openeed: ");
                return true; // Return true to expand action view
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        getFilter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        getFilter(newText);
        return false;
    }


   */



    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceConstant.RESTRICT_NOTIFICATION = "";
        Intent intent = new Intent(RoosterConnectionService.NEW_MESSAGE);
        sendOrderedBroadcast(intent, null);

        if(inputEditText.getText().length() > 0) {

            chatTextList.put(contactJid, inputEditText.getText().toString() + "");
            share.setHash(SharedPrefrence.CHAT_TEXT_USERS, chatTextList);
        }
    }



    //Implement item click and long click over listview
    private void implementListViewClickListeners() {
        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //If ActionMode not null select item

                if (mActionMode != null) {
                    ChatBody model = populateChat.get(position);

                    String type = model.getMessageType();


                }


            }
        });
        chatlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Select item on long click

                ChatBody model = populateChat.get(position);

                String type = model.getMessageType();



                return true;
            }
        });
    }



    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }


    public void updaterecentchat() {
        int length = populateChat.size();

        if (length > 2) {
            if (!populateChat.get(length - 1).getMessageType().equals(PreferenceConstant.DATE_SECTION)) {
                db.updateOpenConversationmsg(populateChat.get(length - 1));

            } else {
                updatechatindb(2, length);
            }
        }
    }


    public void updatechatindb(int id, int length) {
        if (!populateChat.get(length - id).getMessageType().equals(PreferenceConstant.DATE_SECTION)) {
            db.updateOpenConversationmsg(populateChat.get(length - id));

        } else {
            updatechatindb(id + 1, length);
        }
    }



    public void updateFileViewOnPosition(View convertView, final ChatBody populate, int position) {

        try {

            String status = populate.getStatus();
            final String msgTimeStamp = populate.getTimeStamp();

            if (populate.getMessageType().equals(PreferenceConstant.IMAGE)) {
                if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {

                    final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

                    final ImageView retry = (ImageView) convertView.findViewById(R.id.retry);
                    if (status.equals(PreferenceConstant.fileFail)) {

                        retry.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                retry.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                new UploadFileToServer(ChatWindow.this, populate).execute();
                            }
                        });

                    } else if (status.equals(PreferenceConstant.fileUploding)) {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                    } else {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }

                } else {

                    final ProgressBar downloadProgress = (ProgressBar) convertView.findViewById(R.id.downloadProgress);
                    final ImageView downloadMedia = (ImageView) convertView.findViewById(R.id.downloadMedia);
                    if (populate.getLocalUri() != null) {
                        downloadMedia.setVisibility(View.GONE);
                        downloadProgress.setVisibility(View.GONE);
                    }
                    downloadMedia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            downloadMedia.setVisibility(View.GONE);
                            downloadProgress.setVisibility(View.VISIBLE);
                            downloadFile(populate);
                        }
                    });
                }

                final ImageView imageBubble = (ImageView) convertView.findViewById(R.id.imageBubble);
                if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {
                    try {
                        if (populate.getLocalUri() == null) {
                            populateChat.set(position, db.appendImageContentByMessageID(ChatWindow.this, populate));
                        }

                        if (populate.getLocalUri() != null) {

                            Picasso.with(ChatWindow.this)
                                    .load(Utility.getSDCardPathForUIL(populate.getLocalUri()))
                                    .placeholder(R.drawable.loading_placeholder)
                                    .error(R.drawable.loading_placeholder)
                                    .resize(Utility.calculateWidth(populate.getLocalUri()), SplashActivity.screenWidth)
                                    .into(imageBubble);

//                            Picasso.with(ChatWindow.this)
//                                    .load(Utility.getSDCardPathForUIL(populate.getLocalUri()))
//                                    .placeholder(R.drawable.ic_launcher)
//                                    .error(R.drawable.ic_launcher)
//                                    .resize(Utility.calculateWidth(populate.getLocalUri()), Splash.screenWidth)
//                                    .into(imageBubble, new com.squareup.picasso.Callback() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            try {
//                                                String thumb = populate.getThumb();
//                                                Bitmap bitmap = null;
//                                                bitmap = Utility.decodeBase64ForBubble(thumb);
//                                                bitmapHashMap.put(msgTimeStamp, bitmap);
//                                                if (bitmap != null) {
//                                                    imageBubble.setImageBitmap(bitmap);
//                                                }
//                                            } catch (Exception e) {
//                                            }
//                                        }
//                                    });
                        } else {
                            try {
                                String thumb = populate.getThumb();
                                Bitmap bitmap = null;
                                bitmap = Utility.decodeBase64ForBubble(thumb);
                                bitmapHashMap.put(msgTimeStamp, bitmap);
                                if (bitmap != null) {
                                    imageBubble.setImageBitmap(bitmap);
                                }
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {

                    try {
                        if (populate.getLocalUri() == null) {
                            populateChat.set(position, db.appendImageContentByMessageID(ChatWindow.this, populate));
                        }

                        if (populate.getLocalUri() != null) {
                            Picasso.with(ChatWindow.this)
                                    .load(Utility.getSDCardPathForUIL(populate.getLocalUri()))
                                    .placeholder(R.drawable.loading_placeholder)
                                    .error(R.drawable.loading_placeholder)
                                    .resize(Utility.calculateWidth(populate.getLocalUri()), SplashActivity.screenWidth)
                                    .into(imageBubble);

                        } else {

                            String thumb = populate.getThumb();

                            if (thumb != null) {
                                Bitmap bitmap = null;
                                if (bitmapHashMap.containsKey(msgTimeStamp)) {
                                    bitmap = bitmapHashMap.get(msgTimeStamp);
                                } else {
                                    bitmap = Utility.decodeBase64ForBubble(thumb);
                                    bitmapHashMap.put(msgTimeStamp, bitmap);

                                }
                                if (bitmap != null) {
                                    imageBubble.setImageBitmap(bitmap);

                                } else {
                                    imageBubble.setImageResource(R.drawable.ic_loading);
                                }
                            } else {
                                imageBubble.setImageResource(R.drawable.ic_loading);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        imageBubble.setImageResource(R.drawable.ic_loading);
                    }

                }

            } else if (populate.getMessageType().equals(PreferenceConstant.VIDEO)) {
                if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {

                    final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

                    final ImageView retry = (ImageView) convertView.findViewById(R.id.retry);
                    if (status.equals(PreferenceConstant.fileFail)) {

                        retry.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                retry.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                new UploadFileToServer(ChatWindow.this, populate).execute();

                            }
                        });

                    } else if (status.equals(PreferenceConstant.fileUploding)) {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                    } else {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                } else {

                    final ProgressBar downloadProgress = (ProgressBar) convertView.findViewById(R.id.downloadProgress);
                    final ImageView downloadMedia = (ImageView) convertView.findViewById(R.id.downloadMedia);
                    if (populate.getLocalUri() != null) {
                        downloadMedia.setVisibility(View.GONE);
                        downloadProgress.setVisibility(View.GONE);
                    }
                    downloadMedia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            downloadMedia.setVisibility(View.GONE);
                            downloadProgress.setVisibility(View.VISIBLE);
                            downloadFile(populate);
                        }
                    });
                }

                ImageView imageBubble = (ImageView) convertView.findViewById(R.id.imageBubble);
                ImageView play_video = (ImageView) convertView.findViewById(R.id.play_video);

                String thumb = populate.getThumb();
                try {
                    if (thumb != null) {
                        Bitmap bitmap = null;
                        if (bitmapHashMap.containsKey(msgTimeStamp)) {
                            bitmap = bitmapHashMap.get(msgTimeStamp);
                        } else {
                            bitmap = Utility.decodeBase64ForBubble(thumb);
                            bitmapHashMap.put(msgTimeStamp, bitmap);
                        }
                        if (bitmap != null) {
                            imageBubble.setImageBitmap(bitmap);
                        } else {
                            imageBubble.setImageResource(R.drawable.ic_loading);
                        }
                    } else {
                        imageBubble.setImageResource(R.drawable.ic_loading);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imageBubble.setImageResource(R.drawable.ic_loading);
                }

                if (populate.getLocalUri() != null) {
                    play_video.setVisibility(View.VISIBLE);
                    final View finalConvertView1 = convertView;
                    play_video.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String localUri = populate.getLocalUri() + "";

                            if (localUri != null) {


                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse("file://" + localUri), "video");
                                startActivity(intent);

                            } else if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {

                                ProgressBar downloadProgress = (ProgressBar) finalConvertView1.findViewById(R.id.downloadProgress);
                                ImageView downloadMedia = (ImageView) finalConvertView1.findViewById(R.id.downloadMedia);

                                downloadMedia.setVisibility(View.GONE);
                                downloadProgress.setVisibility(View.VISIBLE);
                                downloadFile(populate);
                            }
                        }
                    });
                }
            } else if(populate.getMessageType().equals(PreferenceConstant.DOCUMENT))
            {
                if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)){


                    final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

                    final ImageView retry = (ImageView) convertView.findViewById(R.id.retry);
                    if (status.equals(PreferenceConstant.fileFail)) {

                        retry.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                retry.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                new UploadFileToServer(ChatWindow.this, populate).execute();

                            }
                        });

                    } else if (status.equals(PreferenceConstant.fileUploding)) {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);

                    } else {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                }
            }
            else if (populate.getMessageType().equals(PreferenceConstant.AUDIO)) {
                if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {


                    final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

                    final ImageView retry = (ImageView) convertView.findViewById(R.id.retry);
                    if (status.equals(PreferenceConstant.fileFail)) {

                        retry.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);

                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                retry.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                new UploadFileToServer(ChatWindow.this, populate).execute();

                            }
                        });

                    } else if (status.equals(PreferenceConstant.fileUploding)) {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);

                    } else {
                        retry.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                } else {

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public View updateChatRowItems(ChatBody populate, int position, View convertView) {

        if (populate == null || populate.getMessageType() == null) {
            try {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.empty_view, null);
                return convertView;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        switch (populate.getMessageType()) {

            case PreferenceConstant.EDIT_SINGLE_USER_TEXT:
                convertView = view_EditTEXT(convertView, populate, position);
                break;

            case PreferenceConstant.TEXT:
                convertView = view_TEXT(convertView, populate, position);
                break;

            case PreferenceConstant.IMAGE:
                convertView = view_IMAGE(convertView, populate, position);
                break;

            case PreferenceConstant.DATE_SECTION:
                convertView = view_DATE_SECTION(convertView, populate, position);
                return convertView;

            default:
                convertView = null;
                break;
        }

        if (convertView != null) {

            String status = populate.getStatus();

            RelativeLayout chatBubble = (RelativeLayout) convertView.findViewById(R.id.chatBubble);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_clock);
            ImageView iv_evap = (ImageView) convertView.findViewById(R.id.iv_evap);

            if (populate.getEvapTime() == 0) {
                iv_evap.setVisibility(View.GONE);
            } else {
                iv_evap.setVisibility(View.VISIBLE);
            }

            if (populateChat.size() == position) {
                if (lastMessageIdXepStatus.contains(populate.getMessageID())) {
                    if (lastMessageIdXepStatus.contains(PreferenceConstant.DELIVERED)) {
                        status = PreferenceConstant.DELIVERED;
                    } else if (lastMessageIdXepStatus.contains(PreferenceConstant.READ)) {
                        status = PreferenceConstant.READ;
                    }
                }
            }
            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {

                iv_clock.setVisibility(View.VISIBLE);

                if (status.equals(PreferenceConstant.SENT)) {
                    iv_clock.setImageResource(R.drawable.delivered_right_tick);
                } else if (status.equals(PreferenceConstant.DELIVERED)) {
                    iv_clock.setImageResource(R.drawable.delivered_tick);
                } else if (status.equals(PreferenceConstant.READ)) {
                    iv_clock.setImageResource(R.drawable.read_tick);
                }
                else if (status.equals(PreferenceConstant.SENT_LATER)) {
                    iv_clock.setVisibility(View.INVISIBLE);
                }
            }



            if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {
                if (populate.getMessageType().equals(PreferenceConstant.AUDIO) ||
                        populate.getMessageType().equals(PreferenceConstant.VIDEO) ||
                        populate.getMessageType().equals(PreferenceConstant.IMAGE)) {
                    if (populate.getLocalUri() == null) {
                        if (Utility.checkItFromSetting(share, SharedPrefrence.AUTOMATIC_DOWNLOAD) && (Utility.checkInternetConn(ChatWindow.this)) && (Utility.checkRestrictData(ChatWindow.this))) {
                            final ImageView downloadMedia = (ImageView) convertView.findViewById(R.id.downloadMedia);
                            ProgressBar downloadProgress = (ProgressBar) convertView.findViewById(R.id.downloadProgress);
                            downloadMedia.setVisibility(View.GONE);
                            downloadProgress.setVisibility(View.VISIBLE);
                            downloadFile(populate);
                        }
                    }
                }
            }

            time.setText(Utility.getFormattedTime(Long.parseLong(populate.getTimeStamp())));

            if (populate.getPriority() == PreferenceConstant.PRIORITY_HIGH || populate.getPriority() == PreferenceConstant.PRIORITY_MEDIUM) {
                if (!populate.getMessageType().equals(PreferenceConstant.CONTACT)) {
                    time.setTextColor(Color.WHITE);
                }
            }

            try {
                setBubbleBackground(populate, chatBubble);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            try {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.empty_view, null);
                return convertView;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        convertView.setBackgroundColor(mSelectedItemsIds.get(position) ? getResources().getColor(R.color.listselector) : Color.TRANSPARENT);

        return convertView;
    }



    private View view_IMAGE(View convertView, final ChatBody populate, final int position) {
        try {
            LayoutInflater inflater = getLayoutInflater();

            String status = populate.getStatus();
            String msgTimeStamp = populate.getTimeStamp();

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {

                convertView = inflater.inflate(R.layout.chat_view_image_outgoing_bubble, null);


                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

                final ImageView retry = (ImageView) convertView.findViewById(R.id.retry);
                if (status.equals(PreferenceConstant.fileFail)) {

                    retry.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);

                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            retry.setVisibility(View.GONE);
                            progress.setVisibility(View.VISIBLE);
                            new UploadFileToServer(ChatWindow.this, populate).execute();
                        }
                    });

                } else if (status.equals(PreferenceConstant.fileUploding)) {
                    retry.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);


                } else {
                    retry.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                }

            } else {

                convertView = inflater.inflate(R.layout.chat_view_image_incoming_bubble, null);


                final ProgressBar downloadProgress = (ProgressBar) convertView.findViewById(R.id.downloadProgress);
                final ImageView downloadMedia = (ImageView) convertView.findViewById(R.id.downloadMedia);

                if (populate.getLocalUri() != null) {
                    downloadMedia.setVisibility(View.GONE);
                    downloadProgress.setVisibility(View.GONE);
                }

                downloadMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        downloadMedia.setVisibility(View.GONE);
                        downloadProgress.setVisibility(View.VISIBLE);
                        downloadFile(populate);
                    }
                });
            }

            ImageView imageBubble = (ImageView) convertView.findViewById(R.id.imageBubble);
            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {
                try {
                    if (populate.getLocalUri() == null) {
                        populateChat.set(position, db.appendImageContentByMessageID(ChatWindow.this, populate));
                    }

                    if (populate.getLocalUri() != null) {
                        Picasso.with(ChatWindow.this)
                                .load(Utility.getSDCardPathForUIL(populate.getLocalUri()))
                                .placeholder(R.drawable.loading_placeholder)
                                .error(R.drawable.loading_placeholder)
                                .resize(Utility.calculateWidth(populate.getLocalUri()), SplashActivity.screenWidth)
                                .into(imageBubble);
                    } else {
                        try {
                            String thumb = populate.getThumb();
                            Bitmap bitmap = null;
                            bitmap = Utility.decodeBase64ForBubble(thumb);
                            bitmapHashMap.put(msgTimeStamp, bitmap);
                            if (bitmap != null) {
                                imageBubble.setImageBitmap(bitmap);
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {

                try {
                    if (populate.getLocalUri() == null) {
                        populateChat.set(position, db.appendImageContentByMessageID(ChatWindow.this, populate));
                    }

                    if (populate.getLocalUri() != null) {
                        Picasso.with(ChatWindow.this)
                                .load(Utility.getSDCardPathForUIL(populate.getLocalUri()))
                                .placeholder(R.drawable.loading_placeholder)
                                .error(R.drawable.loading_placeholder)
                                .resize(Utility.calculateWidth(populate.getLocalUri()), SplashActivity.screenWidth)
                                .into(imageBubble);

                    } else {

                        String thumb = populate.getThumb();

                        if (thumb != null) {
                            Bitmap bitmap = null;
                            if (bitmapHashMap.containsKey(msgTimeStamp)) {
                                bitmap = bitmapHashMap.get(msgTimeStamp);
                            } else {
                                bitmap = Utility.decodeBase64ForBubble(thumb);
                                bitmapHashMap.put(msgTimeStamp, bitmap);

                            }
                            if (bitmap != null) {
                                imageBubble.setImageBitmap(bitmap);

                            } else {
                                imageBubble.setImageResource(R.drawable.ic_loading);
                            }
                        } else {
                            imageBubble.setImageResource(R.drawable.ic_loading);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imageBubble.setImageResource(R.drawable.ic_loading);
                }

            }

            final View finalConvertView = convertView;
            imageBubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String localUri = populate.getLocalUri();

                    if (localUri != null) {
                        File file = new File(localUri);
                        try{
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(ChatWindow.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            intent.setDataAndType(contentUri, "image");
                        } else {
                            intent.setDataAndType(Uri.fromFile(file), "image");
                        }

                        startActivity(intent);
                    }catch(ActivityNotFoundException act)
                         {   Intent intent = new Intent();
                             intent.setAction(Intent.ACTION_VIEW);
                             intent.setDataAndType(Uri.parse("file://" + localUri), "image/*");
                             startActivity(intent);
                      }




                    } else if (populate.getInOutBound().equals(PreferenceConstant.IN_BOUND)) {

                        ProgressBar downloadProgress = (ProgressBar) finalConvertView.findViewById(R.id.downloadProgress);
                        ImageView downloadMedia = (ImageView) finalConvertView.findViewById(R.id.downloadMedia);

                        downloadMedia.setVisibility(View.GONE);
                        downloadProgress.setVisibility(View.VISIBLE);
                        downloadFile(populate);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    private View view_EditTEXT(View convertView, ChatBody populate, final int position) {
        try {

            LayoutInflater inflater = getLayoutInflater();

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {
                convertView = inflater.inflate(R.layout.chat_view_edit_text_outgoing_bubble, null);
            } else {
                convertView = inflater.inflate(R.layout.chat_view_edit_text_incoming_bubble, null);
            }


            TextView message_body = (TextView) convertView.findViewById(R.id.message_body);
            message_body.setText(populate.getMessage());

            if (populate.getPriority() == PreferenceConstant.PRIORITY_HIGH || populate.getPriority() == PreferenceConstant.PRIORITY_MEDIUM) {
                if (populate.getMessageType().equals(PreferenceConstant.TEXT)) {
                    message_body.setTextColor(Color.WHITE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private View view_TEXT(View convertView, ChatBody populate, final int position) {
        try {

            LayoutInflater inflater = getLayoutInflater();

            if (populate.getInOutBound().equals(PreferenceConstant.OUT_BOUND)) {
                convertView = inflater.inflate(R.layout.chat_view_text_outgoing_bubble, null);
            } else {
                convertView = inflater.inflate(R.layout.chat_view_text_incoming_bubble, null);
            }


            TextView message_body = (TextView) convertView.findViewById(R.id.message_body);
            message_body.setText(populate.getMessage());

            if (populate.getPriority() == PreferenceConstant.PRIORITY_HIGH || populate.getPriority() == PreferenceConstant.PRIORITY_MEDIUM) {
                if (populate.getMessageType().equals(PreferenceConstant.TEXT)) {
                    message_body.setTextColor(Color.WHITE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private View view_DATE_SECTION(View convertView, ChatBody populate, final int position) {
        try {

            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.section_header, null);


            TextView sectionHeader = (TextView) convertView.findViewById(R.id.sectionHeader);
            sectionHeader.setText(populate.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////

    public class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                populateChat = db.getChat(ownjid, contactJid, limiter);
                appendImageContent();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.setVisibility(View.GONE);

            if (populateChat.size() == 0) {
                try {
                    populateChat = db.getChat(ownjid, contactJid, limiter);
                    appendImageContent();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            updateChatList();
        }
    }



    public void deleteRows() {

        deleteSelectedChats();
    }


    protected void deleteSelectedChats() {

        SparseBooleanArray selected = chatListAdapter.getSelectedIds();//Get selected ids
        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                db.deleteChatRow(populateChat.get(selected.keyAt(i)).getOwnerJID(), populateChat.get(selected.keyAt(i)).getFriendJID(), populateChat.get(selected.keyAt(i)).getMessageID());

                populateChat.remove(selected.keyAt(i));
                chatListAdapter.notifyDataSetChanged();//notify adapter
            }
        }

        int count = db.getchatCount(ownjid, contactJid);

        if (count == 0) {
            populateChat.clear();
            db.updateOpenConversationmsgdelete(contactJid);
            chatListAdapter.notifyDataSetChanged();//notify adapter
        } else {
            try {
                updaterecentchat();
            } catch (Exception e) {
            }
        }

        Toast.makeText(ChatWindow.this, selected.size() + " chats deleted", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use
    }


    public void downloadFile(ChatBody populate) {
        // new FileDownloader(this, populate).start();

        downloadChatBody = populate;

        if (PermissionFramework.isDirectAccessSinglePermission(ChatWindow.this, PermissionFramework.PG_STORAGE)) {

            downloadFileOperation();

        } else {
            Intent gotoPermissionRequest = new Intent(ChatWindow.this, PermissionFramework.class);
            gotoPermissionRequest.putExtra(PermissionFramework.PERMISSION_GROUP, PermissionFramework.PG_STORAGE);
            startActivityForResult(gotoPermissionRequest, PermissionFramework.PERMISSION_REQUEST_CODE);
        }
    }


    private void downloadFileOperation() {

        new DownloadFileAsyn(this, downloadChatBody, "CHAT").execute();

        downloadChatBody = null;
    }



   /* private void galleryOperation() {


        if (Utility.checkInternetConn(ChatWindow.this)) {
            vGallery();
        } else {
        DialogUtility.showMaterialDialog(ChatWindow.this, getString(R.string.alert), getString(R.string.filesend_alert));
        }
    }*/





    private static File getOutputMediaFile(int type) {



        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), "OrderPro/" + IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "PROCRYPT_IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "PROCRYPT_VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void selectImage() {
        final CharSequence[] options = {"Gallery","Camera", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatWindow.this);
        builder.setTitle("Add Attachment");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean chooser = false;

                if (options[item].equals("Camera")) {
                    ArrayList<String> requestParams = new ArrayList<String>();
                    requestParams.add(PermissionFramework.PG_STORAGE);
                    requestParams.add(PermissionFramework.PG_CAMERA);

                    if (PermissionFramework.isDirectAccessMultiplePermission(ChatWindow.this, requestParams)) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            File file=new File(fileUri.getPath());
                            Uri contentUri = FileProvider.getUriForFile(ChatWindow.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        }

                        // start the image capture Intent
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                    } else {
                        // Check Permission
                        isTakePhotoClicked = true;
                        Intent gotoPermissionRequest = new Intent(ChatWindow.this, PermissionFramework.class);
                        gotoPermissionRequest.putExtra(PermissionFramework.PERMISSION_GROUP, PermissionFramework.PG_STORAGE);
                        startActivityForResult(gotoPermissionRequest, PermissionFramework.PERMISSION_REQUEST_CODE);
                    }



                } else if (options[item].equals("Gallery")) {


                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_REQUEST_CODE_IMAGE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);

        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        if (searchItem != null) {
            prepareSearchView(searchItem);
        }

        //clear chat
        MenuItem itemClearChat = menu.findItem(R.id.action_clear_chat);
        searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();

        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            /* (non-Javadoc)
             * @see android.support.v4.view.MenuItemCompat.OnActionExpandListener#onMenuItemActionExpand(android.view.MenuItem)
             */
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                templist.clear();
                templist.addAll(populateChat);
                footerLayout.setVisibility(View.GONE);
                Log.e("", "Openeed: ");

                return true; // Return true to expand action view

            }

            /* (non-Javadoc)
             * @see android.support.v4.view.MenuItemCompat.OnActionExpandListener#onMenuItemActionCollapse(android.view.MenuItem)
             */
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus();
                searchView.setQuery("", false);
                footerLayout.setVisibility(View.VISIBLE);
                populateChat.clear();
                templist = db.getChat(ownjid, contactJid, limiter);
                populateChat.addAll(templist);
                appendImageContent();
                chatListAdapter.notifyDataSetChanged();//notify adapter
                Log.e("", "Closed: ");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(sendMessagebutton, InputMethodManager.SHOW_IMPLICIT);

                return true; // Return true to collapse action view

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return false;
            case R.id.action_clear_chat:
                Showdialogclearchat();
                return true;

            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        // searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) ChatWindow.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ChatWindow.this.getComponentName()));
        android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {

                   getFilter(newText);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                 getFilter(query);

                return false;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }
    public void getFilter(String str) {

        populateChat.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            populateChat.addAll(templist);

        } else {
            for (ChatBody chatBody : templist) {
                if (chatBody.getMessageType().equals(PreferenceConstant.TEXT) && chatBody.getMessage().toLowerCase().contains(txt)) {
                    populateChat.add(chatBody);

                }
            }
        }
        chatListAdapter = new ChatListAdapter(populateChat);
        chatlist.setAdapter(chatListAdapter);
        //chatListAdapter.notifyDataSetChanged();//notify adapter

    }
    public void Showdialogclearchat() {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        saveDialog.setTitle(getString(R.string.alert));
        saveDialog.setMessage(getString(R.string.clear_conversation));
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                db.deleteAllChat(ownjid, contactJid, PreferenceConstant.SINGLE_USER_CHAT);
                populateChat.clear();
                chatListAdapter.notifyDataSetChanged();

                dialog.cancel();

            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

}

