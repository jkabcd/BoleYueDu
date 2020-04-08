package com.example.boleyuedu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bole.basemodel.BaseActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.net.NetWorkManager;
import com.bole.basemodel.tools.RxBus;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity3 extends BaseActivity {
    private Button btn_c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btn_c = findViewById(R.id.btn_c);
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
//                sendData(new EventMsg<String>(221,"传回去"));
//                finish();


//                Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
//                startActivity(intent);
                getdata();

            }
        });
    }
public void getdata(){
    NetWorkManager.getRequest().getkefu().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Msg>() {
        @Override
        public void accept(Msg s) throws Exception {
            Log.e("d","d");
        }
    });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
