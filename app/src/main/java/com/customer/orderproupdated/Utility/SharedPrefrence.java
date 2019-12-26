package com.customer.orderproupdated.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.customer.orderproupdated.bean.AllMerchantDetails;
import com.customer.orderproupdated.bean.Banner;
import com.customer.orderproupdated.bean.CategoryBean;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.MerchantList_LoginResponse;
import com.customer.orderproupdated.bean.Order;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.SubCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static SharedPrefrence myObj;
    public static String IS_MERCHANT_SWITCHED = "is_merchant_switched";


    int PRIVATE_MODE = 0;


    public static final String MERCHANT_ID = "merchnat_id";

    public static final String MERCHANT_DETAIL = null;


    public static final String IS_USER_LOGIN = "isLogin";
    public static final String IS_SLIDER_UP = "isUP";

    //customer Detail
    public static final String CUSTOMERID = "customer_id";
    public static final String FIRST_NAME = "firstname";
    public static final String LAST_NAME = "lastname";
    public static final String MOB_NUMBER = "mob_number";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String PINCODE = "pincode";

    //notification settings
    public static String SHOW_CHAT_NOTIFICATION = "SHOW_CHAT_NOTIFICATION";
    public static String SHOW_PRODUCT_NOTIFICATION= "SHOW_PRODUCT_NOTIFICATION";
    public static String SHOW_OFFER_NOTIFICATION = "SHOW_OFFER_NOTIFICATION";
    public static String IN_APP_VIBRATE = "IN_APP_VIBRATE";




    public static final String PRODUCT_LIST = "productList";
    public static final String CART_LIST = "cartList";
    public static final String FAVOURITE_LIST = "favouriteList";

    public static final String All_MERCHANT_LIST = "merchantlist";
    public static final String MERCHANT_LIST_ADDED = "merchantlistadded";
    public static final String LIST_BANNER_DEFAULT = "list_banner_default";
    public static final String CATEGORY_LIST = "list_category";
    public static final String SUBCATEGORY_LIST = "list_sub_category";
    public static final String PRODUCT_ATTRIBUTE_LIST = "list_Product_attribute";
    public static final String PRODUCTVAL_LIST = "list_Product_attribute";

    public static final String QUICK_ORDER_LIST = "quick_order_list";
    public static final String ORDERLIST = "orderlist";


    /*----------------------xmpp--------------------------*/
    public static String PRIVATE_MESSAGE_LIST = "private_message_lst";

    /* RESTORE TO DEFAULT SETTING F5CHAT*/
    public static String evapEnabled = "mEvapEnableOrNot";
    public static String mMintute = "mMintute";
    public static String mSec = "mSec";

    /* RESTORE TO DEFAULT SETTING F5CHAT*/

    // Privacy
    public static String SHOW_ONLINE_STATUS = "SHOW_ONLINE_STATUS";
    public static String SHOW_SEEN_STATUS = "SHOW_SEEN_STATUS";
    public static String COLLECT_ANALYTICS_DATA = "COLLECT_ANALYTICS_DATA";
    public static String SHOW_YOUR_PROFILE_PICTURE = "SHOW_YOUR_PROFILE_PICTURE";
    public static String DEACTIVATE_ACCOUNT = "DEACTIVATE_ACCOUNT";

    /*// Notifications
    public static String SHOW_CHAT_NOTIFICATION = "SHOW_CHAT_NOTIFICATION";
    public static String SHOW_MESSAGE_PREVIEW = "SHOW_MESSAGE_PREVIEW";
    public static String SHOW_NEW_UPDATES = "SHOW_NEW_UPDATES";
    public static String SHOW_NEW_CONTACTS_JOINS = "SHOW_NEW_CONTACTS_JOINS";
    public static String IN_APP_SOUND = "IN_APP_SOUND";
    public static String IN_APP_VIBRATE = "IN_APP_VIBRATE";*/

    // Calls & Messages
    public static String CALL_WAITING = "CALL_WAITING";
    public static String FILTER_UNKNOWN_SENDER = "FILTER_UNKNOWN_SENDER";

    // Multimedia
    public static String SAVE_TO_GALLERY = "SAVE_TO_GALLERY";
    public static String AUTOMATIC_DOWNLOAD = "AUTOMATIC_DOWNLOAD";
    public static String RESTRICT_DATA_USAGE = "RESTRICT_DATA_USAGE";

    public static String MUTE_UNMUTE = "MUTE_UNMUTE";

    public static String TRUE = "true";
    public static String FALSE = "false";


    public static String outLetDetailList = "outLetDetailList";
    public static String lat = "lat";
    public static String lng = "lng";
    public static String GET_USER_DETAILS = "getUserDetails";
    public static String OTP = "getOtp";
    public static String MOBILE = "getMobile";
    public static String ORG_ID = "org_id";

    public static String DEVICETOKEN = "getDeviceToken";
    public static String CONTACT_LIST = "contactList";
    public static String SYNC_ENABLE = "syncEnable";
    public static String TRACKLOCATION = "trackLocation";

    public static String OFFLINE_MSG_SENDING = "offline_message_Sending";
    public static String OFFLINE_GROUP_NOTIFY = "offline_group_notify";

    /* ContactSync  Rajesh */
    public static String FIRST_TIME = "contact_string";
    public static String CONTACT_STRING = "contact_string";
    public static String LASTSYNCING = "last_syncing";
    public static String SYNCING_REQUEST = "syncing_request";
    public static String GROUP_SYNCING_REQUEST = "groupsyncing_request";
    public static String FETCH_USER_REQUEST = "fetchuser_request";
    public static String MAPPING_OBJ = "mapping_object";

    //public static String GROUP_LST = "groupLst";

    public static String FROM_VERIFICATION = "fromverification";
    public static String NAME = "name";
    public static String PASSPHARSE = "passpharse";
    public static final String USERID = "user_id";
    public static final String JID = "xmpp_jid";
    public static final String XMPPPASSWORD = "xmpp_password";
     public static final String XMPP_USER_NAME = "xmpp_username";




    public static final String IS_XMPP_LOGIN = "xmpp_logged_in";
    public static final String OUTGOING = "outgoing";
    public static final String INCOMING = "incoming";

    public static final String GROUPID = "group_id";
    public static final String ADMINID = "admin_id";
    public static final String GROUPNAME = "group_name";
    public static final String GROUPS_RESPONSE = "group_response";

    public static final String COUNTRY_NAME = "countryName";
    public static final String COUNTRY_CODE = "countryCode";

    public static final String PROFILE = "profile";

    public static final String CHAT_BG = "chat_bg";

    public static final String GALLERY_CHAT_BG_BASE64 = "gallery_chat_bg_base64";

    public static final String VCARD_LIST = "vCardList";

    public static final String OWNPRIVATEKEY = "ownprivatekey";
    public static final String OWNPUBLICKEY = "ownpublickey";

    public static final String CHAT_TEXT_USERS = "text_users";

//---------------------------------------------------------------------------------------------

    private SharedPrefrence() {

    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = ctx.getSharedPreferences("com.orderpro.customer", Context.MODE_PRIVATE );
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }


    public String getValue(String Tag) {
        return myPrefs.getString(Tag, "");
    }


    public void setValue(String Tag, String value) {
        prefsEditor.putString(Tag, value);
        prefsEditor.commit();
    }

    public void setBooleanValue(String Tag, boolean value) {
        prefsEditor.putBoolean(Tag, value);
        prefsEditor.commit();
    }

    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, Boolean.parseBoolean(""));
    }

    public void clearSharedPrefernces(Context ctx) {

        myObj = new SharedPrefrence();
        myPrefs = ctx.getSharedPreferences("com.orderpro.customer", Context.MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();

    }


    /*--------------------merchnat detail------------------*/

    public void setMerchantDetail(String Tag,MerchantDetail merchantDetail)
    {
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(merchantDetail); // myObject - instance of MyObject
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }
    public MerchantDetail getMerchantDetail(String Tag)
    {
        Gson gson = new Gson();
        String json = myPrefs.getString("MyObject", "");
        MerchantDetail obj = gson.fromJson(json, MerchantDetail.class);
        return obj;
    }

  /*------------------------Cart list------------------------------------*/
public void setCartList(String Tag, List<ProdVal> lst) {
    Gson gson = new Gson();
    String json = gson.toJson(lst);
    System.out.println("<<---json string-"+json);
    prefsEditor.putString(Tag, json);
    prefsEditor.apply();
}


    public ArrayList<ProdVal> getCartList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<ProdVal>();
        } else {
            Type type = new TypeToken<ArrayList<ProdVal>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<ProdVal> List = gson.fromJson(obj, type);
            return List;
        }
    }
/*------------------------Favourite list------------------------------------*/
/*public void setFavouriteList(String Tag, List<ProdVal> lst) {
    Gson gson = new Gson();
    String json = gson.toJson(lst);
    prefsEditor.putString(Tag, json);
    prefsEditor.apply();
}


    public ArrayList<ProdVal> getFavouriteList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<ProdVal>();
        } else {
            Type type = new TypeToken<ArrayList<ProdVal>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<ProdVal> List = gson.fromJson(obj, type);
            return List;
        }
    }*/

    public HashMap<String, ProdVal> getFavouriteMap(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");

        if (obj.equals("defValue")) {
            return new HashMap<String, ProdVal>();
        } else {
            Type type = new TypeToken<HashMap<String, ProdVal>>() {
            }.getType();
            Gson gson = new Gson();
            HashMap<String, ProdVal> List =  gson.fromJson(obj, type);
            return List;
        }
    }
    public void setFavouriteMap(String Tag, HashMap<String, ProdVal> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }
    /*--------------------------------------------------------------------------------------*/
    public void setAllProductList(String Tag, List<ProdVal> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<ProdVal> getAllProductList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<ProdVal>();
        } else {
            Type type = new TypeToken<ArrayList<ProdVal>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<ProdVal> List = gson.fromJson(obj, type);
            return List;
        }
    }
    /*----------------------------------------------------------*/
    public void setMerchantList(String Tag, ArrayList<MerchantDetail> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<MerchantDetail> getMerchantList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<MerchantDetail>();
        } else {
            Type type = new TypeToken<ArrayList<MerchantDetail>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<MerchantDetail> List = gson.fromJson(obj, type);
            return List;
        }
    }
    /*----------------------------------------------------------*/

    public void setAllMerchantList(String Tag, ArrayList<AllMerchantDetails> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<AllMerchantDetails> getAllMerchantList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<AllMerchantDetails>();
        } else {
            Type type = new TypeToken<ArrayList<AllMerchantDetails>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<AllMerchantDetails> List = gson.fromJson(obj, type);
            return List;
        }
    }
    //------------------------------------------------------------------

    public void setMerchantList_added(String Tag, List<MerchantList_LoginResponse> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<MerchantList_LoginResponse> getMerchantList_added(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<MerchantList_LoginResponse>();
        } else {
            Type type = new TypeToken<ArrayList<MerchantList_LoginResponse>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<MerchantList_LoginResponse> List = gson.fromJson(obj, type);
            return List;
        }
    }

//----------------- banner list-------------------------------

    public void setBannerList(String Tag, ArrayList<Banner> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<Banner> getBannerList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<Banner>();
        } else {
            Type type = new TypeToken<ArrayList<Banner>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<Banner> List = gson.fromJson(obj, type);
            return List;
        }
    }
    //-----------------categoty list-------------------------------


    public void setMainCategoryList(String Tag, ArrayList<CategoryBean> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<CategoryBean> getMainCategoryList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<CategoryBean>();
        } else {
            Type type = new TypeToken<ArrayList<CategoryBean>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<CategoryBean> List = gson.fromJson(obj, type);
            return List;
        }
    }
    //-----------------Subcategoty list-------------------------------


    public void setSubCategoryList(String Tag, ArrayList<SubCategory> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.apply();
    }


    public ArrayList<SubCategory> getSubCategoryList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<SubCategory>();
        } else {
            Type type = new TypeToken<ArrayList<SubCategory>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<SubCategory> List = gson.fromJson(obj, type);
            return List;
        }
    }

    //-----------------Product list-------------------------------

    public HashMap<String, ProdVal> getProduct(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");

        if (obj.equals("defValue")) {
            return new HashMap<String, ProdVal>();
        } else {
            Type type = new TypeToken<HashMap<String, ProdVal>>() {
            }.getType();
            Gson gson = new Gson();
            HashMap<String, ProdVal> List = gson.fromJson(obj, type);
            return List;
        }
    }
    public void setProductList(String Tag, HashMap<String, ProdVal> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }
//----------------------------orderList-------------------

    public HashMap<String, Order> getOrderList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new HashMap<String, Order>();
        } else {
            Type type = new TypeToken<HashMap<String, Order>>() {
            }.getType();
            Gson gson = new Gson();
            HashMap<String, Order> List = gson.fromJson(obj, type);
            return List;
        }
    }
    public void setOrderList(String Tag, HashMap<String, Order> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }
//------------------------------------------------------------------

    public HashMap<String, String> getHash(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new HashMap<String, String>();
        } else {
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Gson gson = new Gson();
            HashMap<String, String> List = gson.fromJson(obj, type);
            return List;
        }
    }

    public void setHash(String Tag, HashMap<String, String> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }

    public  void clearAllList(Context ctx)
    {
        myObj = new SharedPrefrence();
        myPrefs = ctx.getSharedPreferences("com.orderpro.customer", Context.MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        prefsEditor.remove(CART_LIST);
        prefsEditor.remove(QUICK_ORDER_LIST);
       // prefsEditor.remove(FAVOURITE_LIST);
        prefsEditor.apply();

        prefsEditor.commit();

    }


}