package com.example.shoponline_java.Helper;

import android.app.Application;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class MyApp extends Application {
    private static final String ONESIGNAL_APP_ID = "d80ed0e9-4fe1-44e7-81a2-6ac0f6f478fd";
    @Override
    public void onCreate() {
        super.onCreate();
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(false, Continue.none());
    }
}
