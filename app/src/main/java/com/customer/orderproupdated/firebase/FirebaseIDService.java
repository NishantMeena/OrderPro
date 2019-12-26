package com.customer.orderproupdated.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.customer.orderproupdated.Utility.Utility;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    SharedPreferences share;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token...
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Utility.setSharedPreference(this, "fcm_token", refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers...
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers...
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token...
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed...
    }
}
