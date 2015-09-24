package com.s21v.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.s21v.coolweather.R;
import com.s21v.coolweather.util.HttpCallbackListener;
import com.s21v.coolweather.util.HttpUtil;
import com.s21v.coolweather.util.Utility;

/**
 * Created by 弈 on 2015/9/24.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText,publicText,weatherDespText,temp1Text,temp2Text,currentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publicText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            //有天气代号就去查询
            publicText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            showWeather();
        }
    }

    private void queryWeatherCode(String countyCode) {
        Log.i("queryWeatherCode","countyCode:"+countyCode);
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        Log.i("queryWeatherCode","address:"+address);
        queryFromServer(address, "countyCode");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        Utility.handleWeatherResponse(WeatherActivity.this, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(WeatherActivity.this, "解析出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWeather() {
        SharedPreferences sharedPreferences = getSharedPreferences("weather", Context.MODE_PRIVATE);
        cityNameText.setText(sharedPreferences.getString("city_name",""));
        weatherDespText.setText(sharedPreferences.getString("weatherDesp",""));
        temp1Text.setText(sharedPreferences.getString("temp1",""));
        temp2Text.setText(sharedPreferences.getString("temp2",""));
        publicText.setText("今天"+sharedPreferences.getString("publishTime","")+"发布");
        currentDateText.setText(sharedPreferences.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    private void queryWeatherInfo(String weatherCode) {
        Log.i("queryWeatherCode","weatherCode:"+weatherCode);
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        Log.i("queryWeatherCode","address:"+address);
        queryFromServer(address,"weatherCode");
    }

    @Override
    public void onClick(View v) {

    }
}
