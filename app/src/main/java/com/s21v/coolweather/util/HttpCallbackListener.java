package com.s21v.coolweather.util;

/**
 * Created by 弈 on 2015/9/23.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
