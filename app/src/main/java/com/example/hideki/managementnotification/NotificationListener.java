package com.example.hideki.managementnotification;

import android.app.NotificationManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Mercury on 2015/06/03.
 */

public class NotificationListener extends NotificationListenerService{

    private String TAG ="Notification";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        CharSequence text = sbn.getNotification().tickerText;
        long time  = sbn.getPostTime();
        String data = String.valueOf(time) + text;
        Log.d(TAG,data);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
