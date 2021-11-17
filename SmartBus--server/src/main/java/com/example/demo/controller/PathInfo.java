package com.example.demo.controller;

import com.example.demo.datacenter.DataCenter;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.TrafficAPIReceiver;
import com.example.demo.utils.TrafficAPIReceiver2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//경로 연산을 제공하는 API

/* 기능
 * 1. 클라이언트에서 출발지, 목적지가 설정되었으면 직통 및 환승 경로를 찾아 출력
 * (직통 : 버스 노선 출력, 환승 : 정류장 경로 출력)
 */

@RestController
public class PathInfo {

    public static String cityCode;
    public static String deptId;
    public static String destId;


    //클라이언트에서 출발지, 목적지가 설정되었으면 직통 및 환승 경로를 찾아 출력
    //(직통 : 버스 노선 출력, 환승 : 정류장 경로 출력)
    @RequestMapping(method = RequestMethod.GET, path = "/getWayList")
    JSONArray getWayList(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.ROUTE_CITY_LIST);
        this.deptId = deptId;
        this.destId = destId;

        DataCenter.Singleton().wayList.clear();

        TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.FIND_WAY);
        TrafficAPIReceiver.start();

        while(!TrafficAPIReceiver.isDone){
            Thread.sleep(100);
        }

        //Thread.sleep(2000);


        return DataCenter.Singleton().wayList;
    }

    /*테스트 코드*/
    //클라이언트에서 출발지, 목적지가 설정되었으면 직통 및 환승 경로를 찾아 출력
    //(직통 : 버스 노선 출력, 환승 : 정류장 경로 출력)
    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public JSONArray test(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
        //

        this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.ROUTE_CITY_LIST);
        this.deptId = deptId;
        this.destId = destId;



        TrafficAPIReceiver2 receiver = new TrafficAPIReceiver2(APIHandler.TEST_FIND_WAY);

        //DataCenter.Singleton().finaldirectPathList.clear();
        receiver.start();

        while(!receiver.isDone) {
            Thread.sleep(200);
        }


        /* 테스트 값 분류

        String startNodeid1 = "CJB271000143";
        //코스모사원 아파트
        String startNodeid2 = "CJB283000207";
        //청주고등학교

        String endNodeid1 = "CJB283000028";
        //사직 사거리
        String endNodeid2 = "CJB283000037";
        //서원 대학교

        //코스모 사원 아파트 - 사직 사거리 : 직통 경로 존재
        //코스모 사원 아파트 - 서원 대학교 : 무조건 환승
        //청주고등학교 - 사직 사거리 : 직통 경로 존재
        //청주고등학교 - 서원 대학교 : 근접위치(꽃다리) 직통경로 존재
        */

        return DataCenter.Singleton().finalPathList;
    }
}
