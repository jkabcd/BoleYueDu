package com.bole.basemodel;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.tools.RxBus;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseActivity  extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {
    private Disposable disposable;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();//生命周期管理类

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }
    public <T,R> void  liftCycle(Observable observable, ActivityEvent composer, Consumer consumer){
        observable.subscribeOn(Schedulers.io()).compose(bindUntilEvent(composer)).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if(RxBus.getInstance().isObserver()){
            RxBus.getInstance().unregister(disposable);
        }
        super.onDestroy();
    }
    public void sendData(EventMsg eventMsg){
       RxBus.getInstance().post(eventMsg);
    }
  public void receiveData(Consumer<EventMsg> consumer){
      disposable =  RxBus.getInstance().toObservable(EventMsg.class,
                AndroidSchedulers.mainThread(),
                AndroidSchedulers.mainThread(),
                consumer);
  }
}
