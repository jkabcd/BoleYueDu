package com.bole.basemodel.exception;

import com.bole.basemodel.bean.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class ResponseTransformer {
    public static <T> ObservableTransformer<T,T> handleResult() {

//          return new ObservableTransformer<T, T>() {
//              @Override
//              public ObservableSource<T> apply(Observable<T> upstream) {
//                  return  upstream.onErrorReturn(new Function<Throwable, ObservableSource<T>>() {
//                      @Override
//                      public ObservableSource<T> apply(Throwable throwable) throws Exception {
//                          return  Observable.error(throwable);
//                      }
//                  }).flatMap(new Function<T, ObservableSource<T>>() {
//                      @Override
//                      public ObservableSource<T> apply(T t) throws Exception {
//                          return Observable.just(t);
//                      }
//                  });
//              }
//          };

//        return new ObservableTransformer<R, T>() {
//            @Override
//            public ObservableSource<R> apply(Observable<T> upstream) {
//                return upstream.onErrorReturn(new ErrorResumeFunction());
//            }
//        };

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onErrorReturn(new <T>ErrorResumeFunction());
            }
        };

    }
    static class ErrorResumeFunction<T> implements Function<Throwable,  Response<T>> {


        @Override
        public  Response<T> apply(Throwable throwable) throws Exception {
            return new Response<>(1,"1");
        }
    }

}
