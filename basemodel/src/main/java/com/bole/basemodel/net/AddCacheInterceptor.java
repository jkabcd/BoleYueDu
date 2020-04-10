package com.bole.basemodel.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.bole.basemodel.BaseApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.Response;


public class AddCacheInterceptor implements Interceptor {
   int maxAge= 3600*24;
    private Response response1;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if(isNetworkConnected(BaseApplication.getApplicationConent())){
            response1 = response.newBuilder()
                    .removeHeader("pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control","public, max-age="+maxAge).build();
        }else {
            response1 = response.newBuilder()
                    .removeHeader("pragma")
                    .header("Cache-Control","public,only-if-cached, max-stale="+maxAge).build();
        }


        return response1;
    }
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
