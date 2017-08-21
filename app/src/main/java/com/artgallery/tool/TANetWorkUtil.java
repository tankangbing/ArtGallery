package com.artgallery.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/8/10 0010.
 */

public class TANetWorkUtil {
    public TANetWorkUtil() {
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if(info != null) {
            for(int i = 0; i < info.length; ++i) {
                if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }
}
