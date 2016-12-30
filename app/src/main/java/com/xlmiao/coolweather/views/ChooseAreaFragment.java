package com.xlmiao.coolweather.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xlmiao.coolweather.R;
import com.xlmiao.coolweather.R2;
import com.xlmiao.coolweather.contract.WeatherInfoContract;
import com.xlmiao.coolweather.db.City;
import com.xlmiao.coolweather.db.County;
import com.xlmiao.coolweather.db.Province;
import com.xlmiao.coolweather.presenter.WeatherInfoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XuLeMiao on 2016/12/23.
 * Fragment 城市列表展示
 * 因为多次用到 所以写成Fragment形式
 */

public class ChooseAreaFragment extends BaseFragment implements WeatherInfoContract.View{


    //@BindView(R.id.title_text)
    TextView mTitleText;

    //@BindView(R.id.back_button)
    Button mBackBtn;

    //@BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private WeatherInfoPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.choose_area;
    }

    /**
     * ButterKnife.bind(this,view); 使用过程中要反复加载R 否则报错 暂停使用
     * @param view
     * @param savedInstanceState
     */
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mBackBtn = (Button) view.findViewById(R.id.back_button);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // ButterKnife.bind(this,view);
        mTitleText.setText(getResources().getString(R.string.china_name));
        mBackBtn.setVisibility(View.INVISIBLE);
        mBackBtn.setOnClickListener(mBackBtnClickListener);
        setupLinearLayoutRecyclerView();
        new WeatherInfoPresenter(this);
        mPresenter.start();
    }

    public void setupLinearLayoutRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private View.OnClickListener mBackBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPresenter.callBack();
        }
    };


    /**
     * RecyclerView 展示城市列表数据即点击
     * @param dataList
     */
    @Override
    public void showInfo(List<? extends Object> dataList) {
        ChooseAreaFragmentAdapter chooseAreaFragmentAdapter = new ChooseAreaFragmentAdapter((List<Object>) dataList);
        chooseAreaFragmentAdapter.setOnItemClickListener(new ChooseAreaFragmentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                if(data instanceof Province){
                    mTitleText.setText(((Province)data).getProvinceName());
                    mBackBtn.setVisibility(View.VISIBLE);
                    mPresenter.loadWeatherCityInfo(data); //加载城市
                }

                if(data instanceof City){
                    mTitleText.setText(((City)data).getCityName());
                    mBackBtn.setVisibility(View.VISIBLE);
                    mPresenter.loadWeatherCountyInfo(data); //加载镇
                }

                if(data instanceof County){ //跳转处理
                    if(activity instanceof WeatherMainActivity){
                        startWeatherActivity(WeatherActivity.class,((County) data).getWeatherId());
                    }else if(activity instanceof WeatherActivity){ //侧滑时点击逻辑
                        ((WeatherActivity) activity).drawerLayout.closeDrawers();
                        ((WeatherActivity) activity).setWeatherId(((County) data).getWeatherId());
                        ((WeatherActivity) activity).loaderData();

                    }

                }
            }
        });
        mRecyclerView.setAdapter(chooseAreaFragmentAdapter);
    }

    private void startWeatherActivity(Class<WeatherActivity> weatherActivityClass, String weatherId) {
        startActivity(new Intent(activity, weatherActivityClass).putExtra(WeatherActivity.weatherId, weatherId));
        activity.finish();
    }

    @Override
    public String loadWeatherId() {
        return null;
    }

    @Override
    public void changeCallBackBtn() {
        mBackBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changeTitle(String titleName) {
        mTitleText.setText(titleName);
    }

    @Override
    public void setPresenter(WeatherInfoContract.Presenter presenter) {
        this.mPresenter = (WeatherInfoPresenter)presenter;
    }
}
