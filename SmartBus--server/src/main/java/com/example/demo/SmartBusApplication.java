package com.example.demo;

import com.example.demo.api_controller.Receiver;
import com.example.demo.data.City;
import com.example.demo.data.DataController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@SpringBootApplication
public class SmartBusApplication {


	@RequestMapping(method = RequestMethod.GET, path = "/test")
	String test(@RequestParam String cityName) throws InterruptedException {
		Receiver receiver = new Receiver(Function.ROUTE_CITY_LIST);
		receiver.start();

		Thread.sleep(4000);

		for(int i = 0; i < DataController.Singleton().cityList.size(); i++) {
			if(DataController.Singleton().cityList.get(i).get_cityName().contains(cityName)){
				return DataController.Singleton().cityList.get(i).get_cityName();
			}
		}
		return "Failed";
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
