package com.bole.basemodel.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import kr.co.namee.permissiongen.PermissionGen;

public class PermissionHelper {
    private static final int HANDLER_TAG=123;
    public static final int NORMAL_PERMISSION = 124;

    private Context mContext;
    private String mPermissions[];

    private Handler mHandler=new PermissionHandler(PermissionHelper.this);

    private PermissionHelper() {
    }

    private static class Holder {
        private static PermissionHelper instance = new PermissionHelper();
    }

    public static PermissionHelper getInstance() {
        return Holder.instance;
    }

    /**
     * 加基本权限
     **/
    public void checkPermissions(String permissions[],Context context) {
        this.mPermissions=permissions;
        this.mContext=context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(HANDLER_TAG);
            }
        }).start();
    }


    private void requestPermissions() {
        PermissionGen.with((Activity) mContext)
                .addRequestCode(NORMAL_PERMISSION)
                .permissions(mPermissions)
                .request();
    }


    /**
     * 重写activity的onRequestPermissionsResult方法，并在里面调用此方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionGen.onRequestPermissionsResult((Activity)mContext, requestCode, permissions, grantResults);
    }

    //自定义handler类
    static class PermissionHandler extends Handler {
        //弱引用(引用外部类)
        WeakReference<PermissionHelper> mCls;

        PermissionHandler(PermissionHelper cls) {
            //构造弱引用
            mCls = new WeakReference<PermissionHelper>(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过弱引用获取外部类.
            PermissionHelper cls = mCls.get();
            //进行非空再操作
            if (cls != null) {
                switch (msg.what){
                    case HANDLER_TAG:
                        cls.requestPermissions();
                        removeMessages(HANDLER_TAG);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
