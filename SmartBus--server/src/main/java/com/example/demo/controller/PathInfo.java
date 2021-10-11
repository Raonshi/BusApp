package com.example.demo.controller;

import com.example.demo.datacenter.DataCenter;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.TrafficAPIReceiver;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//경로 연산을 제공하는 API

/* 기능
 * 1. 클라이언트에서 출발지, 목적지가 설정되었으면 직통 및 환승 경로를 찾아 출력
 */

@RestController
public class PathInfo {

    public static String cityCode;
    public static String deptId;
    public static String destId;

    @RequestMapping(method = RequestMethod.GET, path = "/getWayList")
    JSONArray getWayList(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.ROUTE_CITY_LIST);
        this.deptId = deptId;
        this.destId = destId;

        DataCenter.Singleton().wayList.clear();

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.FIND_WAY);
        TrafficAPIReceiver.start();

        Thread.sleep(3000);


        return DataCenter.Singleton().wayList;
    }
}
