package com.example.boleyuedu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bole.basemodel.BaseActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.bean.Response;
import com.bole.basemodel.exception.ResponseTransformer;
import com.bole.basemodel.net.NetWorkManager;
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
        //
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
    NetWorkManager.getRequest().getkefu().compose(ResponseTransformer.<Response<Msg>>handleResult()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Response<Msg>>() {
        @Override
        public void accept(Response<Msg> msgResponse) throws Exception {
          Log.e("","");
        }
    });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
