package com.xlmiao.coolweather.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.xlmiao.coolweather.R;
import com.xlmiao.coolweather.common.ActivityLifeCycleEvent;
import com.xlmiao.coolweather.common.HttpUtils;
import com.xlmiao.coolweather.common.SubscriberCallBack;
import com.xlmiao.coolweather.contract.WeatherInfoContract;
import com.xlmiao.coolweather.db.City;
import com.xlmiao.coolweather.db.County;
import com.xlmiao.coolweather.db.DbUtilty;
import com.xlmiao.coolweather.db.Province;
import com.xlmiao.coolweather.views.ChooseAreaFragment;
import com.xlmiao.coolweather.views.WeatherActivity;
import com.xlmiao.coolweather.views.WeatherMainActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;


/**
 * Created by XuLeMiao on 2016/12/21.
 * 该presenter 用于处理城市列表的数据获取和逻辑处理
 */

public class WeatherInfoPresenter extends BasePresenter implements WeatherInfoContract.Presenter {

    private static final int LEVEL_PROVINCE = 0;

    private static final int LEVEL_CITY = 1;

    private static final int LEVEL_COUNTY = 2;

    private  int currenLevel;

    private City selectedCity;

    private Province selectedProvince;


    private ChooseAreaFragment mFragment;

    /**
     *  可使用AppManager.getAppManager().currentActivity() 获取activity
     *  当是为了体验mvp模式，所以没有添加
     */
    private Activity mActivity;

    public WeatherInfoPresenter(WeatherInfoContract.View view){
        this.mFragment = (ChooseAreaFragment)view;
        this.mActivity = mFragment.activity;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        attachView(mFragment);
        loadWeatherPrivinceInfo();
    }

    /**
     * 加载 Province 信息 先从数据库中查询 再从服务器上查询
     */
    @Override
    public void loadWeatherPrivinceInfo() {
        currenLevel = LEVEL_PROVINCE;
        queryProvince();
    }

    /**
     * 加载 City 信息 先从数据库中查询 再从服务器上查询
     * @param data
     */
    @Override
    public void loadWeatherCityInfo(Object data) {
        currenLevel = LEVEL_CITY;
        selectedProvince = (Province) data;
        queryCity((Province) data);

    }

    /**
     * 加载 county 信息 先从数据库中查询 再从服务器上查询
     * @param data
     */
    @Override
    public void loadWeatherCountyInfo(Object data) {
        currenLevel = LEVEL_COUNTY;
        selectedCity = (City) data;
        quetyCounty((City) data);
    }

    private void queryProvince() {
           List<Province> model = DataSupport.findAll(Province.class);
           if(model.size() > 0){
               mFragment.showInfo(model);
           }else{
               Observable ob = iHttpService.getPrivince();
               HttpUtils.getHttpUtils().toSubscribe(ob, ActivityLifeCycleEvent.DESTROY, getActivityLifeCycleEvent(), new SubscriberCallBack<List<Province>>(mFragment.activity) {
                   @Override
                   public void onCancelProgress() {
                   }

                   @Override
                   public void onSuccess(List<Province> provinces) {
                       if(provinces.size() > 0){
                           DbUtilty.handleProvinceResponse(provinces);
                           mFragment.showInfo(provinces);
                       }else{
                           Toast.makeText(mActivity,"provinces为空",Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(String msg) {

                   }

               });
           }
    }

    private void queryCity(Province province) {
        List<City> model = DataSupport.where("provinceCode = ?", String.valueOf(province.getProvinceCode())).find(City.class);
        if(model.size() > 0){
            mFragment.showInfo(model);
        }else{
            Observable ob = iHttpService.getCity(selectedProvince.getProvinceCode());
            HttpUtils.getHttpUtils().toSubscribe(ob, ActivityLifeCycleEvent.DESTROY, getActivityLifeCycleEvent(), new SubscriberCallBack<List<City>>(mFragment.activity) {
                @Override
                public void onCancelProgress() {

                }

                @Override
                public void onSuccess(List<City> cities) {
                    if(cities.size() > 0){
                        DbUtilty.handleCityResponse(cities,selectedProvince.getProvinceCode());
                        mFragment.showInfo(DataSupport.where("provinceCode = ?", String.valueOf(province.getProvinceCode())).find(City.class));
                    }else{
                        Toast.makeText(mActivity,"cities为空",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String msg) {

                }

            });
        }
    }

    private void quetyCounty(City city) {
        List<County> model = DataSupport.where("cityCode = ?", String.valueOf(city.getCityCode())).find(County.class);
        if(model.size() > 0){
            mFragment.showInfo(model);
        }else{
            Observable ob = iHttpService.getCounty(selectedProvince.getProvinceCode(),selectedCity.getCityCode());
            HttpUtils.getHttpUtils().toSubscribe(ob, ActivityLifeCycleEvent.DESTROY, getActivityLifeCycleEvent(), new SubscriberCallBack<List<County>>(mFragment.activity) {
                @Override
                public void onCancelProgress() {

                }

                @Override
                public void onSuccess(List<County> counties) {
                    if(counties.size() > 0){
                        DbUtilty.handleCountyResponse(counties, selectedCity.getCityCode());
                        mFragment.showInfo(DataSupport.where("cityCode = ?", String.valueOf(city.getCityCode())).find(County.class));
                    }else{
                        Toast.makeText(mActivity,"counties为空",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String msg) {

                }

            });
        }
    }

    @Override
    public void callBack() {
        if(currenLevel == LEVEL_CITY){
            loadWeatherPrivinceInfo();
            mFragment.changeCallBackBtn();
            mFragment.changeTitle(mFragment.getResources().getString(R.string.china_name));
        }

        if(currenLevel == LEVEL_COUNTY){
            loadWeatherCityInfo(selectedProvince);
            mFragment.changeTitle(selectedProvince.getProvinceName());
        }

    }

    public PublishSubject<ActivityLifeCycleEvent> getActivityLifeCycleEvent() {
        PublishSubject<ActivityLifeCycleEvent> lifecycleEventPublishSubject;

        if(mActivity instanceof WeatherMainActivity){
            lifecycleEventPublishSubject = ((WeatherMainActivity)mActivity).lifecycleEventPublishSubject;
        }else{
            lifecycleEventPublishSubject =((WeatherActivity)mActivity).lifecycleEventPublishSubject;
        }
        return lifecycleEventPublishSubject;
    }
}
