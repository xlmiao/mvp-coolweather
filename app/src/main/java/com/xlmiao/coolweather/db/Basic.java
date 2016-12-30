package com.xlmiao.coolweather.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by XuLeMiao on 2016/12/25.
 */

public class Basic extends DataSupport {
    @SerializedName("city")
    private String cityName;

    @SerializedName("id")
    private String weatheId;

    private Update update;

    public class Update{
        @SerializedName("loc")
        private String updateTime;

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatheId() {
        return weatheId;
    }

    public void setWeatheId(String weatheId) {
        this.weatheId = weatheId;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
