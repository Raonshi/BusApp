package com.example.demo.controller;

import com.example.demo.datacenter.DataCenter;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.TrafficAPIReceiver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Optional;

//공용 오퍼레이션

/*기능
* 1. 도시 이름 -> 도시 코드 파싱
* 2. 정류장 id -> 정류장 이름 파싱
* 3. 노선 id -> 노선 번호 파싱
*/

public class PublicOperation {

    public String cityCode;
    public String nodeNm;

    /*cityName to cityCode*/
    public static String getCityCode(String cityName, @Nullable APIHandler type) throws InterruptedException {
        type = Optional.ofNullable(type).orElse(APIHandler.ROUTE_CITY_LIST);

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(type);
        TrafficAPIReceiver.start();

        Thread.sleep(500);

        JSONArray list = DataCenter.Singleton().cityList;

        for(Object obj : list) {
            JSONObject json = (JSONObject) obj;
            if(json.get("cityName").toString().contains(cityName)){
                return json.get("cityCode").toString();
            }
        }
        return "failed";
    }


    /*nodeNm to nodeId*/
    public ArrayList<String> getNodeId(String cityName, String nodeNm) throws InterruptedException {
        this.cityCode = getCityCode(cityName, APIHandler.STATION_CITY_LIST);
        this.nodeNm = nodeNm;

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.STATION_NUMBER_LIST);
        TrafficAPIReceiver.start();

        Thread.sleep(500);

        JSONArray list = DataCenter.Singleton().stationNumList;

        //결과 리스트 반환
        ArrayList<String> result = new ArrayList<>();

        for(Object obj : list) {
            JSONObject json = (JSONObject) obj;
            if(json.get("nodenm").toString().contains(nodeNm)){
                result.add(json.get("nodeid").toString());
            }
        }

        return result;
    }

    /*routeNo to routeId*/
}
