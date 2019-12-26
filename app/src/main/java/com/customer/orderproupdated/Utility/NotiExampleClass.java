package com.customer.orderproupdated.Utility;

public class NotiExampleClass {

    /*package com.organiccart.firebase;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.organiccart.R;
import com.organiccart.activity.DashBoardActivity;
import com.organiccart.utils.ConnectionDetector;
import com.organiccart.utils.Constant;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String currentDateandTime;
    RemoteMessage.Builder builder1;
    ConnectionDetector cd;
    private boolean foregroundValue = false;
    private boolean isNotificationShowed = false;
    Context cx;
    JSONObject j;
    String message, status;
    Context ctx;
    int i = 0;
    int notification_count=0;
    int CHANNEL_ID=0;
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Random rnd = new Random();
            i=100 + rnd.nextInt(90000);
            ctx = getApplicationContext();
            SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(this);
            Constant.organicCartUserId = settings1.getString("mobile", "");
            try
            {
                notification_count  = Integer.parseInt(settings1.getString("notification", null));
            }
            catch (Exception c)
            {
                c.printStackTrace();


            }


            Constant.notification_counter=notification_count+1;
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("notification", String.valueOf(Constant.notification_counter));
            editor.commit();

            Map<String, String> data = remoteMessage.getData();
            if (data.size() > 0) {
                String jsonString = data.get("data");
                if (jsonString.contains("message")) {
                    JSONObject jObj = new JSONObject(jsonString);
                    message = jObj.getString("message");
                    sendNotification(message);
                }
                try {
                    Intent in = new Intent("NotificationPushReceived");
                    sendBroadcast(in);
                } catch (Exception cv) {
                    cv.printStackTrace();
                }
       }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendNotification(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
        currentDateandTime = sdf.format(new Date());
        System.out.println("currentDateandTime===" + currentDateandTime);



        try {
            j = new JSONObject(message);
            message = j.optString("message");

        } catch (Exception e) {
            e.printStackTrace();
        }

            try
            {
                SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(this);
                Constant.organicCartUserId = settings1.getString("organicCartUserId", "");

                if(Constant.organicCartUserId.equalsIgnoreCase(""))
                {
                    Intent intent = new Intent(this, DashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(this, DashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    String CHANNEL_ID = "my_channel_01";// The id of the channel.
                    CharSequence name = "My Channel";// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    }
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notification;
                    notification = mBuilder
//.setTicker(title).setWhen(0)
                            .setAutoCancel(true)
                            .setContentTitle("CLickPharma")
                            .setContentIntent(pendingIntent)
                            .setSound(defaultSoundUri)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setSmallIcon(R.drawable.logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setContentText(message)
                            .setChannelId(CHANNEL_ID)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    notificationManager.notify(0, notification);

                   }
                 }
            catch (Exception d)
            {
                d.printStackTrace();
            }

        isNotificationShowed = true;
    }
    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
     @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        if (intent.getExtras() != null) {
            try {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                builder1 = builder;
                new CheckIfForeground().execute();
            } catch (Exception e) {
            }
        }
    }

    private class CheckIfForeground extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("Foreground App", appProcess.processName);
                    if (getPackageName().equalsIgnoreCase(appProcess.processName)) {
                        Log.i("tag", "foreground true:" + appProcess.processName);
                        foregroundValue = true;
                        break;
                    } else {
                        if (!isNotificationShowed) {
                            onMessageReceived(builder1.build());
                        }
                    }
                }
            }
            return null;
        }
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        return useWhiteIcon ? R.drawable.logo : R.drawable.logo;
    }

    private int createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Random rnd = new Random();
            CHANNEL_ID=100 + rnd.nextInt(90000);

            CharSequence name = "OrganicCart";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(String.valueOf(CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        return useWhiteIcon ? R.drawable.logo : R.drawable.logo;

    }

}
*/


}
