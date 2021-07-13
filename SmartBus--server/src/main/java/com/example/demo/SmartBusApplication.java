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

	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
