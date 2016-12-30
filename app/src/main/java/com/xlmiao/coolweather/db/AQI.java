package com.xlmiao.coolweather.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by XuLeMiao on 2016/12/25.
 */

public class AQI extends DataSupport{
    @SerializedName("city")
    private AQICity aqiCity;

    public AQICity getAqiCity() {
        return aqiCity;
    }

    public void setAqiCity(AQICity aqiCity) {
        this.aqiCity = aqiCity;
    }

    public class AQICity{
        private String aqi;

        private String pm25;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }
    }


}
