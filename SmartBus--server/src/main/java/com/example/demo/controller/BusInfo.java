package com.example.demo.controller;

import com.example.demo.datacenter.DataCenter;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.TrafficAPIReceiver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//버스 노선에 대한 정보만을 받아오는 API

/* 기능
 * 1. 매개변수로 입력받은 버스노선에 대한 기본 정보
 * 2. 매개변수로 입력받은 버스노선에 대한 상세 정보
 * 3. 매개변수로 입력받은 버스노선 별로 현재 운행중인 버스의 gps 정보 출력 (List)
 * 4. 매개변수로 입력받은 버스노선에 대해 경유하는 모든 정류장을 출력. (List)
 */


@RestController
public class BusInfo {

    public static String cityCode;
    public static String routeId;
    public static String routeNo;


    @RequestMapping(method = RequestMethod.GET, path = "/getBusList")
    JSONArray getBusList(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {

        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.ROUTE_CITY_LIST);
        this.routeNo = routeNo;

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.ROUTE_NUMBER_LIST);
        TrafficAPIReceiver.start();

        Thread.sleep(1000);

        JSONArray routeList = DataCenter.Singleton().routeNumList;

        JSONArray result = new JSONArray();

        for(Object obj : routeList) {
            JSONObject json = (JSONObject) obj;
            if(json.get("routeno").toString().contains(routeNo)){
                result.add(json);
            }
        }
        return result;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/getBusLocation")
    JSONArray getBusLocation(@RequestParam String cityName, @RequestParam String routeId) throws InterruptedException{
        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.LOCATION_CITY_LIST);
        this.routeId = routeId;

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.LOCATION_BUS_LIST);
        TrafficAPIReceiver.start();
        Thread.sleep(1000);

        return DataCenter.Singleton().routeLocationList;
    }
}
