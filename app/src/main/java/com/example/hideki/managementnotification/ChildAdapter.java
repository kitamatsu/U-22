package com.example.hideki.managementnotification;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Mercury on 2015/07/02.
 */
public class ChildAdapter extends ArrayAdapter<ChildMobile> {

    Context mContext;
    int mLayoutResourceId;

    public ChildAdapter(Context context, int layoutResourcedId){
        super(context, layoutResourcedId);

        mContext = context;
        mLayoutResourceId = layoutResourcedId;
    }

}
