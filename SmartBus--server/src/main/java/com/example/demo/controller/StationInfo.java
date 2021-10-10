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

//버스 정류장에 대한 정보만을 받아오는 API

/* 기능
 * 1. 매개변수로 입력받은 정류장의 기본 정보 조회
 * 2. 클라이언트에서 위치를 입력받아 출발할 정류소를 조회  (List)
 * 3. 클라이언트에서 도착할 정류장 이름를 입력받아 도착지 정류장 정보 조회  (List)
 */


@RestController
public class StationInfo {

    public static String cityCode;
    public static String nodeNm;

    @RequestMapping(method = RequestMethod.GET, path = "/getStationList")
    JSONArray getStationList(@RequestParam String cityName, @RequestParam String nodeNm) throws InterruptedException {
        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.STATION_CITY_LIST);
        this.nodeNm = nodeNm;

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.STATION_NUMBER_LIST);
        TrafficAPIReceiver.start();

        Thread.sleep(1000);

        JSONArray stationList = DataCenter.Singleton().stationNumList;
        JSONArray result = new JSONArray();
        for(Object obj : stationList) {
            JSONObject json = (JSONObject) obj;
            if(json.get("nodenm").toString().contains(nodeNm)) {
                result.add(json);
            }

        }
        return result;
    }
}
