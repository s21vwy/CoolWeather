package com.s21v.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.s21v.coolweather.model.City;
import com.s21v.coolweather.model.County;
import com.s21v.coolweather.model.Province;

import java.util.ArrayList;

/**
 * Created by 弈 on 2015/9/22.
 */
public class CoolWeatherDB {
    //数据库名称
    private static final String DB_NAME = "CoolWeather";
    //数据库版本
    private static final int DB_VERSION = 1;
    //3个表
    private static final String PROVINCE_NAME = "Province";
    private static final String CITY_NAME = "City";
    private static final String COUNTY_NAME = "County";

    private static CoolWeatherDB coolWeatherDB;
    private CoolWeatherDBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        dbOpenHelper = new CoolWeatherDBOpenHelper(context,DB_NAME,null,DB_VERSION);
        db = dbOpenHelper.getWritableDatabase();
    }

    //单例方法
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if(coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //添加省信息
    public void saveProvince(Province province) {
        if(province != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            db.insert(PROVINCE_NAME,null,contentValues);
        }
    }

    //获得数据库里所有省的信息
    public ArrayList<Province> loadProvince() {
        ArrayList<Province> provinces = new ArrayList<>();
        Cursor cursor = db.query(PROVINCE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            }while (cursor.moveToNext());
        }
        return provinces;
    }

    //添加城市信息
    public void saveCity(City city) {
        if(city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name",city.getCityName());
            contentValues.put("city_code",city.getCityCode());
            contentValues.put("province_id",city.getProvinceId());
            db.insert(CITY_NAME,null,contentValues);
        }
    }

    //获得数据库里所有城市的信息
    public ArrayList<City> loadCity() {
        ArrayList<City> cities = new ArrayList<>();
        Cursor cursor = db.query(CITY_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cities.add(city);
            }while (cursor.moveToNext());
        }
        return cities;
    }

    //添加县的信息
    public void saveCounty(County county) {
        if(county != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name",county.getCountyName());
            contentValues.put("county_code",county.getCountCode());
            contentValues.put("city_id",county.getCityId());
            db.insert(COUNTY_NAME,null,contentValues);
        }
    }

    //获得数据库里所有县的信息
    public ArrayList<County> loadCounty() {
        ArrayList<County> counties = new ArrayList<>();
        Cursor cursor = db.query(COUNTY_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                counties.add(county);
            }while (cursor.moveToNext());
        }
        return counties;
    }
}
