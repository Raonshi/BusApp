package com.example.demo.dto;

public class City {
    private String _cityCode;
    private String _cityName;

    public String get_cityName() {
        return _cityName;
    }

    public String get_cityCode(){
        return _cityCode;
    }

    public City(String cityCode, String cityName){
        this._cityName = cityName;
        this._cityCode = cityCode;
    }
}
