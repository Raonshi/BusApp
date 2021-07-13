package com.example.demo.data;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public class DataController {

    /**
     * 싱글톤
     */
    private static DataController instance = null;
    public static DataController Singleton(){
        if(instance == null){
            instance = new DataController();
        }
        return instance;
    }

    /**
     * <p>국토교통부-버스노선정보 API를 통해 얻은 데이터를 저장하는 ArrayList</p>
     */
    //public ArrayList<City> cityList = new ArrayList<>();
    //public ArrayList<RouteInfo> routeInfoList = new ArrayList<>();
    public ArrayList<RouteNum> routeNumList = new ArrayList<>();
    public ArrayList<AccessStation> accessStationList = new ArrayList<>();
    public JSONArray cityList = new JSONArray();
    public JSONArray routeInfoList = new JSONArray();

}