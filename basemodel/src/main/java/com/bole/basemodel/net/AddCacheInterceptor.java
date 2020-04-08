package com.bole.basemodel.net;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.Response;


public class AddCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Response response1 = response.newBuilder()
                .removeHeader("pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control","max-age="+3600*24).build();

        return response1;
    }

}
