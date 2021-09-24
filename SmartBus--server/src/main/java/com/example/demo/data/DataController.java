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
     * <p>경로 탐색시 실제로 사용자가 입력한 정거장 이름</p>
     */
    public String deptName, destName;


    /**
     * <p>국토교통부-버스노선정보 API를 통해 얻은 데이터를 저장하는 ArrayList</p>
     */
    public JSONArray cityList = new JSONArray();            //지원하는 도시 리스트
    public JSONArray routeInfoList = new JSONArray();       //버스노선 상세 정보
    public JSONArray stationNumList = new JSONArray();      //정류장 기본 정보
    public JSONArray accessStationList = new JSONArray();   //노선이 지나는 정류장 정보
    public JSONArray routeNumList = new JSONArray();        //버스노선 기본 정보
    public JSONArray arrivalList = new JSONArray();         //도착지 노선 정보 리스트
    public JSONArray routeLocationList = new JSONArray();   //요청한 지나는 노선의 위치, 정보 및 정류장 정보
    public JSONArray gpsStationList = new JSONArray();      //좌표 기반 주변 정류장 목록
    public JSONArray arrivalRouteList = new JSONArray();

    public JSONArray wayList = new JSONArray();             //출발지 -> 도착지 중복 경로 노선 리스트
}