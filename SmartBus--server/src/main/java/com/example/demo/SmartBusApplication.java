package com.example.demo;

import com.example.demo.api_controller.Receiver;
import com.example.demo.data.*;
import com.example.demo.data.DataController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	public static String deptId;	//출발지 정류소 id
	public static String destId;	//도작지 정류소 id

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

	@RequestMapping(method = RequestMethod.GET, path = "/getBusList")
	JSONArray getBusList(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {

		this.cityCode = getCityCode(cityName);
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
		this.cityCode = getCityCode(cityName);
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

	@RequestMapping(method = RequestMethod.GET, path = "/test")
	JSONArray test(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_THROUGH_STATION_LIST);
		receiver.start();

		Thread.sleep(1000);

		JSONArray routeThroughStationList = DataController.Singleton().accessStationList;
		JSONArray result = new JSONArray();

		for(int i = 0; i < routeThroughStationList.size(); i++) {
			JSONObject json = (JSONObject) routeThroughStationList.get(i);
			if(json.get("routeId").toString().contains("DJB30300004")) {
				result.add(json);
			}
		}
		return result;
	}

	/*cityName to cityCode*/
	public static String getCityCode(String cityName) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
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
	public static String getNodeId(String cityName, String nodeNm) throws InterruptedException {
		String cityCode = getCityCode(cityName);
		Receiver receiver = new Receiver(null);
		receiver.getRouteNoList(cityCode, nodeNm);

		JSONArray list = DataController.Singleton().stationNumList;

		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("nodeNm").toString().contains(nodeNm)){
				return json.get("nodeid").toString();
			}
		}

		return "failed";
	}


	/**
	 * 각 도시별 지발추와 도착지 정류장 번호를 통해 경로를 탐색한 뒤 결과를 반환한다.
	 * @param cityName 조회할 도시
	 * @param deptId 조회할 정류장
	 * @param destId 목적지 정류장
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/getWayList")
	JSONArray getWayList(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
		this.cityCode = getCityCode(cityName);
		this.deptId = deptId;
		this.destId = destId;

		Receiver receiver = new Receiver(Function.FIND_WAY);
		receiver.start();

		Thread.sleep(2000);

		return DataController.Singleton().wayList;
	}




	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
