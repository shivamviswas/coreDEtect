package com.wikav.coviddetect.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiver connectivityReceiver;

    public ConnectivityReceiver(){
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network activeNetwork= cm.getActiveNetwork();

        }


    }

    public interface ConnectivityReciverListner{
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
