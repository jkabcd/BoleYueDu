package com.example.boleyuedu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bole.basemodel.BaseActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.bean.Response;
import com.bole.basemodel.exception.ResponseTransformer;
import com.bole.basemodel.net.NetWorkManager;
import com.bole.basemodel.tools.PermissionHelper;
import com.bole.basemodel.tools.RxBus;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity3 extends BaseActivity {
    private Button btn_c;
    private TextView tv_dfa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tv_dfa = findViewById(R.id.tv_dfa);
        btn_c = findViewById(R.id.btn_c);
        String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionHelper.getInstance().checkPermissions(permissions, MainActivity3.this);
        receiveData(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
              if(eventMsg.getTag()==111)
                btn_c.setText(eventMsg.getData()+"");
            }
        });

        findViewById(R.id.btn_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
            }
        });
    }
public void getdata(){
    sendHttpData(NetWorkManager.getRequest().getkefu(), Msg.class, new Consumer<Msg>() {
        @Override
        public void accept(Msg msg) throws Exception {
            Toast.makeText(MainActivity3.this,msg.getMsg(),Toast.LENGTH_SHORT).show();
        }
    });
//    NetWorkManager.getRequest().getkefu().compose(ResponseTransformer.handleResult(Msg.class)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Msg>() {
//        @Override
//        public void accept(Msg msg) throws Exception {
//        Log.e("","");
//        }
//    });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
