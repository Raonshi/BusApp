package com.example.springboottest1.data;

import org.json.simple.JSONArray;

public class DataCenter {

    private static DataCenter instance = null;
    public static DataCenter Singleton(){
        if(instance == null){
            instance = new DataCenter();
        }
        return instance;
    }


    //현재 정류장에 도착예정중인 버스 리스트
    public JSONArray nodeArrivarBusList = new JSONArray();

    //출발지로 설정한 정류장에 도착예정중인 버스 리스트
    public JSONArray startNodeArrivalBusList = new JSONArray();

    //도착지로 설정한 정류장에 도착예정중인 버스 리스트
    public JSONArray endNodeArrivalBusList = new JSONArray();

    //노선이 경유하는 모든 정거장 리스트
    public JSONArray busPathList = new JSONArray();

    //출발지 - 도착지 직통으로 가는 버스 리스트
    public JSONArray directBusList = new JSONArray();

    //출발지 - 도착지로 가는 경로
    public JSONArray directPathList = new JSONArray();

    //출발지 - 도착지로 가는 모든 버스 와 경로 리스트
    public JSONArray finaldirectPathList = new JSONArray();

    //출발지 정류장에 오는 버스가 경유하는 모든 정거장 리스트
    public JSONArray startBusPathList = new JSONArray();

    //출발지 - 종점 가는 경로 리스트
    public JSONArray startToLastPathList = new JSONArray();

    //기점 - 도착지 가는 경로 리스트
    public JSONArray firstToEndPathList = new JSONArray();

    //출발지 버스정보 및 출발지 - 환승지점 으로 가는 경로
    public JSONArray startToTransPathList = new JSONArray();

    //도착지지 정류에 오는 버스가 경유하는 모든 정거장 리스트
    public JSONArray endBusPathList = new JSONArray();

    //도착지 버스정보 및 환승지점 - 도착지 으로 가는 경로
    public JSONArray transToEndPathList = new JSONArray();

    //출발지 - 환승 - 도착지로 가는 경로
    public JSONArray transportPathList = new JSONArray();

    //출발지 - 환승 - 도착지로 가는 모든 버스와 경로 리스트
    public JSONArray finalTransportPathList = new JSONArray();


    public JSONArray finalpathList = new JSONArray();

}
