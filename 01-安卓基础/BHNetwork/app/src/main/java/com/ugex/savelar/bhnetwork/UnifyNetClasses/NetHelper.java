package com.ugex.savelar.bhnetwork.UnifyNetClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetHelper {
    public static boolean checkNetworkStateAvaliable(Context context){
        ConnectivityManager connManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=connManager.getActiveNetworkInfo();
        if(netinfo==null){
            Toast.makeText(context, "无可用网络", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(netinfo.isConnected()){

                if(netinfo.getType()==ConnectivityManager.TYPE_MOBILE){
                    Toast.makeText(context, "正在使用移动数据网络", Toast.LENGTH_SHORT).show();
                }else if(netinfo.getType()==ConnectivityManager.TYPE_WIFI){
                    Toast.makeText(context, "正在使用WIFI网络", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "正在使用其他类型网络", Toast.LENGTH_SHORT).show();
                }
                return true;
            }else{
                Toast.makeText(context, "当前网络未连接", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }
}
