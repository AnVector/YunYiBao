package com.anyihao.ayb.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;

import com.anyihao.ayb.R;

/**
 * Created by Admin on 2017/4/7.
 */

public class WifiAdapter extends RecyclerViewAdapter<ScanResult> {


    public WifiAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ScanResult scanResult) {

        holder.setText(R.id.title, scanResult.SSID);
        holder.setCompoundDrawables(R.id.title, R.drawable.ic_wifi_left, 20);
    }
}
