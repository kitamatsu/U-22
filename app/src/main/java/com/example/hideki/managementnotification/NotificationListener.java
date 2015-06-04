package com.example.hideki.managementnotification;

import android.app.NotificationManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mercury on 2015/06/03.
 */

public class NotificationListener extends NotificationListenerService{

    private String TAG ="Notification";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        CharSequence text = sbn.getNotification().tickerText;
        //long time  = sbn.getPostTime();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String data = String.valueOf(date) + ": " + text;
        Log.d(TAG,data);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
