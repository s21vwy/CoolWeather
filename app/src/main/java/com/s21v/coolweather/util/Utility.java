package com.s21v.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.s21v.coolweather.db.CoolWeatherDB;
import com.s21v.coolweather.model.City;
import com.s21v.coolweather.model.County;
import com.s21v.coolweather.model.Province;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 弈 on 2015/9/23.
 */
public class Utility {
    //解析省的数据
    public synchronized static boolean handleProvinceResponse(
            CoolWeatherDB coolWeatherDB, String response) {
        if(!TextUtils.isEmpty(response)) {
            String[] allProvinceString = response.split(",");
            if(allProvinceString!=null && allProvinceString.length>0) {
                for(String s : allProvinceString) {
                    String[] provinceString = s.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(provinceString[0]);
                    province.setProvinceName(provinceString[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //解析市的数据
    public synchronized static boolean handleCityResponse(
            CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCityString = response.split(",");
            if(allCityString!=null && allCityString.length>0) {
                for (String s : allCityString) {
                    String[] cityString = s.split("\\|");
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setCityCode(cityString[0]);
                    city.setCityName(cityString[1]);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //解析县的数据
    public synchronized static boolean handleCountyResponse(
            CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCountyString = response.split(",");
            if(allCountyString!=null && allCountyString.length>0) {
                for (String s : allCountyString) {
                    String[] countyString = s.split("\\|");
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountCode(countyString[0]);
                    county.setCountyName(countyString[1]);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据,并将解析出的数据存储到本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            String weatherCode = weatherInfo.getString("cityid");
            saveWeatherInfo(context,cityName,temp1,temp2,weatherDesp,publishTime,weatherCode);
        } catch (Exception e) {
            Log.i("handleWeatherResponse",e.getMessage());
        }
    }

    /**
     * 将服务器返回的所有天气存储到SharePerferences文件中
     * @param context
     * @param cityName
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    private static void saveWeatherInfo(Context context, String cityName, String temp1,
                                        String temp2, String weatherDesp, String publishTime, String weatherCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = context.getSharedPreferences("weather",Context.MODE_PRIVATE).edit();
        editor.putString("city_name",cityName);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weatherDesp",weatherDesp);
        editor.putString("publishTime",publishTime);
        editor.putString("weatherCode",weatherCode);
        editor.putBoolean("city_selected", true);
        editor.putString("current_date", simpleDateFormat.format(new Date()));
        editor.commit();
    }
}
