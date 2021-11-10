package com.example.demo.dto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Path {
    //첫 번째 경로 - Required
    ArrayList<Station> first = new ArrayList<Station>();
    public ArrayList<Station> getFirst() {return first;}
    public void setFirst(ArrayList<Station> first) {this.first = first;}

    //두 번째 경로 - Required
    ArrayList<Station> second = new ArrayList<Station>();
    public ArrayList<Station> getSecond() {return second;}
    public void setSecond(ArrayList<Station> second) {this.second = second;}

    //첫 번째 경로 소요시간 - Optional
    private int firstTime;
    public int getFirstTime() {return firstTime;}
    public void setFirstTime(int firstTime) {this.firstTime = firstTime;}

    //두 번째 경로 소요시간 - Optional
    private int secondTime;
    public int getSecondTime() {return secondTime;}
    public void setSecondTime(int secondTime) {this.secondTime = secondTime;}


    //Json -> Path
    /**
     * JSONObject를 매개변수로 받은 뒤 path로 변환한다.
     * @param json Path객체로 변환할 JSONObject
     * @return Path
     */
    Path fromJson(JSONObject json){
        //반환할 객체 생성
        Path path = new Path();

        //first값을 JSONArray로 저장 후 반복문을 통해 ArrayList에 저장
        JSONArray jsonArray = (JSONArray) json.get("first");
        ArrayList<Station> first = new ArrayList<Station>();
        for(Object obj : jsonArray){
            JSONObject tmp = (JSONObject) obj;
            Station station = new Station();
            station.fromJson(tmp);
            first.add(station);
        }
        //객체에 저장
        path.setFirst(first);

        //JSONArray 초기화
        jsonArray.clear();

        //second JSONArray로 저장 후 반복문을 통해 ArrayList에 저장
        jsonArray = (JSONArray) json.get("second");
        ArrayList<Station> second = new ArrayList<Station>();
        for(Object obj : jsonArray){
            JSONObject tmp = (JSONObject) obj;
            Station station = new Station();
            station.fromJson(tmp);
            second.add(station);
        }
        //객체에 저장
        path.setSecond(second);

        return path;
    }

    /*
    //Path -> Json
    JSONObject toJson(){

    }
    */
}

class Station {
    //정거장 이름
    private String nodenm;
    public String getNodenm(){return this.nodenm;}
    public void setNodenm(String value){this.nodenm = value;}

    //정거장 번호
    private String nodeno;
    public String getNodeno(){return this.nodeno;}
    public void setNodeno(String value){this.nodeno = value;}

    //정거장 아이디
    private String nodeid;
    public String getNodeid(){return this.nodeid;}
    public void setNodeid(String value){this.nodeid = value;}

    //현재 경로에서 정거장의 위치
    private String nodeord;
    public String getNodeord(){return this.nodeord;}
    public void setNodeord(String value){this.nodeord = value;}

    //경도
    private String gpslong;
    public String getGpslong(){return this.gpslong;}
    public void setGpslong(String value){this.gpslong = value;}

    //위도
    private String gpslati;
    public String getGpslati(){return this.gpslati;}
    public void setGpslati(String value){this.gpslati = value;}

    //운행 버스 번호
    private String routeNo;
    public String getRouteNo(){return this.routeNo;}
    public void setRouteNo(String value){this.routeNo = value;}

    //상행, 하행 구분
    private String updowncd;
    public String getUpdowncd(){return this.updowncd;}
    public void setUpdowncd(String value){this.updowncd = value;}


    //Json -> Station
    Station fromJson(JSONObject json){
        Station station = new Station();
        station.setNodenm(json.get("nodenm").toString());
        station.setNodeno(json.get("nodeno").toString());
        station.setNodeid(json.get("nodeid").toString());
        station.setNodeord(json.get("nodeord").toString());
        station.setGpslong(json.get("gpslong").toString());
        station.setGpslati(json.get("gpslati").toString());
        station.setRouteNo(json.get("routeno").toString());
        station.setUpdowncd(json.get("updowncd").toString());

        return station;
    }


    //Station -> Json
    JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("nodenm", this.nodenm);
        json.put("nodeno", this.nodeno);
        json.put("nodeid", this.nodeid);
        json.put("nodeord", this.nodeord);
        json.put("gpslong", this.gpslong);
        json.put("gpslati", this.gpslati);
        json.put("routeno", this.routeNo);
        json.put("updowncd", this.updowncd);

        return json;
    }
}
