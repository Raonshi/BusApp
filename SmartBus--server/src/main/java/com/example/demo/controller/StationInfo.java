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
 * 2. 클라이언트에서 위치(위도, 경도)를 입력받아 정류소를 조회  (List)
 * 3. 클라이언트에서 도착할 정류장 이름를 입력받아 도착지 정류장 정보 조회  (List)
 * 4. 클라이언트에서 도시 이름 과 정류장 ID를 받아 해당 정류장의 도착 예정 버스의 정보를 출력 (List)
 * 5. 도시이름과 도착 장소를 받아 해당 위치의 근접 좌표(위도, 경도) 출력 (Object : 리스트중 매칭되는 첫번째 오브젝트를 반환)
 * (1, 3 오퍼레이션 동일)
 */


@RestController
public class StationInfo {

    public static String cityCode;
    public static String cityName;
    public static String nodeNm;
    public static String nodeId;

    public static String xPos;
    public static String yPos;

    public static String place;

    //매개변수로 입력받은 정류장의 세부 정보 조회
    //클라이언트에서 도착할 정류장 이름를 입력받아 도착지 정류장 정보 조회
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

        while(!TrafficAPIReceiver.isDone) {
            Thread.sleep(100);
        }

        //Thread.sleep(1000);

        JSONArray stationList = DataCenter.Singleton().stationNumList;
        for(Object obj : stationList) {
            JSONObject json = (JSONObject) obj;
            if(json.get("nodenm").toString().contains(nodeNm)) {
                DataCenter.Singleton().finalStationList.add(json);
            }

        }

        return DataCenter.Singleton().finalStationList;
    }

    //클라이언트에서 위치(위도, 경도)를 입력받아 출발할 정류소를 조회
    @RequestMapping(method = RequestMethod.GET, path = "/getDeptStation")
    JSONArray getDeptStation(@RequestParam String cityName, @RequestParam String latitude, @RequestParam String longitude) throws InterruptedException{

        PublicOperation pub = new PublicOperation();

        this.cityCode = pub.getCityCode(cityName, APIHandler.STATION_CITY_LIST);
        this.yPos = latitude;
        this.xPos = longitude;


        TrafficAPIReceiver receiver = new TrafficAPIReceiver(APIHandler.STATION_SPECIFY_LOCATION_LIST);
        receiver.start();

        while(!receiver.isDone) {
            Thread.sleep(100);
        }

        //Thread.sleep(1500);

        JSONArray result = new JSONArray();
        result = DataCenter.Singleton().gpsStationList;

        return result;
    }


    //클라이언트에서 도시 이름 과 정류장 ID를 받아 해당 정류장의 도착 예정 버스의 정보를 출력
    @RequestMapping(method = RequestMethod.GET, path = "/preArrivalStation")
    JSONArray getPreArrivalStation(@RequestParam String cityName, @RequestParam String nodeId) throws InterruptedException {

        PublicOperation pub = new PublicOperation();

        this.cityCode = pub.getCityCode(cityName, APIHandler.ARRIVE_CITY_LIST);
        this.nodeId = nodeId;

        TrafficAPIReceiver receiver = new TrafficAPIReceiver(APIHandler.ARRIVE_BUS_LIST);
        receiver.start();

        while(!receiver.isDone) {
            Thread.sleep(100);
        }

        //Thread.sleep(1500);

        return DataCenter.Singleton().arrivalList;
    }


    //도시이름과 도착 장소를 받아 해당 위치의 근접 좌표(위도, 경도) 출력 (Object : 리스트중 매칭되는 첫번째 오브젝트를 반환)
    @RequestMapping(method = RequestMethod.GET, path = "/getDestCoordinate")
    JSONObject getDestStation(@RequestParam String cityName, @RequestParam String place) throws InterruptedException{

        this.cityName = cityName;
        this.place = place;

        TrafficAPIReceiver receiver = new TrafficAPIReceiver(APIHandler.GET_COORDINATE);
        receiver.start();

        while(!receiver.isDone) {
            Thread.sleep(100);
        }

        //Thread.sleep(1500);

        JSONObject object = DataCenter.Singleton().placeCoordinate;

        return object;
    }

}
