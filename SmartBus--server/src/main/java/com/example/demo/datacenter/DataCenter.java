package com.example.demo.datacenter;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public class DataCenter {

    /**
     * 싱글톤
     */
    private static DataCenter instance = null;
    public static DataCenter Singleton(){
        if(instance == null){
            instance = new DataCenter();
        }
        return instance;
    }


    /**
     * <p>경로 탐색시 실제로 사용자가 입력한 정거장 이름</p>
     */
    public String deptName, destName;


    /**
     * <p>국토교통부-버스노선정보 API를 통해 얻은 데이터를 저장하는 List</p>
     */
    public JSONArray cityList = new JSONArray();            //지원하는 도시 리스트

    public JSONArray routeNumList = new JSONArray();        //버스노선 기본 정보
    public JSONArray routeInfoList = new JSONArray();       //버스노선 상세 정보
    public JSONArray arrivalList = new JSONArray();         //도착지 노선 정보 리스트
    public JSONArray routeLocationList = new JSONArray();   //요청한 지나는 노선의 위치, 정보 및 정류장 정보
    public JSONArray arrivalRouteList = new JSONArray();

    public JSONArray stationNumList = new JSONArray();      //정류장 기본 정보
    public JSONArray accessStationList = new JSONArray();   //노선이 지나는 정류장 정보
    public JSONArray gpsStationList = new JSONArray();      //좌표 기반 주변 정류장 목록



    /*
    * 국토교통부-버스노선정보 API를 가공하여 얻은 데이터를 저장하는 List
    */

    public ArrayList<String> routeIdList = new ArrayList<>();
    //getBusList 요청시 PublicOperation.getRouteId 로 얻어온 버스 노선들의 id 리스트
    public ArrayList<String> stationIdList = new ArrayList<>();
    //getStation 요청시 PublicOperation.getStationId 로 얻어온 버스 정류장들의 id 리스트

    public JSONArray finalRouteList = new JSONArray();
    //도시이름, 노선번호로 getBusList 요청시 API를 통해 얻어온 데이터들을 가공한 버스 노선들의 상세 정보 리스트
    public JSONArray finalStationList = new JSONArray();
    //도시이름, 정류장이름으로 getStationList 요청시 API를 통해 얻어온 데이터들을 가공한 버스 정류장들의 상세 정보 리스트

    public JSONArray wayList = new JSONArray();
    //출발지 -> 도착지 중복 경로 노선 리스트


    /*
    * OpenWeatherAPI 현재 날씨 정보 API를 통해 얻은 데이터를 저장하는 List
    */
    public JSONArray weatherList = new JSONArray();


    /*
     * 한국환경공단(에어코리아) 대기오염정보 API를 통해 얻은 데이터를 저장하는 List
     */
    public JSONArray dustList = new JSONArray();
}