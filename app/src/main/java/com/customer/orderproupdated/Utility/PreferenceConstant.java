package com.customer.orderproupdated.Utility;

import android.net.Uri;

/**
 * Created by Sony on 12/29/2016.
 */
public class PreferenceConstant {

    public static final String SERVER = "36.255.3.15";
    public static  boolean IS_Merchant_Switched = false;
    public static  boolean PANEL_VISIBLE = false;
    public static int CART_COUNT=0;



    public static final String BASE_URL = "https://" + PreferenceConstant.SERVER + "/order_p/API/";
   // public static final String BASE_URL = "https://36.255.3.15/order_p/API/";

    public static int PASS_STATUS=1;
    public static int FAIL_STATUS=0;

    public static Double TOTAL_PRICE=0.0;
     public static String PRODUCT_VERSION = "0";

 public static final String FILE_TRANSFER = BASE_URL+"file_transfer.php";




    // ............ for xmpp......................


   // Change by Nishant...
   // public static final String XMPPSERVER = "shieldcrypt.com";
    public static final String XMPPSERVER = "36.255.3.15";

    public static final String XMPPPUID = "919977339839@f5chat.com";
    public static final String XMPPPWD = "f5advantal";

    public static final String SERVERATTHERSTE = "@" + XMPPSERVER;

    public static final int XMPP_PORT = 5333;
    public static final String XMPP_HOST = XMPPSERVER;

    public static final String IN_BOUND = "IN";
    public static final String OUT_BOUND = "OUT";
    public static final String CHAT_PROGRESS = "normal";

    public static final String SENT = "sent";
    public static final String DELIVERED = "delivered";
    public static final String SEEN = "seen";
    public static final String UNREAD = "unread";
    public static final String READ = "read";
    public static final String SENT_LATER = "sent_later";
    public static String RESTRICT_NOTIFICATION = "";

    public static final int NOTIFY_MESSAGE = 1001;

    public static final String SINGLE_USER_CHAT = "singleUserChat";

    public static final String MESSAGE_TYPE_CHAT = "5000";
    public static final String MESSAGE_TYPE_GROUP_CHAT = "6000";
    public static final String MESSAGE_TYPE_SMS_CHAT = "7000";
    public static final String BROADCAST_NEW_MEMBER = "8000";
    public static final String BROADCAST_ADMIN= "8001";
    public static final String BROADCAST_USERGROUP = "8002";
    public static final String MESSAGE_TYPE_EXIT_GROUP = "51";

    //    Single Chat :-
    public static final String TEXT = "1000";
    public static final String IMAGE = "1001";
    public static final String VIDEO = "1002";
    public static final String AUDIO = "1003";
    public static final String LOCATION = "1004";
    public static final String SKETCH = "1005";
    //    public static final String EMOJI = "1006";
    public static final String CONTACT = "1007";
    public static final String STICKER = "1006";
    public static final String ENCRYPTED = "1008";
    public static final String DOCUMENT = "1009";


    //  XEP-0184 Implementation
    public static final String XEP_DELIVERED_RECEIPT = "20001";
    public static final String XEP_READ_RECEIPT = "20002";
    public static final String XEP_ALL_READ_RECEIPT = "20003";

    //    Profile Notification Subject:-
    public static final String DATE_SECTION = "45000";


    // Priority Messages Type
    public static final int PRIORITY_NORMAL = 30001;
    public static final int PRIORITY_MEDIUM = 30002;
    public static final int PRIORITY_HIGH = 30003;

    public static final String MSG_DELIVERED_SUCCESS = "MSG_DELIVERED_SUCCESS";
    public static final String MSG_READ = "READ";
    public static final String MSG_DELIVERED = "DELIVERED";

    public static final int IC_ATTACHMENT = 1111;
    public static final int IC_STICKERS = 1112;
    public static final int IC_RECORDER = 1113;

    // Notify to all
    public static final String NOTIFY_TO_ALL = "all@broadcast." + XMPPSERVER;
    public static final String user_jid = "user_jid";
    public static final String member_jid_without_ip = "member_jid_without_ip";

    public static final String JID = "jid";

    public static Uri uri = null;
    public static int phoneDialogCode = 9001;




    public static String TYPE = "screen_type";

    public static String MemberJidWithoutIp = "member_jid_without_ip";
    public static String MemberJID = "member_jid";
    public static String SEPERATOR = "##:<A>O<B>:##"; // *^!%#:#%!^* // "#:#"
    public static String COMMA_SEPARATOR = ",";
    public static String SMSSEPERATOR = "##:##";

    /* Directory Creation*/

    /*ROOT*/
    public static String rootDirectory = "OrderPro";

    /*SUBROOT  rootDirectory*/
    public static String subRootDBDirectory = "Databases";
    public static String subRootMediaDirectory = "Media";

    /*SUBROOT  subRootMediaDirectory */
    public static String dirVideo = "ChatVideo";
    public static String dirImages = "ChatImages";
    public static String dirAudio = "ChatAudio";

    public static String dirSent = "Sent";

    /* Files Status */
    public static String fileUploding = "uploading";
    public static String fileUploded = "uploaded";
    public static String fileDownloading = "downloading";
    public static String fileDownloaded = "downloaded";
    public static String fileFail = "fail";

    public static int INCOMING_TYPE = 1;
    public static int MISSED_TYPE = 3;
    public static int OUTGOING_TYPE = 2;
    /*working*/
    public static String IS_APP_USER = "true";
    public static String WORKING = "true";
    public static String NOT_WORKING = "false";

 public static final String EDIT_SINGLE_USER_TEXT = "1051";


}

