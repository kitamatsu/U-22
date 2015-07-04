package com.example.hideki.managementnotification;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.os.Handler;


import java.util.Calendar;
import java.util.Date;


/**
 * Created by Mercury on 2015/06/03.
 */

public class NotificationListener extends NotificationListenerService{

    private Handler handler;

    private String TAG ="Notification";
    Managementnotificationdb notifi;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        NotificationListener getService() {
            return NotificationListener.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {

        Log.d(TAG, "onNotificationPosted");

        CharSequence text = sbn.getNotification().tickerText;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();


        Intent i = new Intent();
        i.putExtra("title", sbn.getPackageName());
        i.putExtra("body", text.toString());
        i.setAction("ACTION");
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationRemoved");
    }


}
