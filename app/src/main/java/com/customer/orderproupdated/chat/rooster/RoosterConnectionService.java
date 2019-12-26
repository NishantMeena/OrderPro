package com.customer.orderproupdated.chat.rooster;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.utility.DialogUtility;


/**
 * Created by gakwaya on 4/28/2016.
 */
public class RoosterConnectionService extends Service {


    public static final String UI_AUTHENTICATED = "com.rooster.uiauthenticated_opc";
    public static final String SEND_MESSAGE = "com.rooster.sendmessage_opc";
    public static final String FILE_DOWNLOAD_RECEIVER = "com.filedownloaded_opc";
    public static final String BUNDLE_MESSAGE_BODY = "b_body_opc";
    public static final String BUNDLE_MESSAGE_ID = "messageID_opc";
    public static final String BUNDLE_TO = "b_to_opc";


    public static final String NEW_MESSAGE = "com.rooster.newmessage_opc";
    public static final String BUNDLE_FROM_JID = "b_from_opc";
    public static final String NEW_GROUP_MESSAGE = "com.rooster.newgroupmessage_opc";
    public static final String NEW_SMS = "com.rooster.newsms_opc";

    public static final String UPDATE_LIST = "com.rooster.updateList_opc";
    public static final String UPDATE_CONTACT_LIST = "com.rooster.updateContactList_opc";
    public static final String UPDATE_CALLS_LIST = "com.rooster.updateCallsList_opc";

    public static final String UPDATE_CHAT = "com.rooster.updatechat_opc";
    public static final String MESSAGE_ID = "com.rooster.messageid_opc";


    public static final String RECIVED_SMS = "com.sms.receive_opc";
    public static final String GROUP_UPDATE = "com.group.update_opc";
    public static final String BROADCAST_UPDATE = "com.brodcast.update_opc";

    public static final String CALL_BROADCAST = "com.group.call_broad_opc";
    public static final String PRESENCE_CHANGE = "com.presence.change_opc";

    public static final String UPDATE_FILE = "com.rooster.updatefile_opc";
    public static final String FILE_STATUS = "com.rooster.filestatus_opc";

    public static final String UPDATE_XEP = "com.rooster.updatexep_opc";
    public static final String XEP_STATUS = "com.rooster.statusxep_opc";

    public static com.customer.orderproupdated.chat.rooster.XmppConnect.ConnectionState sConnectionState;
    public static com.customer.orderproupdated.chat.rooster.XmppConnect.LoggedInState sLoggedInState;
    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;//We use this handler to post messages to
    //the background thread.
    private com.customer.orderproupdated.chat.rooster.XmppConnect mConnection;

    private SharedPrefrence share;

    public RoosterConnectionService() {

    }

    public static com.customer.orderproupdated.chat.rooster.XmppConnect.ConnectionState getState() {
        if (sConnectionState == null) {
            return com.customer.orderproupdated.chat.rooster.XmppConnect.ConnectionState.DISCONNECTED;
        }
        return sConnectionState;
    }

    public static com.customer.orderproupdated.chat.rooster.XmppConnect.LoggedInState getLoggedInState() {
        if (sLoggedInState == null) {
            return com.customer.orderproupdated.chat.rooster.XmppConnect.LoggedInState.LOGGED_OUT;
        }
        return sLoggedInState;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initConnection() {
        DialogUtility.showLOG("<-- initConnection()");
        try {

            share = SharedPrefrence.getInstance(this);


            if (Utility.checkInternetConn(this))
            {
                mConnection = com.customer.orderproupdated.chat.rooster.XmppConnect.getInstance();
                mConnection.initObject(getApplicationContext());
                mConnection.connect();
                DialogUtility.showLOG("<--connected user");
            }
        } catch (Exception e) {
            DialogUtility.showLOG("<-- Something went wrong while connecting ,make sure the credentials are right and try again");
            e.printStackTrace();
            stopSelf();
        }

    }


    public void start() {
        DialogUtility.showLOG(" Service Start() function called.");
        if (!mActive) {
            mActive = true;
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Looper.prepare();
                        mTHandler = new Handler();
                        initConnection();
                        //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                        Looper.loop();

                    }
                });
                mThread.start();
            }


        }

    }

    public void stop() {
        try {
            DialogUtility.showLOG("<--stop()");
            mActive = false;
            mTHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mConnection != null) {
                        mConnection.disconnect();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DialogUtility.showLOG("<-- onStartCommand()");
        start();
        return Service.START_STICKY;
        //RETURNING START_STICKY CAUSES OUR CODE TO STICK AROUND WHEN THE APP ACTIVITY HAS DIED.
    }

    @Override
    public void onDestroy() {
        DialogUtility.showLOG("<-- onDestroy()");
        super.onDestroy();
        stop();
    }
}
