package com.example.boleyuedu;

import org.reactivestreams.Subscriber;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus2 {
    private static final String TAG = RxBus2.class.getSimpleName();


    private static volatile RxBus2 mInstance;

    /**
     * 默认 bus ;
     */

    private Subject<Object> _mBus;

    /**
     * 背压
     */
    private FlowableProcessor<Object> _mBackPressureBus;


    private Map<Object, CompositeDisposable> mSubscription;


    private RxBus2() {

        _mBus = PublishSubject.create().toSerialized();

        _mBackPressureBus = PublishProcessor.create().toSerialized();
    }


    public static RxBus2 getInstance() {

        if (mInstance == null) {

            synchronized (RxBus2.class) {
                if (mInstance == null) {

                    mInstance = new RxBus2();
                }
            }
        }

        return mInstance;
    }


    /**
     * 发送普通事件
     */
    public void send(Object event) {

        _mBus.onNext(event);

    }


    /**
     * 发送背压事件
     */
    public void sendByBackPressure(Object event) {
        _mBackPressureBus.onNext(event);

    }


    /**
     * 接收普通事件
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {

        return _mBus.ofType(eventType);
    }


    /**
     * 接受背压事件
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType) {

        return _mBackPressureBus.ofType(eventType);
    }


    /**
     * 普通事件的处理
     */
    public <T> Disposable doSubscribe(Class<T> eventType, Consumer<T> next, Consumer<Throwable> error) {

        return toObservable(eventType)
                .compose(RxHelper.<T>IO_Main())
                .subscribe(next, error);
    }


    /**
     * 背压事件处理
     */
    public <T> Flowable doFlowable(Class<T> eventType, Subscriber<T> tSubscriber) {

        toFlowable(eventType)
                .onBackpressureLatest() //背压策略
                .compose(RxHelper.<T>IO_Main_Flowable())
                .subscribeWith(tSubscriber);

        return toFlowable(eventType);
    }


    /**
     * 是否有订阅者
     */
    public  boolean hasSubscribers(boolean isBackPressure) {

        if (!isBackPressure)
            return _mBus.hasObservers();
        else
            return _mBackPressureBus.hasSubscribers();
    }


    /**
     * 背压解除订阅
     */
    public void unSubscription(){

        _mBackPressureBus.onComplete();

    }


    /**
     * 添加订阅到集合(一般事件)
     */
    public void addSubscriptions(Object o, Disposable disposable) {

        if (mSubscription == null) {
            mSubscription = new HashMap<>();
        }

        String key = o.getClass().getName();

        if (mSubscription.get(key) != null) {
            mSubscription.get(key).add(disposable);

        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();

            compositeDisposable.add(disposable);
            mSubscription.put(key, compositeDisposable);
        }


    }


    /**
     * 解除订阅
     * 一般事件的解除订阅
     *
     * @param o
     */
    public void clearSubscriptions(Object o) {
        if (mSubscription == null) {
            return;
        }


        String key = o.getClass().getName();

        if (!mSubscription.containsKey(key)) {
            return;
        }


        if (mSubscription.get(key) != null) {
            mSubscription.get(key).dispose();

        }

        mSubscription.remove(key);
    }
}
