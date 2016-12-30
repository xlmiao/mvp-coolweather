package com.xlmiao.coolweather.views;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xlmiao.coolweather.R;
import com.xlmiao.coolweather.contract.WeatherContract;
import com.xlmiao.coolweather.db.ForeCast;
import com.xlmiao.coolweather.presenter.WeatherPresenter;
/**
 * Created by XuLeMiao on 2016/12/25.
 * WeatherActivity 城市详情页
 */
public class WeatherActivity extends BaseActivity implements WeatherContract.View {
    public static final String weatherId = "WEATHER_ID";
    private String currentWeatherId = null;
    private WeatherContract.Presenter presenter;
    private Bundle savedInstanceState;

    Button navBtn;

    TextView cityTitleTV;

    TextView updateTimeTitleTV;

    ImageView bingPicImg;

    TextView degreeTV;

    TextView weatherInfoTV;

    LinearLayout forecastLayoutLL;

    TextView aqiTV;

    TextView pm25TV;

    TextView comfortTV;

    TextView carWashTV;

    TextView sportTV;

    SwipeRefreshLayout swipeRefreshLayout;

    DrawerLayout drawerLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initVariables(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        currentWeatherId = getIntent().getStringExtra(weatherId);
    }

    @Override
    public void initView() {
        navBtn = (Button) findViewById(R.id.nav_button);
        cityTitleTV = (TextView) findViewById(R.id.title_city);
        updateTimeTitleTV = (TextView) findViewById(R.id.title_update_time);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        degreeTV = (TextView) findViewById(R.id.degree_text);
        weatherInfoTV = (TextView) findViewById(R.id.weather_info_text);
        forecastLayoutLL = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiTV = (TextView) findViewById(R.id.aqi_text);
        pm25TV = (TextView) findViewById(R.id.pm25_text) ;
        carWashTV = (TextView) findViewById(R.id.car_wash_text);
        comfortTV = (TextView) findViewById(R.id.comfort_text);
        sportTV = (TextView) findViewById(R.id.sport_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        navBtn.setOnClickListener(navBtnOnClickListener);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        new WeatherPresenter(this);
    }

    private View.OnClickListener navBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            presenter.loadWeatherInfo();
        }
    };

    @Override
    public void loaderData() {
        presenter.start();
    }

    @Override
    public String loadWeatherId() {
        return currentWeatherId;
    }

    @Override
    public void setWeatherId(String weatherId) {
        this.currentWeatherId = weatherId;
    }


    TextView dateTV;

    TextView infoTV;

    TextView maxTV;

    TextView minTV;

    @Override
    public void showInfo() {
        cityTitleTV.setText(presenter.getCurrentBasic().getCityName());
        updateTimeTitleTV.setText(presenter.getCurrentBasic().getUpdate().getUpdateTime().split(" ")[1]);
        degreeTV.setText(presenter.getCurrentNow().getTemperature() + getResources().getString(R.string.celsius_name));
        weatherInfoTV.setText(presenter.getCurrentNow().getMore().getInfo());


        comfortTV.setText(presenter.getCurrentSuggestion().getComfort().getInfo());
        carWashTV.setText(presenter.getCurrentSuggestion().getCarWash().getInfo());
        sportTV.setText(presenter.getCurrentSuggestion().getSport().getInfo());

        forecastLayoutLL.removeAllViews();
        for (ForeCast foreCast : presenter.getCurrentForeCastList()) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayoutLL, false);
            dateTV = (TextView) view.findViewById(R.id.date_text);
            infoTV = (TextView) view.findViewById(R.id.info_text);
            maxTV = (TextView) view.findViewById(R.id.max_text);
            minTV = (TextView) view.findViewById(R.id.min_text);
            forecastLayoutLL.addView(view);
            dateTV.setText(foreCast.getDate());
            infoTV.setText(foreCast.getMore().getInfo());
            maxTV.setText(foreCast.getTemperature().getMax());
            minTV.setText(foreCast.getTemperature().getMin());
        }

        /**
         * 防空多重检测
         */
        aqiTV.setText(null != presenter.getCurrentAQI().getAqiCity() && null != presenter.getCurrentAQI().getAqiCity().getAqi()  ? presenter.getCurrentAQI().getAqiCity().getAqi() : getResources().getString(R.string.null_data));
        pm25TV.setText(null != presenter.getCurrentAQI().getAqiCity() && null != presenter.getCurrentAQI().getAqiCity().getPm25() ? presenter.getCurrentAQI().getAqiCity().getPm25() : getResources().getString(R.string.null_data));
    }

    @Override
    public void setBingPicImg(String bingPicPath) {
        Glide.with(this).load(bingPicPath).into(bingPicImg);
    }

    @Override
    public void setRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savedInstanceState.putString(currentWeatherId, currentWeatherId);
    }
}
