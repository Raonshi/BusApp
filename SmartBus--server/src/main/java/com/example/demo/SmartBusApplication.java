package com.example.demo;

import com.example.demo.controller.PublicOperation;
import com.example.demo.utils.APIHandler;
import com.example.demo.utils.TrafficAPIReceiver;
import com.example.demo.datacenter.DataCenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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


	/**
	 * 각 도시별 출발지와 도착지 정류장 번호를 통해 경로를 탐색한 뒤 결과를 반환한다.
	 * @param cityName 조회할 도시
	 * @param deptId 조회할 정류장
	 * @param destId 목적지 정류장
	 * @return
	 */

	@RequestMapping(method = RequestMethod.GET, path = "/getWayList")
	JSONArray getWayList(@RequestParam String cityName, @RequestParam String deptId, @RequestParam String destId) throws InterruptedException {
		this.cityCode = PublicOperation.getCityCode(cityName, APIHandler.ROUTE_CITY_LIST);
		this.deptId = deptId;
		this.destId = destId;

		DataCenter.Singleton().wayList.clear();

		TrafficAPIReceiver TrafficAPIReceiver = new TrafficAPIReceiver(APIHandler.FIND_WAY);
		TrafficAPIReceiver.start();

		Thread.sleep(3000);


		return DataCenter.Singleton().wayList;
	}


	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
