package com.example.hideki.managementnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Mercury on 2015/07/04.
 */
public class NotificationReceiver extends BroadcastReceiver{

    public static Handler handler;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Reciver", "onReceive");

        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");

        if (handler != null) {
            Message msg = new Message();

            Bundle data = new Bundle();
            data.putString("title", title);
            data.putString("body", body);
            msg.setData(data);

            handler.sendMessage(msg);
        }
    }

    public void registerHandler(Handler locationUpdateHandler) {
        handler = locationUpdateHandler;
    }
}
