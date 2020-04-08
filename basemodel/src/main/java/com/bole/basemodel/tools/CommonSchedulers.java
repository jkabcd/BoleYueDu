package com.bole.basemodel.tools;

import android.graphics.drawable.Drawable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CommonSchedulers {
    public static <String> ObservableTransformer<Integer, java.lang.String> io2main() {
        return new ObservableTransformer<Integer, java.lang.String>() {
            @Override
            public ObservableSource<java.lang.String> apply(Observable<Integer> upstream) {

                return upstream.map(new Function<Integer, java.lang.String>() {
                    @Override
                    public java.lang.String apply(Integer integer) throws Exception {
                        return "";
                    }
                });
//                return upstream.subscribeOn(Schedulers.io())
//
//                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }
public static <Integer> ObservableTransformer<Drawable,java.lang.String> ddf(){
        return new ObservableTransformer<Drawable, java.lang.String>() {
            @Override
            public ObservableSource<java.lang.String> apply(Observable<Drawable> upstream) {
                return upstream.map(new Function<Drawable, java.lang.String>() {
                    @Override
                    public java.lang.String apply(Drawable drawable) throws Exception {
                        return "";
                    }
                });
            }
        };
    }

    public static <T,R> ObservableTransformer<T, R> io2maind() {
        return new ObservableTransformer<T, R>() {
            @Override
            public ObservableSource<R> apply(Observable<T> upstream) {
                     return upstream.map(new Function<T, R>() {
                         @Override
                         public R apply(T t) throws Exception {
                             return null;
                         }
                     });
//                return upstream.subscribeOn(Schedulers.io())
//
//                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
