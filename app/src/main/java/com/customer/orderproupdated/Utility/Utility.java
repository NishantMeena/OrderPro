package com.customer.orderproupdated.Utility;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.SplashActivity;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.utility.Base64;

import org.jivesoftware.smack.packet.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Sony on 1/10/2017.
 */
public class Utility {
    static Context  mContext;
    private static String PREFERENCE = "orderpro";

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static boolean checkInternetConn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        SharedPrefrence share = SharedPrefrence.getInstance(context);
        if (Utility.checkItFromSetting(share, SharedPrefrence.RESTRICT_DATA_USAGE) && mobile.isConnected()) {

            //Toast.makeText(context, "Mobile Data Enable", Toast.LENGTH_SHORT).show();
        } else if (Utility.checkItFromSetting(share, SharedPrefrence.RESTRICT_DATA_USAGE) && wifi.isConnected()) {
            //Toast.makeText(context, "wifi Enable", Toast.LENGTH_SHORT).show();
        }
        NetworkInfo n_info = connMgr.getActiveNetworkInfo();

        if (n_info == null) {

            return false;
        } else {
            return true;
        }
    }
    public static boolean checkItFromSetting(SharedPrefrence share, String tag) {

        boolean bool=share.getBooleanValue(tag);

        if (bool) {
            return true;
        } else {
            return false;
        }
       /* String value = share.getValue(tag); // true Or false

        if (value.equals(SharedPrefrence.TRUE)) {
            return true;
        } else {
            return false;
        }*/
    }



    // for username string preferences
    public static void setSharedPreference(Context context, String name, String value) {
        mContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static String getFormattedTime(long timestamp) {

        SimpleDateFormat dateFormater = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date(timestamp);
        return dateFormater.format(date);

    }
    public static String getCurentDateIFormate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
/*--------------------------------------------------------*/

    public static String MessageTransfer(ChatBody chat) {

        String Message = chat.getMessage();
        return Message;
    }


    public static String fileTransferSingleImageVideo(ChatBody filesInfo) {

        String Message = filesInfo.getUrl() + PreferenceConstant.SEPERATOR + filesInfo.getThumb() + PreferenceConstant.SEPERATOR + filesInfo.getPriority();

        return Message;
    }


    public static String fileTransferSingleAudio(ChatBody chat) {

        String Message = chat.getUrl() + PreferenceConstant.SEPERATOR + chat.getAudio_title() + PreferenceConstant.SEPERATOR + chat.getPriority();
        return Message;
    }


    public static String GroupMessageTransfer(ChatBody chat) {

        String Message = chat.getMessage() + PreferenceConstant.SEPERATOR + chat.getPriority();

        return Message;
    }






/*---------------------------------------------------------*/

    public static String utf8UrlEncoding(String message) throws UnsupportedEncodingException {

        try {
            message = URLEncoder.encode(message, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e) {
        }

        return message;
    }

    public static String utf8UrlDecoding(String message) throws UnsupportedEncodingException {

        try {

            message= URLDecoder.decode(message.replace("+", "%2B"), "UTF-8")
                    .replace("%2B", "+");
        } catch (Exception e) {
        }

        return message;
    }
    /*-------------------------------------------------*/
    public static int getIntegerFromIndex(String msg, int index) {
        int l = 0;
        try {
            String[] val = msg.split(PreferenceConstant.SEPERATOR);
            return Integer.parseInt(val[index]);
        } catch (Exception ex) {
            return l;
        }
    }
    public static String getStringFromIndex(String msg, int index) {
        try {
            String[] val = msg.split(PreferenceConstant.SEPERATOR);
            return val[index];
        } catch (Exception ex) {
            return "";
        }
    }

    public static long getLongFromIndex(String msg, int index) {
        long l = 0;
        try {
            String[] val = msg.split(PreferenceConstant.SEPERATOR);
            return Long.parseLong(val[index]);
        } catch (Exception ex) {
            return l;
        }
    }

    public static String[] getArrayFromString(String val, String seperator) {
        String[] array = null;
        try {
            array = val.split(seperator);
            return array;
        } catch (Exception ex) {
            return array;
        }
    }

    /*----------------------------------------*/

    public static String getBareJID(String fromJID) {
        String from = fromJID;
        String contactJid = "";
        if (from.contains("/")) {
            contactJid = from.split("/")[0];
            DialogUtility.showLOG("The real jid is :" + contactJid);
        } else {
            contactJid = from;
        }
        return contactJid;
    }

    /*-----------------------------------------*/

    public static ChatBody getChatBodyForChatType(String txt, String OwnerJID, String FreindJID, String type,
                                                  String bound, String status, Message newMessage, String Category, int priority) {

        ChatBody chat = new ChatBody();
        chat.setOwnerJID(OwnerJID);
        chat.setFriendJID(FreindJID);
        chat.setMessageID(newMessage.getPacketID());
        chat.setMessage(txt);
        chat.setMessageType(type);
        chat.setInOutBound(bound);
        chat.setDate(Utility.getCurentDateIFormate());
        chat.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        chat.setProgress(PreferenceConstant.CHAT_PROGRESS);
        chat.setStatus(status);
        chat.setCategory(Category);
        chat.setPriority(priority);

        return chat;
    }
    /*--------------------------------------*/
    public static String fileTransferSingldoc(ChatBody chat) {

        String Message = chat.getUrl() + PreferenceConstant.SEPERATOR + chat.getExtension() + PreferenceConstant.SEPERATOR + chat.getPriority();
        return Message;
    }

    public static boolean isMessageBelongsToChat(String freind_ID) {
        boolean isMessageBelongsToChat = false;

        if (freind_ID.contains(PreferenceConstant.SERVERATTHERSTE)) {
            isMessageBelongsToChat = true;
        }

        return isMessageBelongsToChat;
    }

    /*-----------------------------------------------------*/

    public static String getNumberFromJID(String JID) {
        if (JID != null) {
            return JID.split("@")[0];
        }
        return JID;
    }
    /*Get File Extension*/
    public static String getFileExtension(File file) {

        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String getThumbFromBitmap(Bitmap bitmap) {

        String thumbBase64 = "";
        try {
            thumbBase64 = encodeTobase64(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbBase64;
    }

    public static String getImageThumb(String localUri) {

        String thumbBase64 = "";
        try {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(localUri),
                    150, 150);
            thumbBase64 = encodeTobase64(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbBase64;
    }


    public static String getImageThumbDynamicSize(String localUri) {

        String thumbBase64 = "";
        try {
            String[] widthHeight = getIMGSize(Uri.parse(localUri)).split(PreferenceConstant.SEPERATOR);
            int w = (int) Float.parseFloat(widthHeight[0]);
            int h = (int) Float.parseFloat(widthHeight[1]);

            Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(localUri),
                    w, h);

            thumbBase64 = encodeTobase64(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbBase64;
    }

    public static String getIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        return calculateRatio(imageWidth, imageHeight);
    }

    public static Bitmap getBitmapFromPath(String localUri) {

        Bitmap bitmap = null;
        try {
            bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(localUri),
                    150, 150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String getSDCardPathForUIL(String fileName) {
        String loadURL = "file://" + fileName;
        return loadURL;
    }


    public static boolean hasActiveInternetConnection(Context context) {
        String LOG_TAG = "Check for slow internet connectivity";

        if (checkInternetConn(context)) {

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(2000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

    public static String getImageThumbDynamicSize(String localUri, Context context) {

        String thumbBase64 = "";
        try {
            String[] widthHeight = getIMGSize(Uri.parse(localUri)).split(PreferenceConstant.SEPERATOR);
            int w = (int) Float.parseFloat(widthHeight[0]);
            int h = (int) Float.parseFloat(widthHeight[1]);

            String compressedPath = compressImage(localUri, context);

            Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(compressedPath),
                    w, h);

//            drawBitmapToCanvas(localUri, context);
            try{
                // bitmap = blur(context, bitmap);

                Bitmap outBitmap = bitmap.copy(bitmap.getConfig(), true);

                //Create the context and I/O allocations
                final RenderScript rs = RenderScript.create(context);
                final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
                final Allocation output = Allocation.createTyped(rs, input.getType());

                //Blur the image
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(4f);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(outBitmap);

                rs.destroy();


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                outBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;

                bitmap = compressImage(outBitmap);

                ByteArrayOutputStream streams = new ByteArrayOutputStream();
                outBitmap.compress(Bitmap.CompressFormat.JPEG, 1, streams);
                byte[] imageInBytes = streams.toByteArray();
                long lengthbmps = imageInBytes.length;

                thumbBase64 = encodeTobase64(outBitmap);


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbBase64;
    }

    public static Bitmap compressImage(Bitmap bmp) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;

        int actualHeight = bmp.getHeight();
        int actualWidth = bmp.getWidth();

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return bmp;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);

        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            return cursor.getString(idx);
        }
    }

    public static String compressImage(String imageUri, Context context) {

        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 0, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }
    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "OrderPro/CompressedResources");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }
    public static int calculateWidth(String path) {

        Uri uri = Uri.parse(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);

        int w = options.outWidth;
        int h = options.outHeight;

        // Calculate Percent as per height 200
        float percentage = 0f;
        float height = SplashActivity.screenWidth;
        int width = 0;

        percentage = (height * 100) / h;    // height=wanted height & h=image actual height

        // Use percentage to calculate width from respective percentage
        width = (int) (percentage * w) / 100;     // width=wanted width & w=image actual width

        return width;
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            immagex.compress(Bitmap.CompressFormat.PNG, 80, baos);

        } catch (Exception e) {
        }
        byte[] b = baos.toByteArray();
        String imageEncoded = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static String calculateRatio(int w, int h) {

        // Calculate Percent as per height (half of screen width)
        float percentage = 0f;
        float height = SplashActivity.screenWidth;
        float width = 0f;

        percentage = (height * 100) / h;    // height=wanted height & h=image actual height

        // Use percentage to calculate width from respective percentage
        width = (percentage * w) / 100;     // width=wanted width & w=image actual width

        int wdth = (int) width;
        int hght = (int) height;

        return wdth + PreferenceConstant.SEPERATOR + hght;
    }

    public static Bitmap decodeBase64ForBubble(String input) {
        try {

            String temp = input.replace(" ", "+");

            byte[] decodedByte = android.util.Base64.decode(temp, Base64.DEFAULT);

            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

            String[] widthHeight = calculateThumbRatio(bitmap.getWidth(), bitmap.getHeight()).split(PreferenceConstant.SEPERATOR);
            int w = (int) Float.parseFloat(widthHeight[0]);
            int h = (int) Float.parseFloat(widthHeight[1]);

            return ThumbnailUtils.extractThumbnail(bitmap, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String calculateThumbRatio(int w, int h) {

// Calculate Percent as per height (half of screen width)
        float percentage = 0f;
        float height = SplashActivity.screenWidth;
        float width = 0f;

        percentage = (height * 100) / h; // height=wanted height & h=image actual height

// Use percentage to calculate width from respective percentage
        width = (percentage * w) / 100; // width=wanted width & w=image actual width

        return width + PreferenceConstant.SEPERATOR + height;
    }

    public static double getFileSize(String filepath) {
        double fileSizeInMB = 0.0;
        try {
            File file = new File(filepath);
            double fileSizeInBytes = file.length();
            double fileSizeInKB = fileSizeInBytes / 1024;
            fileSizeInMB = fileSizeInKB / 1024;

        } catch (Exception ex) {
        }
        return fileSizeInMB;

    }
    public static boolean checkRestrictData(Context context) {
        boolean bool;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        SharedPrefrence share = SharedPrefrence.getInstance(context);
        if (Utility.checkItFromSetting(share, SharedPrefrence.RESTRICT_DATA_USAGE) && mobile.isConnected()) {
//         showAlertDialog(,"Alert Message","Mobile Data Enable");
//            Toast.makeText(context, "Mobile Data Enable restrict data", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utility.checkItFromSetting(share, SharedPrefrence.RESTRICT_DATA_USAGE) && wifi.isConnected()) {
//            Toast.makeText(context, "wifi Enable restrict data", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;

    }

    public static String formatGroupJID(String JID) {
        if (JID.contains(PreferenceConstant.SERVERATTHERSTE)) {
            JID = JID.replace(PreferenceConstant.SERVERATTHERSTE, "");
        }
        if (!JID.contains("@broadcast." + PreferenceConstant.SERVER)) {
            JID = JID + "@broadcast." + PreferenceConstant.SERVER;
        }
        return JID;
    }

    //-------------------------------------------------------------------

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1); }

    public static String lowercaseAllLetters(final String line) {
        return line.toLowerCase();
    }

}
