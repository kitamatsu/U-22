package com.example.hideki.managementnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.Handler;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity{

    private NotificationReceiver receiver;
    private IntentFilter intentFilter;
    private Notification notifi;
    Intent listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener = new Intent(this, NotificationListener.class);

        receiver = new NotificationReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION");
        registerReceiver(receiver, intentFilter);

        receiver.registerHandler(getNotification);

        ButterKnife.bind(this);
    }


    //通知ボタン
    @OnClick(R.id.Button01)
    void clickButton(Button button) {
        Log.d("Button", "clickButton");
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.google.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
        android.app.Notification notification = new android.app.Notification.Builder(MainActivity.this)
                .setContentTitle("Title!")
                .setContentText("Content Text!")
                .setTicker("Tiker Text!")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha)
                .setAutoCancel(true)
                .build(); NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1000, notification);
    }

    //詳細設定ボタン
    @OnClick(R.id.Button02)
    void clickButton2(Button button)
    {
        Intent i = new Intent(this, SimplePreferenceActivity.class);
        startActivity(i);
    }



private Handler getNotification  = new Handler() {
    @Override
    public void handleMessage(Message msg)
    {
        Log.d("Main", "handleMessage");
        
        Bundle bundle = msg.getData();

        Log.d("handle", bundle.toString());
        //String[] array = bundle.getStringArray("arrayStr");
        String title = bundle.getString("title");
        String body = bundle.getString("body");

        Calendar cal = Calendar.getInstance();
        Date date =  cal.getTime();
        Log.d("handle", cal.getTime().toString());

        notifi = new Notification();

        notifi.setTitle(title);
        notifi.setBody(body);
        notifi.setDate(date);
        notifi.setComplete(false);

        if(notifi.getBody() == null)
        {
            notifi.setBody("");
        }

        Log.d("handle", "" + title + " : " + body);
        Log.d("Main", notifi.getTitle() + " : " + notifi.getBody());

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
                            MainActivity.this);
                    mAdapter = new MNAdapter(MainActivity.this, 0);

                    MobileServiceTable<Notification> dbTable = mClient.getTable("notificationMobile", Notification.class);

                    dbTable.insert(params[0]).get();

                    if(!params[0].isComplete())
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.add(params[0]);
                            }
                        });
                    }
                }catch (Exception e)
                {
                    Log.d("doInBackground", e.getMessage());
                }
                return null;
            }
        }.execute(notifi);
    }


};

}
