package com.bole.basemodel.mvpBase;

import android.hardware.ConsumerIrManager;
import android.util.Log;

import com.bole.basemodel.exception.ResponseTransformer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * **************************************
 * 项目名称：BoleYueDu
 *
 * @Author jack
 * 邮箱：1253865188@qq.com
 * 创建时间2020/4/11 11:41
 * 用途
 * **************************************
 */
public  class BaseModels<P> {
   public P mvpPresent;
    private CompositeDisposable compositeDisposable;

    public void setMvpPresent(P mvpPresent) {
        this.mvpPresent = mvpPresent;
    }
    public CompositeDisposable getCompositeSubscription() {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        return this.compositeDisposable;
    }
    public <T>void sendHttpData(Observable observable, Class<T> clas, Consumer<T> consumer){
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
       Disposable disposable = observable.compose(ResponseTransformer.handleResult(clas)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
       compositeDisposable.add(disposable);
    }
    public void clearHttps(){
        if (this.compositeDisposable != null) {
        compositeDisposable.clear();
        }
    }
}
