package com.xlmiao.coolweather.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

import com.google.gson.Gson;
import com.xlmiao.coolweather.db.HeWeather.Weather;
import com.xlmiao.coolweather.views.WeatherActivity;

/**
 * Created by XuLeMiao on 2016/12/23.
 */

public class DbUtilty {

    public static void handleProvinceResponse(List<Province> provinces) {
        for(Province province:provinces){
            Province dbProvince = new Province();
                     dbProvince.setProvinceCode(province.getId());
                     dbProvince.setProvinceName(province.getProvinceName());
                     dbProvince.save();
        }
    }

    public static void handleCityResponse(List<City> cities, int provinceCode) {
        for(City city:cities){
            City dbCity = new City();
            dbCity.setCityCode(city.getId());
            dbCity.setCityName(city.getCityName());
            dbCity.setProvinceCode(provinceCode);
            dbCity.save();
        }
    }

    public static void handleCountyResponse(List<County> counties, int cityCode) {
        for(County county:counties){
            County dbCounty = new County();
            dbCounty.setCountyName(county.getCountyName());
            dbCounty.setWeatherId(county.getWeatherId());
            dbCounty.setCityCode(cityCode);
            dbCounty.save();
        }
    }
}
