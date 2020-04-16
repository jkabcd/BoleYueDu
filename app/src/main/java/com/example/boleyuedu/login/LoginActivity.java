package com.example.boleyuedu.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bole.basemodel.BaseActivity;
import com.bole.basemodel.BaseApplication;
import com.bole.basemodel.bean.EventMsg;
import com.example.boleyuedu.R;
import com.example.boleyuedu.fragment.Fragement1;
import com.example.boleyuedu.fragment.Fragement2;
import com.jakewharton.rxbinding3.view.RxView;

import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContrat.LoginIView {

    private TextView textView;
    private Fragement2 fragement2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragement1 fragement1;

    @Override
    public void updateUI() {
//        getSupportFragmentManager().beginTransaction().addToBackStack();
//        getFragmentManager().beginTransaction().addToBackStack();

        Toast.makeText(BaseApplication.getApplicationConent(), "调用了5", Toast.LENGTH_SHORT).show();

    }


    @Override
    public int getlayoutId() {
        return R.layout.activity_main3;
    }

    @Override
    public void initView() {
        textView = findViewById(R.id.btn_c);
    }

    @Override
    public void initData() {
        receiveData(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if(eventMsg.getTag()==22){
                    textView.setText(eventMsg.getData().toString());
                }
            }
        });
        findViewById(R.id.btn_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.sendEmptyMessageDelayed(1,0);
                mvppresenter.senddata();
            }
        });
        fragement1 = new Fragement1();
        fragement2 = new Fragement2();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fra_se, fragement1,"111").add(R.id.fra_se, fragement2,"2222").commit();

//        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
//        fragmentTransaction2.replace(R.id.fra_se,fragement2,"tag2").commit();
//        fragmentTransaction2.addToBackStack(null);
//        fragmentTransaction.commitNow();
//        fragmentTransaction2.commit();
    }
Handler handler = new Handler(){
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        fragmentManager.beginTransaction().hide(fragement1).show(fragement2).commit();
    }
};

}
