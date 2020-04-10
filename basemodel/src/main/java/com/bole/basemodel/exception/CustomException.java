package com.bole.basemodel.exception;

import com.bole.basemodel.bean.Response;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class CustomException {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static int handleException(Throwable e) {
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //解析错误
            return PARSE_ERROR;
        } else if (e instanceof ConnectException) {
            //网络错误
            return NETWORK_ERROR;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            return NETWORK_ERROR;
        } else {
            //未知错误
            return UNKNOWN;
        }
    }
}
