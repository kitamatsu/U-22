package com.example.hideki.managementnotification;

import android.app.Application;
import android.util.Log;

/**
 * Created by Mercury on 2015/07/04.
 */
public class MyApplication extends Application{

    private Managementnotificationdb mnb;

    @Override
    public void onCreate() {
        Log.d("MyApplication", "onCreate");
    }

    public Managementnotificationdb getMnb() {
        return mnb;
    }

    public void setMnb(Managementnotificationdb mnb) {
        this.mnb = mnb;
    }
}
