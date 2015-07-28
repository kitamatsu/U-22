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
        //listener = new Intent(this, NotificationListener.class);
        ButterKnife.bind(this);
    }

    //TEST通知ボタン
    @OnClick(R.id.Button01)
    void clickButton(Button button) {
        Log.d("Button", "clickButton");
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.google.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
        android.app.Notification notification = new android.app.Notification.Builder(MainActivity.this)
                .setContentTitle("TEST通知")
                .setContentText("TEST")
                .setTicker("TEST通知です")
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

}
