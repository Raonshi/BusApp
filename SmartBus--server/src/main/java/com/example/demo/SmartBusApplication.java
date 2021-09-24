package com.example.demo;

import com.example.demo.api_controller.Receiver;
import com.example.demo.data.*;
import com.example.demo.data.DataController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class SmartBusApplication {

	public static String cityCode;	//타 클래스로 넘길 도시코드
	public static String routeId;	//타 클래스로 넘길 노선 id
	public static String routeNo;	//타 클래스로 넘길 노선 번호
	public static String nodeNm;	//타 클래스로 넘길 정류장 이름
	public static String nodeId;	//타 클래스로 넘길 정류장 id

	public static String deptId;	//출발지 정류소 id
	public static String destId;	//도작지 정류소 id

	/*
	//지원 도시 목록 출력
	//지금까지는 사용 할 필요성이 없었음.
	@RequestMapping(method = RequestMethod.GET, path = "/list")
	JSONArray list(@RequestParam String cityName) throws InterruptedException {

		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
		receiver.start();

		Thread.sleep(500);


		JSONArray list = DataController.Singleton().cityList;
		JSONArray result = new JSONArray();

		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("cityName").toString().contains(cityName)) {
				result.add(json);
			}
		}

		return result;
	}
	*/

	/*
	//각 도시별 버스 노선 출력
	//지금까지 사용할 필요성이 없었음.
	@RequestMapping(method = RequestMethod.GET, path = "/route")
	JSONArray route(@RequestParam String cityCode, @RequestParam String routeId) throws InterruptedException {
		this.cityCode = cityCode;
		this.routeId = routeId;

		Receiver receiver = new Receiver(Function.ROUTE_INFO);
		receiver.start();

		Thread.sleep(500);

		JSONArray route = DataController.Singleton().routeInfoList;
		JSONArray result = new JSONArray();

		for(int i = 0; i < route.size(); i++) {
			JSONObject json = (JSONObject) route.get(i);
			if(json.get("routeId").toString().contains(routeId)) {
				result.add(json);

			}
		}
		return result;
	}
	*/

	/**
	 * 버스 노선 검색
	 * @param cityName 검색할 도시
	 * @param routeNo 검색할 버스 노선 번호
	 * @return {@link JSONArray}타입의 버스 노선 정보
	 * @throws InterruptedException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/getBusList")
	JSONArray getBusList(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {

		this.cityCode = getCityCode(cityName, Function.ROUTE_CITY_LIST);
		this.routeNo = routeNo;

		Receiver receiver = new Receiver(Function.ROUTE_NUMBER_LIST);
		receiver.start();

		Thread.sleep(1000);

		JSONArray routeList = DataController.Singleton().routeNumList;
		JSONArray result = new JSONArray();
		for(int i = 0; i < routeList.size(); i++) {
			JSONObject json = (JSONObject) routeList.get(i);
			if(json.get("routeno").toString().contains(routeNo)){
				result.add(json);
			}
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getStationList")
	JSONArray getStationList(@RequestParam String cityName, @RequestParam String nodeNm) throws InterruptedException {
		this.cityCode = getCityCode(cityName, Function.STATION_CITY_LIST);
		this.nodeNm = nodeNm;

		Receiver receiver = new Receiver(Function.STATION_NUMBER_LIST);
		receiver.start();

		Thread.sleep(1000);

		JSONArray stationList = DataController.Singleton().stationNumList;
		JSONArray result = new JSONArray();
		for(int i = 0; i < stationList.size(); i++) {
			JSONObject json = (JSONObject) stationList.get(i);
			if(json.get("nodenm").toString().contains(nodeNm)) {
				result.add(json);
			}

		}
		return result;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/TEST1")
	JSONArray getStationPreArrivalNodeList(@RequestParam String cityName, @RequestParam String nodeNm) throws InterruptedException {
		this.cityCode = getCityCode(cityName, Function.STATION_CITY_LIST);

		ArrayList<String> nodeIdList = getNodeId(cityName, nodeNm);

		JSONArray result = new JSONArray();
		for(int i = 0; i < nodeIdList.size(); i++) {
			this.nodeId = nodeIdList.get(i);

			Receiver receiver = new Receiver(Function.ARRIVE_BUS_LIST);
			receiver.start();

			Thread.sleep(1000);
			JSONArray arrivalList = DataController.Singleton().arrivalList;

			for(int j = 0; j < arrivalList.size(); j++) {
				JSONObject json = (JSONObject) arrivalList.get(i);
				if(json.get("nodenm").toString().contains(nodeNm)) {
					result.add(json);
				}
			}
		}
		return result;
	}

	/**
	 * 각 도시별 출발지와 도착지 정류장 번호를 통해 경로를 탐색한 뒤 결과를 반환한다.
	 * @param cityName 조회할 도시
	 * @param deptId 조회할 정류장
	 * @param destId 목적지 정류장
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/getWayList")
	JSONArray getWayList(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
		this.cityCode = getCityCode(cityName, Function.ROUTE_CITY_LIST);
		this.deptId = deptId;
		this.destId = destId;

		DataController.Singleton().wayList.clear();

		Receiver receiver = new Receiver(Function.FIND_WAY);
		receiver.start();

		Thread.sleep(3000);


		return DataController.Singleton().wayList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getBusLocation")
	JSONArray getBusLocation(@RequestParam String cityName, @RequestParam String routeId) throws InterruptedException{
		this.cityCode = getCityCode(cityName, Function.LOCATION_CITY_LIST);
		this.routeId = routeId;

		Receiver receiver = new Receiver(Function.LOCATION_BUS_LIST);
		receiver.start();
		Thread.sleep(1000);
		//JSONArray locationList = DataController.Singleton().routeLocationList;

		return DataController.Singleton().routeLocationList;
	}




	/*cityName to cityCode*/
	public static String getCityCode(String cityName, @Nullable Function type) throws InterruptedException {
		type = Optional.ofNullable(type).orElse(Function.ROUTE_CITY_LIST);

		Receiver receiver = new Receiver(type);
		receiver.start();

		Thread.sleep(500);

		JSONArray list = DataController.Singleton().cityList;

		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("cityName").toString().contains(cityName)){
				return json.get("cityCode").toString();
			}
		}
		return "failed";
	}


	/*nodeNm to nodeId*/
	public ArrayList<String> getNodeId(String cityName, String nodeNm) throws InterruptedException {
		this.cityCode = getCityCode(cityName, Function.STATION_CITY_LIST);
		this.nodeNm = nodeNm;

		Receiver receiver = new Receiver(Function.STATION_NUMBER_LIST);
		receiver.start();

		Thread.sleep(500);

		JSONArray list = DataController.Singleton().stationNumList;
		
		//결과 리스트 반환
		ArrayList<String> result = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("nodenm").toString().contains(nodeNm)){
				result.add(json.get("nodeid").toString());
			}
		}

		return result;
	}

	/*test*/
	@RequestMapping(method = RequestMethod.GET, path = "/test")
	JSONArray test(@RequestParam String cityCode, @RequestParam String nodeId) throws InterruptedException{
		JSONArray result = new JSONArray();
		this.cityCode = cityCode;
		this.nodeId = nodeId;

		Receiver receiver = new Receiver(Function.ARRIVE_BUS_LIST);
		receiver.start();
		Thread.sleep(500);
		result = DataController.Singleton().arrivalList;

		return result;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
