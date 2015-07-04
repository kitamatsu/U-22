package com.example.hideki.managementnotification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Mercury on 2015/07/02.
 */
public class MNAdapter extends ArrayAdapter<Managementnotificationdb> {

    Context mContext;
    int mLayoutResourceId;

    public MNAdapter(Context context, int layoutResourcedId){
        super(context, layoutResourcedId);

        mContext = context;
        mLayoutResourceId = layoutResourcedId;
    }

//表示に使用する
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//
//        final Managementnotificationdb currentItem = getItem(position);
//
//        if (row == null) {//再利用できるrowがあるかどうかをチェック、なければ新規作成
//            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//            row = inflater.inflate(mLayoutResourceId, parent, false);
//        }
//
//        //rowのlayoutになにを表示させるかを設定
//        TextView pointTv = (TextView) row.findViewById(R.id.point);
//        TextView priceDownTv = (TextView) row.findViewById(R.id.priceDown);
//        pointTv.setText(Float.toString(currentItem.getPointUp()));
//        priceDownTv.setText(Integer.toString(currentItem.getPriceDown()));
//
//        return row;
//    }
}
