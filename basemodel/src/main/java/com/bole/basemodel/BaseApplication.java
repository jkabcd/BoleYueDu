package com.bole.basemodel;

import android.app.Application;
import android.content.Context;

import com.bole.basemodel.net.NetWorkManager;

public class BaseApplication extends Application {
    private static Context content;
    @Override
    public void onCreate() {
        super.onCreate();
        this.content = this;
//        if(LeakCanary.isInAnalyzerProcess(this)){
//            return;
//        }
//        LeakCanary.install(this);//内存泄漏检测
        NetWorkManager.getInstance().init();//初始化网络

   }

   public static Context getApplicationConent(){
        return content;
   }

}
