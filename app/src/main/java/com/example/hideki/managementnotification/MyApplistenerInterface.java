package com.example.hideki.managementnotification;

import java.util.EventListener;

/**
 * Created by Mercury on 2015/07/04.
 */
public interface MyApplistenerInterface extends EventListener{

    //MyAppにデータが入った時に通知
    public void inData();

}
