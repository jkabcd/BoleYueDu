package com.bole.basemodel;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bole.basemodel.bean.EventMsg;
import com.bole.basemodel.exception.ResponseTransformer;
import com.bole.basemodel.mvpBase.BasePresenter;
import com.bole.basemodel.mvpBase.IBaseView;
import com.bole.basemodel.tools.RxBus;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.lang.reflect.ParameterizedType;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseActivity<I,P extends BasePresenter>  extends AppCompatActivity implements LifecycleProvider<ActivityEvent>, IBaseView<P>{
    private Disposable disposable;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();//生命周期管理类
    protected P presenter;
    protected I Iview;
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
        Iview = (I) this;
        setPresenterAndView();
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
  
    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if(RxBus.getInstance().isObserver()){
            RxBus.getInstance().unregister(disposable);
        }
        super.onDestroy();
    }
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
      disposable =  RxBus.getInstance().toObservable(EventMsg.class,
                AndroidSchedulers.mainThread(),
                AndroidSchedulers.mainThread(),
                consumer);
  }
    /**
     * @description 网络请求
     * @param
     * @return
     * @author Administrator
     * @time 2020/4/10 17:49
     */
  public <T>void sendHttpData(Observable observable,Class<T> clas,Consumer<T> consumer){
        observable.compose(ResponseTransformer.handleResult(clas)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
  }


    /**
     * @description 实例化Presenter
     * @param
     * @return void
     * @author Administrator
     * @time 2020/4/11 12:45
     */

    public void setPresenterAndView() {
        try {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<P> clazz = (Class<P>) pt.getActualTypeArguments()[0];
            presenter = clazz.newInstance();
            presenter.setMvpView(Iview);
        } catch (Exception e) {
            Log.e("BaseActivity","P请继承BasePresent,否则无法实例化");
            e.printStackTrace();
        }
    }
}
