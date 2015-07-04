package com.example.hideki.managementnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.Handler;


import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity{

    private NotificationReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, NotificationListener.class));

        receiver = new NotificationReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION");
        registerReceiver(receiver, intentFilter);

        receiver.registerHandler(getNotification);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.Button01)
    void clickButton(Button button) {
        Log.d("Button", "押した");
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.google.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
        Notification notification = new Notification.Builder(MainActivity.this)
                .setContentTitle("Title!")
                .setContentText("Content Text!")
                .setTicker("Tiker Text!")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha)
                .setAutoCancel(true)
                .build(); NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1000, notification);

    }

private Handler getNotification  = new Handler() {
    @Override
    public void handleMessage(Message msg)
    {
        Log.d("Main", "handleMessage");
        Bundle bundle = msg.getData();
        String title = bundle.getString("title");
        String body = bundle.getString("body");

        Log.d("Main", title + " : " + body);
    }


};

}
