package com.bole.basemodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.exception.ResponseTransformer;
import com.bole.basemodel.tools.RxBus;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * **************************************
 * 项目名称：BoleYueDu
 *
 * @Author jack
 * 邮箱：1253865188@qq.com
 * 创建时间2020/4/13 11:34
 * 用途
 * **************************************
 */
public abstract class BaseFragment extends RxFragment {
    private Disposable transmitdisposable;
    private Unbinder unbinder;

    /**
     * @description 发送数据
     * @param eventMsg
     * @return
     * @author Administrator
     * @time 2020/4/10 17:50
     */
    public void sendData(EventMsg eventMsg){
        RxBus.getInstance().post(eventMsg);
    }
    /**
     * @description 接受数据
     * @param consumer
     * @return
     * @author Administrator
     * @time 2020/4/10 17:50
     */
    public void receiveData(Consumer<EventMsg> consumer){
        transmitdisposable =  RxBus.getInstance().toObservable(EventMsg.class,
                AndroidSchedulers.mainThread(),
                AndroidSchedulers.mainThread(),
                consumer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getlayoutId(),container,false);
        unbinder = ButterKnife.bind(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(RxBus.getInstance().isObserver()){
            RxBus.getInstance().unregister(transmitdisposable);
        }
    }

    public <T>void sendHttpData(Observable observable, Class<T> clas, Consumer<T> consumer){
        observable.compose(ResponseTransformer.handleResult(clas)).compose(bindToLifecycle()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }
    public abstract int getlayoutId();
    public abstract void initView();
    public abstract void initData();
}
