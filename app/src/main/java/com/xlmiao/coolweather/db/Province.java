package com.xlmiao.coolweather.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;


/**
 * Created by XuLeMiao on 2016/12/22.
 */
public class Province extends DataSupport{

    private int id;

    @SerializedName("name")
    private String provincename;

    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provincename;
    }

    public void setProvinceName(String provincename) {
        this.provincename = provincename;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
