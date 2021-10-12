package com.example.demo.utils;

import com.example.demo.controller.RegionInfo;
import com.example.demo.datacenter.DataCenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherAPIReceiver extends Thread{

    private APIHandler APIHandler;
    RegionInfo regionInfo;

    public WeatherAPIReceiver(APIHandler APIHandler) {
        this.APIHandler = APIHandler;
    }

    @Override
    public void run() {
        super.run();

        switch(APIHandler) {
            case REGION_WEATHER_INFO:
                System.out.println(regionInfo.latitude + " " + regionInfo.longitude);
                getRegionWeather(regionInfo.latitude, regionInfo.longitude);
                break;
        }
    }

    void getRegionWeather(String latitude, String longitude) {

        try {
            StringBuilder urlBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather");
            urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=0db7106ec70e7b086c079aaf5db4af9c");
            urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");

            URL url = new URL(urlBuilder.toString());

            DataCenter.Singleton().weatherList.clear();

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            String result = "";
            while((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            JSONObject resultObj = new JSONObject();
            JSONParser jsonParser = new JSONParser();

            JSONObject parsejson = (JSONObject) jsonParser.parse(result);

            resultObj.put("region", parsejson.get("name"));

            JSONArray weatherArray = (JSONArray) parsejson.get("weather");
            JSONObject weatherObj = (JSONObject) weatherArray.get(0);

            resultObj.put("weather", weatherObj.get("main"));

            JSONObject tempArray = (JSONObject) parsejson.get("main");

            resultObj.put("temparature", tempArray.get("temp"));

            DataCenter.Singleton().weatherList.add(resultObj);

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
