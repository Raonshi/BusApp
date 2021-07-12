package com.example.demo;

import com.example.demo.api_controller.Receiver;
import com.example.demo.data.*;
import com.example.demo.data.DataController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@SpringBootApplication
public class SmartBusApplication {
	


	@RequestMapping(method = RequestMethod.GET, path = "/list")
	City list(@RequestParam String cityName) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
		receiver.start();

		Thread.sleep(500);
		
		for(int i = 0; i < DataController.Singleton().cityList.size(); i++) {
			System.out.println(DataController.Singleton().cityList.get(i).get_cityName());
		}

		for(int i = 0; i < DataController.Singleton().cityList.size(); i++) {
			if(DataController.Singleton().cityList.get(i).get_cityName().contains(cityName)){
				System.out.println("Success");
				return DataController.Singleton().cityList.get(i);
			}
			
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/route")
	RouteInfo route(@RequestParam String cityCode, @RequestParam String routeId) throws InterruptedException {
		RouteInfo rouif = new RouteInfo();
		rouif.set_in_cityCode(cityCode);
		rouif.set_in_routeId(routeId);
		System.out.println(rouif.get_in_cityCode() + "" +  rouif.get_in_routeId());
		Receiver receiver = new Receiver(Function.ROUTE_INFO);
		receiver.getRouteInfoItem(rouif);
		receiver.start();
		
		for(int i = 0; i < DataController.Singleton().routeInfoList.size(); i++) {
			if(DataController.Singleton().routeInfoList.get(i).get_routeId().contains(routeId)){
				String r_data = "노선 ID : " + DataController.Singleton().routeInfoList.get(i).get_routeId() +
						" 노선 번호  : " + DataController.Singleton().routeInfoList.get(i).get_routeNum() +
						" 노선 유형  : " + DataController.Singleton().routeInfoList.get(i).get_routeType() +
						" 종점  : " + DataController.Singleton().routeInfoList.get(i).get_endNode() +
						" 기점  : " + DataController.Singleton().routeInfoList.get(i).get_startNode() +
						" 막차시간  : " + DataController.Singleton().routeInfoList.get(i).get_endTime() +
						" 첫차시간  : " + DataController.Singleton().routeInfoList.get(i).get_startTime() +
						" 배차(평일) : " + DataController.Singleton().routeInfoList.get(i).get_interval() +
						" 배차(토요일) : " + DataController.Singleton().routeInfoList.get(i).get_intervalSat() +
						" 배차(일요일) : " + DataController.Singleton().routeInfoList.get(i).get_IntervalSun();
				System.out.println(r_data);
				return DataController.Singleton().routeInfoList.get(i);
			}
		}
			
		return null;
	}
	
		

	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
