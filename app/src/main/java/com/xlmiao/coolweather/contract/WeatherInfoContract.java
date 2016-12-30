package com.xlmiao.coolweather.contract;


import java.util.List;


/**
 * Created by XuLeMiao on 2016/12/21.
 * 城市列表页契约接口 含 view 和其presenter 的使用方法
 */

public interface WeatherInfoContract {
    interface View extends BaseView<Presenter>{
        void showInfo(List<? extends Object> provinces);
        String loadWeatherId();
        void changeTitle(String titleName);
        void changeCallBackBtn();
    }

    interface Presenter {
        void start();
        void loadWeatherPrivinceInfo();
        void loadWeatherCityInfo(Object data);
        void loadWeatherCountyInfo(Object data);
        void callBack();
    }

}
