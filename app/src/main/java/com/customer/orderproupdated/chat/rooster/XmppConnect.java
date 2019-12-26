package com.customer.orderproupdated.chat.rooster;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.HomeActivity;
import com.customer.orderproupdated.UI.SplashActivity;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.bean.FriendPresence;
import com.customer.orderproupdated.chat.bean.XEP;
import com.customer.orderproupdated.database.DatabaseHandler;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ChatMessageListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;


//import android.support.v7.app.NotificationCompat;
public class XmppConnect implements ConnectionListener {
    HashMap<String, String> readAllForXEP = new HashMap<String, String>();
    public Handler handler = null;
    android.os.Handler mhandler = new android.os.Handler();
    int MIN_WAIT_DURATION = 3000;
    boolean isNotificationHandlerWorking = false;
    private static final String TAG = "XmppConnect";
    int totalUnreadCount = 0, totalUnreadConversationCount = 0;
    String msg;
    int NOTIFICATION_ID = 15231;
    public static String lastSentMessagePacketID = "";
    int indexing = 0;
    public Context mApplicationContext;
    private String mUsername;
    private String mPassword;
    private String mServiceName;
    public static XMPPTCPConnection mConnection;
    SharedPrefrence share;
    DatabaseHandler db;
    private BroadcastReceiver uiThreadMessageReceiver;//Receives messages from the ui thread.

    private static XmppConnect xmppConnect = null;

    public static enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED;
    }

    public static enum LoggedInState {
        LOGGED_IN, LOGGED_OUT;
    }

    private XmppConnect() {
    }

    public static XmppConnect getInstance() {
        if (xmppConnect == null) {
            xmppConnect = new XmppConnect();
        }
        return xmppConnect;
    }


    public void initObject(Context context) {
        try {

            mApplicationContext = context.getApplicationContext();
            share = SharedPrefrence.getInstance(context);
            db = new DatabaseHandler(context);
            String jid = share.getValue(SharedPrefrence.JID);
            mPassword = share.getValue(SharedPrefrence.XMPPPASSWORD);


            if (jid != null && !jid.equalsIgnoreCase("")) {
                if (jid.trim().length() > 0 && jid.contains("@"))
                    mUsername = jid.split("@")[0];
                mServiceName = jid.split("@")[1];
            } else {
                mUsername = "";
                mServiceName = "";
            }
            DialogUtility.showLOG("<-- XmppConnect initObject success.");

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtility.showLOG("<-- XmppConnect initObject fail.");
        }
    }

    public void connect() throws IOException, XMPPException, SmackException {

//        XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder builder=
//                XMPPTCPConnectionConfiguration.builder();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SmackConfiguration.DEBUG_ENABLED = true;
            XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setUsernameAndPassword(mUsername, mPassword);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);


         /*   configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
            configBuilder.setSocketFactory(SSLSocketFactory.getDefault());


            SSLContext sslContext = null;

            try {
                sslContext = createSSLContext(mApplicationContext);
                configBuilder.setCustomSSLContext(sslContext);
            }catch (Exception e)
            {
                e.printStackTrace();
            }*/

            configBuilder.setResource("Shield");
            configBuilder.setServiceName(PreferenceConstant.XMPPSERVER);
            configBuilder.setHost(PreferenceConstant.XMPP_HOST);
            configBuilder.setPort(PreferenceConstant.XMPP_PORT);
            configBuilder.setSendPresence(true);
            configBuilder.setDebuggerEnabled(true);

            ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
            ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, new DeliveryReceiptRequest().getNamespace(), new DeliveryReceiptRequest.Provider());

            //Set up the ui thread broadcast message receiver.
            setupUiThreadBroadCastMessageReceiver();

            mConnection = new XMPPTCPConnection(configBuilder.build());
            mConnection.addConnectionListener(this);
            mConnection.setUseStreamManagement(true);

            if (mConnection.isConnected())
                mConnection.disconnect();

            mConnection.connect();
            setmConnection(mConnection);
            DialogUtility.showLOG("<-- XmppConnect connect now.");
            ReconnectionManager.getInstanceFor(mConnection).enableAutomaticReconnection();
            mConnection.login();
            DialogUtility.showLOG("<-- XmppConnect connect login success.");

//            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
//            reconnectionManager.setEnabledPerDefault(true);
//            reconnectionManager.enableAutomaticReconnection();

            DeliveryReceiptManager.getInstanceFor(mConnection).enableAutoReceipts();


            DeliveryReceiptManager.getInstanceFor(mConnection).addReceiptReceivedListener(new ReceiptReceivedListener() {
                @Override
                public void onReceiptReceived(String fromJid, String toJid, String receiptId, Packet receipt) {

                    Log.d(TAG, "onReceiptReceived: from: " + fromJid + " to: " + toJid + " deliveryReceiptId: " + receiptId);

                }

            });


            mConnection.getRoster().addRosterListener(new RosterListener() {



                @Override
                public void entriesUpdated(Collection<String> arg0) {
                }

                @Override
                public void entriesDeleted(Collection<String> arg0) {
                }

                @Override
                public void presenceChanged(Presence presence) {

                }

                @Override
                public void entriesAdded(Collection<String> entries) {




                }
            });

//            OFFLINE<MESSAGING
            share.setValue(SharedPrefrence.OFFLINE_MSG_SENDING, PreferenceConstant.NOT_WORKING);
            /* Send Offline Messages */
            DialogUtility.showLOG("<-- XmppConnect connect send Offline Messages.");
            if (mConnection.isConnected()) {
                sendOfflineMessages();
            }

            DialogUtility.showLOG("<-- XmppConnect connect complete.");
        } catch (Exception e) {
            e.printStackTrace();

            DialogUtility.showLOG("<-- XmppConnect error at login." + e);

        }
    }





    public void sendOfflineMessages() {

        String isOfflineWorking = share.getValue(SharedPrefrence.OFFLINE_MSG_SENDING);
        if (isOfflineWorking.equals(PreferenceConstant.NOT_WORKING)) {

            try {
                share.setValue(SharedPrefrence.OFFLINE_MSG_SENDING, PreferenceConstant.WORKING);


                ArrayList<ChatBody> chatBodies = db.getOfflineChats();

                for (int i = 0; i < chatBodies.size(); i++) {

                    try {
                        ChatBody filesInfo = chatBodies.get(i);
                        ChatBody temp = db.getFileInfo(filesInfo.getMessageID());
                        filesInfo.setUrl(temp.getUrl());
                        filesInfo.setThumb(temp.getThumb());
                        filesInfo.setLocalUri(temp.getLocalUri());
                        chatBodies.set(i, filesInfo);
                    } catch (Exception e) {
                    }

                    sendMessage(chatBodies.get(i));
                }
                // SEND OFFLINE GROUP NOTIFY

                // SEND OFFLINE GROUP NOTIFY
                if (chatBodies.size() == 0) {
                    share.setValue(SharedPrefrence.OFFLINE_MSG_SENDING, PreferenceConstant.NOT_WORKING);
                }
            } catch (Exception e) {
                share.setValue(SharedPrefrence.OFFLINE_MSG_SENDING, PreferenceConstant.NOT_WORKING);
            }
        }
    }


    public void performMessageLossOperation(Message message) {

        if ((message.getFrom().contains("admin" + PreferenceConstant.SERVERATTHERSTE)) &&
                ((message.toXML().toString().contains("<server_receipt xmlns='server_receipt'></server_receipt></message>")) ||
                        (message.toXML().toString().contains("</server_receipt><delay xmlns='urn:xmpp:delay'")))) {
            // Update Client that, message is succesfully
            changeMessageDeliveryStatus(message);
            return;

        } else {
            if (!(message.toXML().toString().contains("</subject>"))) {
                return;

            } else {
                /***
                 * SEND RECEIPT TO SERVER *
                 * // Update SERVER that, message is successfully received by client
                 * message received by client
                 */
                try {
                    CustomPacket mypacket = new CustomPacket("message", "jabber:client");

                    mypacket.setTo(message.getFrom());
                    mypacket.setFrom(message.getTo());
                    mypacket.addAttribute("id", message.getPacketID());
                    mypacket.addAttribute("type", (Message.Type.chat).toString());
                    mConnection.sendPacket(mypacket);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            /*
            * Client Server Receipt End
			*/
            }
        }

    }

    public void changeMessageDeliveryStatus(Message message) {
        db.updateStatus(message);
        notifyChatWindows(message);

        if ((message.getFrom().contains("admin" + PreferenceConstant.SERVERATTHERSTE))) {

            if (readAllForXEP.containsKey(message.getPacketID())) {

                String friendJid = readAllForXEP.get(message.getPacketID());
                db.deleteXEPRowForJID(friendJid);
                readAllForXEP.remove(message.getPacketID());


            } else {

                db.deleteXEPRow(message.getPacketID());
            }
        }
    }


    public void notifyChatWindows(Message message) {
        Intent intentOne = new Intent(RoosterConnectionService.UPDATE_CHAT);
        intentOne.setPackage(mApplicationContext.getPackageName());
        intentOne.putExtra(RoosterConnectionService.MESSAGE_ID, message.getPacketID());
        mApplicationContext.sendBroadcast(intentOne);
    }


    public void receiveMessage(Chat chat, Message message) {
        try {


            if (message.getSubject().equals(PreferenceConstant.TEXT)
                    || message.getSubject().equals(PreferenceConstant.IMAGE)
                    || message.getSubject().equals(PreferenceConstant.VIDEO)
                    || message.getSubject().equals(PreferenceConstant.AUDIO)
                    || message.getSubject().equals(PreferenceConstant.CONTACT)
                    || message.getSubject().equals(PreferenceConstant.LOCATION)
                    || message.getSubject().equals(PreferenceConstant.STICKER)) {

                receiveSingleTxtMsg(message);

            } else if (message.getSubject().equals(PreferenceConstant.BROADCAST_ADMIN)) {
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }






    public void receiveSingleTxtMsg(Message message) {

        String ownjid = share.getValue(SharedPrefrence.JID);

        String from = message.getFrom();
        String rcvMessage = "";
        String msgKey = null;
        String contactJid = "";
        if (from.contains("/")) {
            contactJid = from.split("/")[0];
            DialogUtility.showLOG("The real jid is :" + contactJid);
        } else {
            contactJid = from;
        }

        int priorityType = PreferenceConstant.PRIORITY_NORMAL;

        String msg = message.getBody();

        try {
            msg = Utility.utf8UrlDecoding(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            msg = msg;

        }


        String type = message.getSubject();
        String msgType = "";
        if (type.equals(PreferenceConstant.IMAGE) ) {
            msgType = "Image";
        } else if (type.equals(PreferenceConstant.VIDEO) ) {
            msgType = "Video";
        } else if (type.equals(PreferenceConstant.CONTACT)) {
            msgType = msg;
        } else if (type.equals(PreferenceConstant.LOCATION)) {
            msgType = msg;
        } else if (type.equals(PreferenceConstant.AUDIO)) {
            msgType = "Audio";
        } else if (type.equals(PreferenceConstant.STICKER)) {
            msgType = msg;
        }else if (type.equals(PreferenceConstant.DOCUMENT)) {
            msgType = "Doc";
        } else if (type.equals(PreferenceConstant.TEXT)) {
            msgType = msg;
        } else {
            msgType = msg;
        }

        boolean isEvap = false;

        ChatBody chatBody = null;

        chatBody = Utility.getChatBodyForChatType(msgType, ownjid, contactJid,
                message.getSubject(), PreferenceConstant.IN_BOUND, PreferenceConstant.UNREAD, message, PreferenceConstant.MESSAGE_TYPE_CHAT, priorityType);


        db.addChatMessageToDB(chatBody);

        if (!isEvap) {


            switch (type) {

                case PreferenceConstant.IMAGE:
                    chatBody.setUrl(Utility.getStringFromIndex(msg, 0));
                    chatBody.setThumb(Utility.getStringFromIndex(msg, 1));
                    chatBody.setMessage(msgType);
                    db.addFilesToDB(chatBody);

                    break;
                case PreferenceConstant.VIDEO:

                    chatBody.setUrl(Utility.getStringFromIndex(msg, 0));
                    chatBody.setThumb(Utility.getStringFromIndex(msg, 1));
                    chatBody.setMessage(msgType);
                    db.addFilesToDB(chatBody);

                    break;
                case PreferenceConstant.AUDIO:

                    chatBody.setUrl(Utility.getStringFromIndex(msg, 0));
                    chatBody.setAudio_title(Utility.getStringFromIndex(msg, 1));
                    chatBody.setMessage(msgType);
                    db.addFilesToDB(chatBody);

                    break;
                case PreferenceConstant.DOCUMENT:

                    chatBody.setUrl(Utility.getStringFromIndex(msg, 0));
                    chatBody.setExtension(Utility.getStringFromIndex(msg, 1));
                    chatBody.setMessage(msgType);
                    db.addFilesToDB(chatBody);

                    break;
                case PreferenceConstant.CONTACT:

                    chatBody.setcName(Utility.getStringFromIndex(msg, 1));
                    chatBody.setcNumber(Long.parseLong(Utility.getStringFromIndex(msg, 2)));

                    break;
                case PreferenceConstant.LOCATION:
                    chatBody.setThumb(Utility.getStringFromIndex(msg, 1));
                    chatBody.setAddress(Utility.getStringFromIndex(msg, 2));
                    chatBody.setLatitude(Utility.getStringFromIndex(msg, 3));
                    chatBody.setLongitude(Utility.getStringFromIndex(msg, 4));
                    break;
            }


        }

        if (chatBody.getFriendJID().equals(PreferenceConstant.RESTRICT_NOTIFICATION)) {
            db.addEntryAndSendReadXEP(chatBody);
        } else {
            db.addEntryForXEP(chatBody);
        }

        Intent intent = new Intent(RoosterConnectionService.NEW_MESSAGE);
        intent.setPackage(mApplicationContext.getPackageName());
        intent.putExtra("CHAT", chatBody);
        intent.putExtra(RoosterConnectionService.BUNDLE_FROM_JID, contactJid);
        intent.putExtra(RoosterConnectionService.BUNDLE_MESSAGE_BODY, msg);
        intent.putExtra(RoosterConnectionService.BUNDLE_MESSAGE_ID, message.getPacketID());
        mApplicationContext.sendBroadcast(intent);

          if (Utility.checkItFromSetting(share, SharedPrefrence.SHOW_CHAT_NOTIFICATION)) {
                addNotification(chatBody);
        }
      //  addNotification(chatBody);


    }



    private void setupUiThreadBroadCastMessageReceiver() {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                //Check if the Intents purpose is to send the message.
                String action = intent.getAction();
                if (action.equals(RoosterConnectionService.SEND_MESSAGE)) {
                    //Send the message.
                    ChatBody chatBody = (ChatBody) intent.getSerializableExtra("CHAT");
                    sendMessage(chatBody);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(RoosterConnectionService.SEND_MESSAGE);
        mApplicationContext.registerReceiver(uiThreadMessageReceiver, filter);
    }

    private void sendMessage(ChatBody chatBody) {

        try {
            Chat chat = ChatManager.getInstanceFor(mConnection).createChat(chatBody.getFriendJID(), chatBody.getFriendJID() + PreferenceConstant.SEPERATOR + System.currentTimeMillis(), new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    DialogUtility.showLOG("message.getFrom() :" + message.getFrom());
                }
            });

            String type = chatBody.getMessageType();
            Message msg = new Message(chatBody.getFriendJID(), Message.Type.chat);
            msg.setSubject(chatBody.getMessageType());
            msg.setBody(Utility.utf8UrlEncoding(formatMsg(chatBody)));
            msg.setPacketID(chatBody.getMessageID());


            if (!lastSentMessagePacketID.equals(msg.getPacketID())) {
                chat.sendMessage(msg);
                lastSentMessagePacketID = msg.getPacketID();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String formatMsg(ChatBody chat) {
        String msgBody = "";

        /*
        * SINGLE
        * */
        switch (chat.getMessageType()) {

            case PreferenceConstant.TEXT:
                msgBody = Utility.MessageTransfer(chat);

                break;

            case PreferenceConstant.IMAGE:
                msgBody = Utility.fileTransferSingleImageVideo(chat);

                break;

            case PreferenceConstant.VIDEO:

                msgBody = Utility.fileTransferSingleImageVideo(chat);

                break;
            case PreferenceConstant.AUDIO:

                msgBody = Utility.fileTransferSingleAudio(chat);

                break;

            case PreferenceConstant.STICKER:
                msgBody = Utility.MessageTransfer(chat);
                break;
            case PreferenceConstant.CONTACT:
                msgBody = Utility.MessageTransfer(chat);
                break;
            case PreferenceConstant.LOCATION:
                msgBody = Utility.MessageTransfer(chat);
                break;

            case PreferenceConstant.DOCUMENT:

                msgBody = Utility.fileTransferSingldoc(chat);

                break;


        }


        return msgBody;
    }


    public void disconnect() {
        DialogUtility.showLOG("<-- XMPP Disconnecting from serser " + mServiceName);

        Intent intent = new Intent(RoosterConnectionService.PRESENCE_CHANGE);
        mApplicationContext.sendBroadcast(intent);


        try {
            if (mConnection != null) {
                mConnection.disconnect();
            }

        } catch (SmackException.NotConnectedException e) {
            RoosterConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
            e.printStackTrace();

        }

        if (mConnection != null) {

            if (Utility.checkInternetConn(mApplicationContext)) {

                DialogUtility.showLOG("<-- XMPP Reconnecting from serser " + mServiceName);
//                reconnectingIn(60);
                ReconnectionManager.getInstanceFor(mConnection).enableAutomaticReconnection();

            } else {
                mConnection = null;

                try {
                    // Unregister the message broadcast receiver.
                    if (uiThreadMessageReceiver != null) {
                        mApplicationContext.unregisterReceiver(uiThreadMessageReceiver);
                        uiThreadMessageReceiver = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public FriendPresence getOfflineFriendPresence() {
        FriendPresence friendPresence = new FriendPresence();
        friendPresence.setType("offline");
        friendPresence.setMode("offline");
        return friendPresence;
    }


    @Override
    public void connected(XMPPConnection connection) {
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTED;
        DialogUtility.showLOG("<-- Connected Successfully");

        sendOfflineMessages();
    }

    @Override
    public void authenticated(XMPPConnection connection) {
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTED;
        DialogUtility.showLOG("<-- Authenticated Successfully");
        showContactListActivityWhenAuthenticated();


        ChatManager.getInstanceFor(mConnection).addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {

                        message.setFrom(Utility.getBareJID(message.getFrom()));

                        performMessageLossOperation(message);

                        if (message.getBody() != null) {

                            boolean isMessageExit = db.isMessagePresentInDB(message.getPacketID());

                            if (!isMessageExit) {

                                DialogUtility.showLOG("<-- message receive :" + message.toXML().toString());
                                if (message.getSubject().equals(PreferenceConstant.XEP_ALL_READ_RECEIPT))
                                {
                                    XEP xep = new XEP();
                                    String friendJid = "";

                                    try {
                                        friendJid = message.getFrom().replace("/Shield", "");
                                    } catch (Exception e) {
                                    }
                                    xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                                    xep.setFriendJID(friendJid);
                                    xep.setStatus(PreferenceConstant.READ);
                                    db.updateAllXEPStatusInMsgTables(xep);

                                }else
                                {
                                    receiveMessage(chat, message);

                                }
                            } else {

                                try {
                                    String friendJid = "";

                                    try {
                                        friendJid = message.getFrom().replace("/Shield", "");
                                    } catch (Exception e) {
                                    }
                                    XEP xep = new XEP();
                                    switch (message.getSubject()) {
                                        case PreferenceConstant.XEP_DELIVERED_RECEIPT:
                                            xep.setSubject(PreferenceConstant.XEP_DELIVERED_RECEIPT);
                                            xep.setFriendJID(friendJid);
                                            xep.setMessageId(message.getPacketID());
                                            xep.setStatus(PreferenceConstant.DELIVERED);
                                            db.updateXEPStatusInMsgTables(xep);

                                            break;

                                        case PreferenceConstant.XEP_READ_RECEIPT:
                                            xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                                            xep.setFriendJID(friendJid);
                                            xep.setMessageId(message.getPacketID());
                                            xep.setStatus(PreferenceConstant.READ);
                                            db.updateXEPStatusInMsgTables(xep);
                                            break;

                                        case PreferenceConstant.XEP_ALL_READ_RECEIPT:
                                            xep.setSubject(PreferenceConstant.XEP_READ_RECEIPT);
                                            xep.setFriendJID(friendJid);
                                            xep.setStatus(PreferenceConstant.READ);
                                            db.updateAllXEPStatusInMsgTables(xep);
                                            break;

                                    }


                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                });
            }
        });

        share.setValue(SharedPrefrence.OFFLINE_MSG_SENDING, PreferenceConstant.NOT_WORKING);

        sendOfflineMessages();

        db.sendPendingXEP();
    }

    @Override
    public void connectionClosed() {
        RoosterConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        RoosterConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
        DialogUtility.showLOG("<--ConnectionClosedOnError, error " + e.toString());
    }

    @Override
    public void reconnectingIn(int seconds) {

//        seconds = 60;
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTING;
        DialogUtility.showLOG("<--ReconnectingIn() ");
        if (Utility.checkInternetConn(mApplicationContext)) {

            try {
                if (mConnection == null) {
                    connect();
                } else {
                    mConnection.connect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reconnectionSuccessful() {
        RoosterConnectionService.sConnectionState = ConnectionState.CONNECTED;

    }

    @Override
    public void reconnectionFailed(Exception e) {
        RoosterConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
        DialogUtility.showLOG("<--ReconnectionFailed()");
    }

    private void showContactListActivityWhenAuthenticated() {
        Intent i = new Intent(RoosterConnectionService.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
        DialogUtility.showLOG("<--Sent the broadcast that we are authenticated");
    }

    public XMPPTCPConnection getmConnection() {
        return mConnection;
    }

    public void setmConnection(XMPPTCPConnection mConnection) {
        this.mConnection = mConnection;
    }

    public void connectDirect() {
        try {
            if (Utility.checkInternetConn(mApplicationContext)) {
                mConnection.connect();

            }

        } catch (Exception e) {
            DialogUtility.showLOG("Trying to Connect but got error");
        }

    }

    public void addNotification(ChatBody chatBody) {

        if (chatBody.getFriendJID().equals(PreferenceConstant.RESTRICT_NOTIFICATION)) {
            return;
        }

        String totalUnread = db.getTotalUnread() + "";
        String totalUnreadConversation = db.getOpenConversation_ofUnread();

        totalUnreadCount = 0;
        totalUnreadConversationCount = 0;

        /* Custom Notification */

        if (totalUnread != null) {
            totalUnreadCount = Integer.parseInt(totalUnread);
        }
        if (totalUnreadConversation != null) {
            totalUnreadConversationCount = Integer.parseInt(totalUnreadConversation);
        }

        if (totalUnreadConversationCount == 1) {
            CustomNotificationOne(chatBody);
        } else {
            CustomNotificationThree(chatBody);
        }


        /* Beep Notification */
        try {
            int res_sound_id = 0;
            Uri notification = null;

            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Ringtone r = RingtoneManager.getRingtone(mApplicationContext, notification);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Custom Notification */
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void CustomNotificationOne(ChatBody chatBody) {

        String jid = "";

        msg = totalUnreadCount + " messages from this chats";

        Bitmap icon1 = BitmapFactory.decodeResource(mApplicationContext.getResources(), R.mipmap.ic_launcher);

        String message = chatBody.getMessage();

        try {
            message = URLDecoder.decode(message, "UTF-8");

            if (message.contains("Contact" + PreferenceConstant.SEPERATOR)) {
                message = "Contact";
            } else if (message.contains("Location" + PreferenceConstant.SEPERATOR)) {
                message = "Location";
            } else if (message.contains("Sticker" + PreferenceConstant.SEPERATOR)) {
                message = "Sticker";
            }
        } catch (Exception e) {
        }


        String getNameOrNumber = "";
        boolean isMessageBelongsToChat = Utility.isMessageBelongsToChat(chatBody.getFriendJID());

        if (isMessageBelongsToChat) {

        } else {
            getNameOrNumber = chatBody.getContactName();
        }

        String msgType = chatBody.getMessageType();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mApplicationContext).setAutoCancel(true)
                .setContentTitle("Chat from " + getNameOrNumber)
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(icon1)
                .setContentText(msg);


        //LED
        mBuilder.setLights(Color.MAGENTA, 3000, 3000);
        AudioManager audiomanager = (AudioManager) mApplicationContext.getSystemService(Context.AUDIO_SERVICE);
        audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        /*if (Utility.checkItFromSetting(share, SharedPrefrence.SHOW_MESSAGE_PREVIEW)) {
            bigText.bigText(message);
        }*/

        bigText.bigText(message);


//      bigText.bigText(message);
        bigText.setBigContentTitle(getNameOrNumber);
        bigText.setSummaryText(msg);
        mBuilder.setStyle(bigText);

        // Creates an explicit intent for an Activity in your app

        Intent resultIntent = null;
        if (isMessageBelongsToChat) {

            resultIntent = new Intent(mApplicationContext, SplashActivity.class);
            jid = chatBody.getFriendJID();
        }
        resultIntent.putExtra(SharedPrefrence.JID, jid);
        resultIntent.putExtra("mobile", Utility.getNumberFromJID(jid));
        resultIntent.putExtra("user_name", getNameOrNumber);
        resultIntent.putExtra("fromnotification", "true");

//        Intent resultIntent = new Intent(mApplicationContext, ChatWindow.class);

        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mApplicationContext);

        // Adds the back stack for the Intent (but not the Intent itself)
        if (isMessageBelongsToChat) {
            stackBuilder.addParentStack(SplashActivity.class);
        }
//        stackBuilder.addParentStack(ChatWindow.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private NotificationCompat.Builder buildNotificationConfig(NotificationCompat.Builder builder, SharedPrefrence share) {

        //LED
        builder.setLights(Color.MAGENTA, 3000, 3000);

        AudioManager audiomanager = (AudioManager) mApplicationContext.getSystemService(Context.AUDIO_SERVICE);


        switch (audiomanager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:

                audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                break;
            case AudioManager.RINGER_MODE_SILENT:

                audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                break;
            case AudioManager.RINGER_MODE_VIBRATE:

                audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                break;
        }


        //Vibration
        if (Utility.checkItFromSetting(share, SharedPrefrence.IN_APP_VIBRATE)) {
            builder.setVibrate(new long[]{100, 100, 100, 100, 100});
        }

        return builder;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void CustomNotificationThree(ChatBody chatBody) {

        msg = totalUnreadCount + " messages from " + totalUnreadConversationCount + " chats";

        ArrayList<ChatBody> chats = db.getOpenConversationsForNotification();

        String[] events = new String[chats.size()];
        for (int i = 0; i < chats.size(); i++) {

            ChatBody chat = chats.get(i);

            String message = chat.getMessage();
            try {
                message = URLDecoder.decode(message, "UTF-8");

                if (message.contains("Contact" + PreferenceConstant.SEPERATOR)) {
                    message = "Contact";
                } else if (message.contains("Location" + PreferenceConstant.SEPERATOR)) {
                    message = "Location";
                } else if (message.contains("Sticker" + PreferenceConstant.SEPERATOR)) {
                    message = "Sticker";
                }
            } catch (Exception e) {
            }

        }

        try {

            Bitmap icon1 = BitmapFactory.decodeResource(mApplicationContext.getResources(), R.mipmap.ic_launcher);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    mApplicationContext).setAutoCancel(true)
                    .setContentTitle("Chat Messages")
                    .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(icon1)
                    .setContentText(msg);
            mBuilder.setLights(Color.MAGENTA, 3000, 3000);
            AudioManager audiomanager = (AudioManager) mApplicationContext.getSystemService(Context.AUDIO_SERVICE);
            audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            // Sets a title for the Inbox style big view
            inboxStyle.setBigContentTitle("Chat Conversations");

/*
            if (Utility.checkItFromSetting(share, SharedPrefrence.SHOW_MESSAGE_PREVIEW)) {
                // Moves events into the big view
                for (int i = 0; i < events.length; i++) {

                    inboxStyle.addLine(events[i]);
                }
            }
*/

            for (int i = 0; i < events.length; i++) {

                inboxStyle.addLine(events[i]);
            }

            inboxStyle.setSummaryText(msg);

            // Moves the big view style object into the notification object.
            mBuilder.setStyle(inboxStyle);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(mApplicationContext, HomeActivity.class);

            // The stack builder object will contain an artificial back stack for
            // the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mApplicationContext);

            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(HomeActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changePresence(Presence presence) {
        String fromJID = Utility.getBareJID(presence.getFrom());

        if (!fromJID.equals(share.getValue(SharedPrefrence.JID))) {
            if (presence != null) {
                FriendPresence friendPresence = new FriendPresence();
                friendPresence.setFriendJID(fromJID);
                Presence.Type type = presence.getType();
                if (type == Presence.Type.available) {
                    friendPresence.setType("available");
                    Presence.Mode mode = presence.getMode();
                    if (mode == Presence.Mode.away) {
                        friendPresence.setMode("away");
                    } else if (mode == Presence.Mode.xa) {
                        friendPresence.setMode("xa");
                    } else if (mode == Presence.Mode.dnd) {
                        friendPresence.setMode("dnd");
                    } else if (mode == Presence.Mode.chat) {
                        friendPresence.setMode("chat");
                    } else {
                        friendPresence.setMode("online");
                    }
                } else if (type == Presence.Type.error) {
                    friendPresence.setType("offline");
                    friendPresence.setMode("offline");
                } else {
                    friendPresence.setType("offline");
                    friendPresence.setMode("offline");
                }
                //  db.updatePresence(friendPresence);
                //Bundle up the intent and send the broadcast.
                Intent intent = new Intent(RoosterConnectionService.PRESENCE_CHANGE);
                mApplicationContext.sendBroadcast(intent);

              /*  HashMap<String, ProfileVCard> profileCard = share.getprofileVcardList(SharedPrefrence.VCARD_LIST);
                ProfileVCard vCard = profileCard.get(fromJID);

                try {
                    if (vCard.getProfileImage().equals("")) {
                        new Utility.loadvCard(mApplicationContext, fromJID, profileCard, vCard).execute();
                    }
                }catch (Exception e)
                {

                }*/

            }

        }
    }

	/*
     * Make Custom Packet For Client Receipt
	 */

    static class CustomPacket extends Packet {
        private String name;
        private String namespace;
        Map<String, String> attributes;

        CustomPacket(String name, String namespace) {
            this.name = name;
            this.namespace = namespace;
            attributes = Collections.emptyMap();
        }

        public void addAttribute(String name, String value) {
            if (attributes == Collections.EMPTY_MAP)
                attributes = new HashMap<String, String>();
            attributes.put(name, value);
        }

        public String getAttribute(String name) {
            return attributes.get(name);
        }

        public String getNamespace() {
            return namespace;
        }

        public String getElementName() {
            return name;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName());

            // TODO Xmlns??
            if (getNamespace() != null) {
                buf.append(" xmlns=\"").append(getNamespace()).append("\"");
            }
            for (String key : attributes.keySet()) {
                buf.append(" ").append(key).append("=\"").append(StringUtils.escapeForXML(attributes.get(key))).append("\"");
            }
            buf.append(">");

            buf.append("<client_receipt>client_receipt_atklique</client_receipt>");

            buf.append("</" + getElementName() + ">");

            return buf.toString();
        }
    }


    public void sendXEPReceipt(XEP xep) {

        try {

            Chat chat = ChatManager.getInstanceFor(mConnection).createChat(xep.getFriendJID(), xep.getFriendJID() + PreferenceConstant.SEPERATOR + System.currentTimeMillis() + (indexing++), new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    DialogUtility.showLOG("message.getFrom() :" + message.getFrom());
                }
            });

            Message msg = new Message(xep.getFriendJID(), Message.Type.chat);
            msg.setSubject(xep.getSubject());
            msg.setBody(xep.getStatus());

            String packetID = xep.getMessageId();
            if (packetID != null) {
                msg.setPacketID(packetID);
            }

            chat.sendMessage(msg);
            lastSentMessagePacketID = msg.getPacketID();

            if (xep.getSubject().equals(PreferenceConstant.XEP_ALL_READ_RECEIPT)) {
                readAllForXEP.put(msg.getPacketID(), xep.getFriendJID());
            }

            db.updateXEP(xep);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }











}
