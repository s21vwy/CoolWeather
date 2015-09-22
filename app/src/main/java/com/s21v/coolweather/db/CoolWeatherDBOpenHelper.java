package com.s21v.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 弈 on 2015/9/22.
 */
public class CoolWeatherDBOpenHelper extends SQLiteOpenHelper {
    //省
    private static final String BUILD_PROVINCE = "create table Province(" +
            "id integer primary key autoincrement," +
            "province_name text," +
            "province_code text)";
    //市
    private static final String BUILD_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "city_code text," +
            "province_id integer)";
    //县
    private static final String BUILD_COUNTY = "create table County(" +
            "id integer primary key autoincrement," +
            "county_name text," +
            "county_code text," +
            "city_id integer)";

    public CoolWeatherDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BUILD_PROVINCE);
        db.execSQL(BUILD_CITY);
        db.execSQL(BUILD_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists County");
        db.execSQL("drop table if exist City");
        db.execSQL("drop table if exist.Province");
        onCreate(db);
    }
}
