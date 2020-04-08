package com.bole.basemodel.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder reb= request.newBuilder();
        Request newrequest = reb
                .addHeader("X-Mashape-key","yuoukey")
                .addHeader("Accept","application/json")
                .build();
        return chain.proceed(newrequest);
    }
}
