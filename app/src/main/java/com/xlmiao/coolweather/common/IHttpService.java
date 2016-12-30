package com.xlmiao.coolweather.common;

import com.xlmiao.coolweather.db.City;
import com.xlmiao.coolweather.db.County;
import com.xlmiao.coolweather.db.HeWeather;
import com.xlmiao.coolweather.model.MainMode;
import com.xlmiao.coolweather.db.Province;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XuLeMiao on 2016/12/22.
 */

public interface IHttpService {
    String API_SERVER_URL = "http://guolin.tech/";
    String APP_SERVER_URL = "http://www.weather.com.cn/";
    String APP_KEY = "915a18c7db5845cda5422d15c68c6775";
    String BING_PIC_PATH = "http://cn.bing.com/az/hprichbg/rb/SouthamptonCommon_ZH-CN8102690225_1920x1080.jpg";

    @GET("/api/china")
    Observable<List<Province>> getPrivince();

    @GET("/api/china/{provinceId}")
    Observable<List<City>> getCity(@Path("provinceId") int provinceId);

    @GET("/api/china/{provinceId}/{cityId}")
    Observable<List<County>> getCounty(@Path("provinceId") int provinceId, @Path("cityId") int cityId);

    @GET("/api/weather")
    Observable<HeWeather> getWeather(@Query("cityid") String cityId, @Query("key") String key);

    @GET("/api/bing_pic")
    Observable<Object> getbingPic();

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<MainMode> loadDataByRetrofitRxjava(@Path("cityId") String cityId);


}
