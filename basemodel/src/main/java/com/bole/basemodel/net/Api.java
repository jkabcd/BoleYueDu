package com.bole.basemodel.net;

import com.bole.basemodel.bean.Msg;
import com.bole.basemodel.bean.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
//   @Headers("Cache-Control: max-age=1000")
    @GET(""+"/booo")
    Observable<Msg> getkefu();
}
