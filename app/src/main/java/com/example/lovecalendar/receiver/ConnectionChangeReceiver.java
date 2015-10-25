package com.example.lovecalendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lovecalendar.Utils.NetUtils;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    private boolean isConnect=false;

    private GetNet getNet;

    public ConnectionChangeReceiver(Context context) {
        isConnect= NetUtils.isConnected(context);
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);    //手机网络
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //wifi网络

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            // unconnect network
            isConnect=false;
        } else {
            // connect network
            isConnect=true;
        }
        getNet.isNetConnect(isConnect);
    }

    public interface GetNet{
        public boolean isNetConnect(boolean isConnect);
    }

    public void setNetListener(GetNet getNet) {
        this.getNet = getNet;
    }
}
