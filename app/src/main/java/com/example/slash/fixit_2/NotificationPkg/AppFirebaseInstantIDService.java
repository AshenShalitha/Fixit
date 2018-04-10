package com.example.slash.fixit_2.NotificationPkg;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import android.util.Log;

/**
 * Created by slash on 1/19/2018.
 */

public class AppFirebaseInstantIDService extends FirebaseInstanceIdService{

    private static final String TAG = "FCM Token";
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String token)
    {

    }

}
