package com.bole.basemodel.net;

import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.bean.Response;

import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
   @Headers("Cache-Control: no_cache,max-age=0")
    @POST("abc"+"/getkefu")
    Observable<Response<Msg>> getkefu();
}
