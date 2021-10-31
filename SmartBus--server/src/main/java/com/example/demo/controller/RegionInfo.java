package com.example.demo.controller;

import com.example.demo.datacenter.DataCenter;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.DustAPIReceiver;
import com.example.demo.utils.TrafficAPIReceiver;
import com.example.demo.utils.WeatherAPIReceiver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.BooleanSupplier;

//지역에 대한 정보만을 받아오는 API

/* 기능
 * 1. 본 애플리케이션의 서비스를 지원하는 도시 인지 확인  (List)
 * 2. 클라이언트에서 위치(위도, 경도)를 입력받아 기상 정보를 조회  (List)
 * 3. 클라이언트에서 시, 도를 입력받아 미세먼지 정보 조회 (list)
 */



@RestController
public class RegionInfo {

    public static String latitude;
    public static String longitude;
    //public static String sido;

    //본 애플리케이션의 서비스를 지원하는 도시 인지 확인
    @RequestMapping(method = RequestMethod.GET, path = "/getRegionInfo")
    public JSONArray getRegionInfo(@RequestParam String cityName) throws InterruptedException {

        TrafficAPIReceiver receiver = new TrafficAPIReceiver(APIHandler.ROUTE_CITY_LIST);
        receiver.start();

        Thread.sleep(1000);

        JSONArray cityList = DataCenter.Singleton().cityList;
        JSONArray result = new JSONArray();

        for(Object obj : cityList) {
            JSONObject json = (JSONObject) obj;
            if(json.get("cityName").toString().contains(cityName)) {
                result.add(json);
            }
        }

        return result;
    }


    //클라이언트에서 위치(위도, 경도)를 입력받아 기상 정보를 조회

    @RequestMapping(method = RequestMethod.GET, path = "/getWeatherInfo")
    public JSONArray getWeatherInfo(@RequestParam String latitude, @RequestParam String longitude) throws InterruptedException {
        this.latitude = latitude;
        this.longitude = longitude;

        WeatherAPIReceiver receiver = new WeatherAPIReceiver(APIHandler.REGION_WEATHER_INFO);
        receiver.start();

        Thread.sleep(1500);


        return DataCenter.Singleton().weatherList;
    }


    //클라이언트에서 위치(위도, 경도)를 입력받아 미세먼지 정보 조회

    @RequestMapping(method = RequestMethod.GET, path = "/getDustInfo")
    public JSONArray getDustInfo(@RequestParam String latitude,  @RequestParam String longitude) throws InterruptedException {
        this.latitude = latitude;
        this.longitude = longitude;

        DustAPIReceiver receiver = new DustAPIReceiver(APIHandler.REGION_DUST_INFO);
        receiver.start();



        Thread.sleep(2500);

        return DataCenter.Singleton().dustList;
    }

}
