package com.example.ibrah.first;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ibrah on 27.01.2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        String freshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + freshToken);


    }
}
