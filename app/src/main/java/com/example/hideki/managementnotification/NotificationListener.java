package com.example.hideki.managementnotification;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.os.Handler;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Mercury on 2015/06/03.
 */

public class NotificationListener extends NotificationListenerService{

    private Handler handler;

    private String TAG ="NotificationListener";
    Notification notifi;

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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {

        Log.d(TAG, "onNotificationPosted");

        CharSequence text = sbn.getNotification().tickerText;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        String title = sbn.getPackageName();

        String body = "　";
        if(text != null){
            body = text.toString();
        }else{
            text = "";
        }

        //通知作成
        notifi = new Notification();
        notifi.setTitle(title);
        notifi.setBody(body);
        notifi.setDate(date);
        notifi.setComplete(false);

        if(notifi.getBody() == null)
        {
            notifi.setBody("");
        }

        new AsyncTask<Notification, Void, Void>()
        {
            MobileServiceClient mClient;
            MNAdapter mAdapter;

            @Override
            protected Void doInBackground(final Notification... params) {

                Log.d("Main", "doInBackGround");
                Log.d("Main", params[0].getBody());

                try{
                    mClient = new MobileServiceClient("https://mnmobile.azure-mobile.net/",
                            "FzelBAxIDNvLBsVazacMeokCyNybYI94",
                            getApplicationContext());
                    mAdapter = new MNAdapter(getApplicationContext(), 0);

                    MobileServiceTable<Notification> dbTable = mClient.getTable("notificationMobile", Notification.class);

                    dbTable.insert(params[0]).get();

                    if(!params[0].isComplete())
                    {
                        mAdapter.add(params[0]);
                    }
                }catch (Exception e)
                {
                    Log.d("doInBackground", e.getMessage());
                }
                return null;
            }
        }.execute(notifi);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationRemoved");
    }


}
