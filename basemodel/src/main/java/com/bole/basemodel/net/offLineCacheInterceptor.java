package com.bole.basemodel.net;

import com.bole.basemodel.BaseApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class offLineCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!AddCacheInterceptor.isNetworkConnected(BaseApplication.getApplicationConent())) {
            int offlineCacheTime = 60;//离线的时候的缓存的过期时间
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                    .build();
        }
        return chain.proceed(request);
    }
}
