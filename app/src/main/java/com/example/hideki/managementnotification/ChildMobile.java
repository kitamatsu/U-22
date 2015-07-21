package com.example.hideki.managementnotification;

import android.os.Build;

/**
 * Created by Mercury on 2015/07/21.
 */
public class ChildMobile {

    private String id;
    private boolean complete;
    private String serialid = Build.SERIAL;
    private String childname;
    private int accountid;

    public ChildMobile() {
        this.serialid = Build.SERIAL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

}
