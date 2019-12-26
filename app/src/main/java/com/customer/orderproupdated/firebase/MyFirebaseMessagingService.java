package com.customer.orderproupdated.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    JSONObject object;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload...
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            System.out.println("Notification--------" + remoteMessage.toString());
        }
        // Check if message contains a notification payload...
        if (remoteMessage.getNotification() != null) {
            //Toast.makeText(this, ""+remoteMessage, Toast.LENGTH_SHORT).show();
            System.out.println("Notification--------" + remoteMessage.toString());
            Log.d(TAG, "Message Notification Body:" + remoteMessage.getNotification().getBody());
        }
        
        try {
            Map<String, String> params = remoteMessage.getData();
            object = new JSONObject(params);
            Log.e("JSON_OBJECT", object.toString());
            sendBookingStatusNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Notification--------" + remoteMessage.toString());

        // {"timestamp":"2019-12-20 09:51:34","title":"Request alert.","message":"nishu nishu sending you a request."}
    }

    private void sendBookingStatusNotification() {
        try {
            Intent i = new Intent();
            i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(object.getString("title"))
                    .setContentText(object.getString("message"))
                    .setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {

        }
    }
    // {"timestamp":"2019-12-23 13:34:57","title":"Request alert.","message":"Your order is processing."}
}