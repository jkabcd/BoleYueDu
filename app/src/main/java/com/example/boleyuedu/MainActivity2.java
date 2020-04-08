package com.example.boleyuedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bole.basemodel.BaseActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.tools.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity2 extends BaseActivity {
    private Disposable disposable;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.btn_c);
   receiveData(new Consumer<EventMsg>() {
       @Override
       public void accept(EventMsg eventMsg) throws Exception {
           if(221==eventMsg.getTag()){
               button.setText(eventMsg.getData() + "");
           }
       }
   });
        findViewById(R.id.btn_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(new EventMsg<String>(111,"传到2"));
                startActivity(new Intent(MainActivity2.this,MainActivity3.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(RxBus.getInstance().isObserver()){
            RxBus.getInstance().unregister(disposable);
        }
    }
}
