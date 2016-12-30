package com.xlmiao.coolweather.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XuLeMiao on 2016/12/25.
 */

public class HeWeather extends DataSupport {
    @SerializedName("HeWeather")
    private List<Weather> weather;
    public class Weather{
        private String status;

        private Basic basic;

        private AQI aqi;

        private Now now;

        private Suggestion suggestion;

        @SerializedName("daily_forecast")
        private List<ForeCast> foreCastsList;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Basic getBasic() {
            return basic;
        }

        public void setBasic(Basic basic) {
            this.basic = basic;
        }

        public AQI getAqi() {
            return aqi;
        }

        public void setAqi(AQI aqi) {
            this.aqi = aqi;
        }

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(Suggestion suggestion) {
            this.suggestion = suggestion;
        }

        public List<ForeCast> getForeCastsList() {
            return foreCastsList;
        }

        public void setForeCastsList(List<ForeCast> foreCastsList) {
            this.foreCastsList = foreCastsList;
        }
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


}
