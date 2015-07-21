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

//表示に使用する
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//
//        final Notification currentItem = getItem(position);
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
