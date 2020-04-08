package com.bole.basemodel.net;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bole.basemodel.BaseApplication;
import com.bole.basemodel.BuildConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bole.basemodel.tools.Contons.BASE_URL;

public class NetWorkManager {
    private static NetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile Api request = null;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(okClinet().build())
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Api getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = retrofit.create(Api.class);
            }
        }
        return request;
    }

    private Cache provideCache(Context context){
        boolean ismoutd = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String stpath="";
        File file ;
        if(ismoutd){
            Log.e("bbb","文件可用");
            stpath=Environment.getExternalStorageDirectory().toString()+"/boleyuedu";
            file= new File(stpath);
            if(!file.exists()){
                file.mkdir();
            }
        }else {
            Log.e("bbb","不文件可用");
            file =context.getCacheDir();
        }
        return new Cache(file,10240*1024);
    }

    private OkHttpClient.Builder okClinet(){
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpClient
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AddHeadInterceptor())
                .addNetworkInterceptor(new AddCacheInterceptor())
                .cache(provideCache(BaseApplication.getApplicationConent()))
                .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build();
        return okHttpClient;
    }
}
