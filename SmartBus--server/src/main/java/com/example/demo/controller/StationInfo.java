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
 * 1. 매개변수로 입력받은 정류장의 세부 정보 조회 (list)
 * 2. 클라이언트에서 위치를 입력받아 출발할 정류소를 조회  (List)
 * 3. 클라이언트에서 도착할 정류장 이름를 입력받아 도착지 정류장 정보 조회  (List)
 */


@RestController
public class StationInfo {

    public static String cityCode;
    public static String nodeNm;

    //매개변수로 입력받은 정류장의 세부 정보 조회 (list)
    //정류장 이름 -> 정류장 id로 파싱된 Arraylists는 DataCenter에 저장
    @RequestMapping(method = RequestMethod.GET, path = "/getStationList")
    JSONArray getStationList(@RequestParam String cityName, @RequestParam String nodeNm) throws InterruptedException {

        PublicOperation pub = new PublicOperation();

        DataCenter.Singleton().stationIdList.clear();
        DataCenter.Singleton().finalStationList.clear();

        this.cityCode = pub.getCityCode(cityName, APIHandler.STATION_CITY_LIST);
        this.nodeNm = nodeNm;

        DataCenter.Singleton().stationIdList = pub.getNodeId(cityName, nodeNm);

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.STATION_NUMBER_LIST);
        TrafficAPIReceiver.start();

        Thread.sleep(1000);

        JSONArray stationList = DataCenter.Singleton().stationNumList;
        for(Object obj : stationList) {
            JSONObject json = (JSONObject) obj;
            if(json.get("nodenm").toString().contains(nodeNm)) {
                DataCenter.Singleton().finalStationList.add(json);
            }

        }

        return DataCenter.Singleton().finalStationList;
    }

}
