package com.customer.orderproupdated.chat.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;

;


public class NetworkChangeReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            this.context = context;
            if (NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_WIFI) {
                startService();
            } else if (NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_MOBILE) {
                startService();
            } else if (NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_NOT_CONNECTED) {
                stopService();
            }
        } catch (Exception e) {
        }
    }

    public void startService() {
        try {
            if (Utility.isMyServiceRunning(RoosterConnectionService.class, context)) {
                Intent i1 = new Intent(context, RoosterConnectionService.class);
                context.stopService(i1);
            }
            Intent i1 = new Intent(context, RoosterConnectionService.class);
            context.startService(i1);
        } catch (Exception e) {
        }
    }

    public void stopService() {
        try {
            if (Utility.isMyServiceRunning(RoosterConnectionService.class, context)) {
                Intent i1 = new Intent(context, RoosterConnectionService.class);
                context.stopService(i1);
            }

        } catch (Exception e) {
        }
    }
}