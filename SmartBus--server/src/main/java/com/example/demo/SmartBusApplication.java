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

	public static String cityCode;
	public static String routeId;

	public static String routeNo;

	public static String nodeNm;
	public static String nodeNo;


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


	@RequestMapping(method = RequestMethod.GET, path = "/getBusList")
	JSONArray getBusList(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {

		this.cityCode = getCityCode(cityName);
		this.routeNo = routeNo;

		Receiver receiver = new Receiver(Function.ROUTE_NUMBER_LIST);
		receiver.start();

		Thread.sleep(1000);


		JSONArray routeList = DataController.Singleton().routeNumList;

		for(int i = 0; i < routeList.size(); i++) {
			JSONObject json = (JSONObject) routeList.get(i);

			if(!json.get("routeno").toString().contains(routeNo)){
				//System.out.println("Success");
				//return json;
				routeList.remove(i);
			}
		}
		return routeList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/test")
	JSONArray test(@RequestParam String cityName, @RequestParam String nodeNm) throws InterruptedException {
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


	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
