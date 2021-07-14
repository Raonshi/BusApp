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

import java.util.ArrayList;
import java.util.Map;

@RestController
@SpringBootApplication
public class SmartBusApplication {

	public static String cityCode;
	public static String routeId;

	public static String routeNo;

	public static String nodeNm;
	public static String nodeNo;

	@RequestMapping(method = RequestMethod.GET, path = "/list")
	JSONObject list(@RequestParam String cityName) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
		receiver.start();

		Thread.sleep(500);

		JSONArray list = DataController.Singleton().cityList;

		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("cityName").toString().contains(cityName)){
				System.out.println("Success");
				return json;
			}
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/route")
	JSONObject route(@RequestParam String cityCode, @RequestParam String routeId) throws InterruptedException {
		this.cityCode = cityCode;
		this.routeId = routeId;

		Receiver receiver = new Receiver(Function.ROUTE_INFO);
		receiver.start();

		Thread.sleep(500);

		JSONArray route = DataController.Singleton().routeInfoList;

		for(int i = 0; i < route.size(); i++) {
			JSONObject json = (JSONObject) route.get(i);
			if(json.get("routeId").toString().contains(routeId)) {
				System.out.println("Success");
				return json;
			}
		}
		return null;
	}

	public static String getCityCode(String cityName) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
		receiver.start();

		Thread.sleep(500);

		JSONArray list = DataController.Singleton().cityList;

		for(int i = 0; i < list.size(); i++) {
			JSONObject json = (JSONObject) list.get(i);
			if(json.get("cityName").toString().contains(cityName)){
				System.out.println("Success");
				return json.get("cityCode").toString();
			}
		}
		return "failed";
	}


	@RequestMapping(method = RequestMethod.GET, path = "/getBusList")
	JSONArray test(@RequestParam String cityName, @RequestParam String routeNo) throws InterruptedException {

		this.cityCode = getCityCode(cityName);
		this.routeNo = routeNo;

		Receiver receiver = new Receiver(Function.ROUTE_NUMBER_LIST);
		receiver.start();

		Thread.sleep(2000);

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


	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
