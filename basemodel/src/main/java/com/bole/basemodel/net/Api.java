package com.bole.basemodel.net;

import com.bole.basemodel.bean.Msg;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface Api {

    @POST("abc"+"/getkefu")
    Observable<Msg> getkefu();
}
