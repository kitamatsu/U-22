package com.example.hideki.managementnotification;

import android.os.Build;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Mercury on 2015/06/26.
 */
public class Notification {

    private String id;
    private int notificationId;
    private Date date;
    private String title;
    private String body;
    private String serialID;
    private Boolean complete;


    //const
    public Notification(){

        this.serialID = Build.SERIAL;

    }

    public Boolean isComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSerialID() {
        return serialID;
    }

    public void setSerialID(String serialID) {
        this.serialID = serialID;
    }

    public Boolean getComplete() {
        return complete;
    }

}
