package com.example.demo;

import com.example.demo.api_controller.Receiver;
import com.example.demo.data.DataController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SmartBusApplication {

	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		receiver.getCityList();

		for(Map.Entry<String, String> entry : DataController.Singleton().cityTable.entrySet()){
			System.out.println("[KEY]" + entry.getKey() + "      [VALUE]" + entry.getValue());
		}

		SpringApplication.run(SmartBusApplication.class, args);
	}

}
