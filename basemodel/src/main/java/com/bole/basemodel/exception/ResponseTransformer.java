package com.bole.basemodel.exception;

import android.util.Log;

import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.bean.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class ResponseTransformer<T> {
    public  static  <T> ObservableTransformer<T,T> handleResult(Class<T> tClass) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onErrorReturn(new Function<Throwable, T>() {
                    @Override
                    public T apply(Throwable throwable) throws Exception {
                        T newCreatedDate;
                        Response<T> responseData = null;
                        try {
                            newCreatedDate = tClass.newInstance();
                            responseData  = (Response<T>)newCreatedDate;
                            responseData.setCode(CustomException.handleException(throwable));
                            responseData.setMsg(throwable.getMessage());
                        }catch (Exception e){
                            Log.e("ResponseTransformer","ResponseTransformer实例化出错"+e.getMessage());
                        }
                        return (T)responseData;
                    }
                }).flatMap(new Function<T, ObservableSource<T>>(){
                    @Override
                    public ObservableSource<T> apply(T t) throws Exception {
                        return Observable.just(t);
                    }
                });
            }
        };
    }


}
