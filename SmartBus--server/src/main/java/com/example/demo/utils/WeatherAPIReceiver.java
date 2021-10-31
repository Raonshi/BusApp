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
                getRegionWeather(regionInfo.latitude, regionInfo.longitude);
                break;
        }
    }

    void getRegionWeather(String latitude, String longitude) {

        try {
            StringBuilder urlBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/forecast");
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

            JSONParser jsonParser = new JSONParser();
            JSONObject parsejson = (JSONObject) jsonParser.parse(result);

            JSONArray weatherDays5 = (JSONArray) parsejson.get("list");
            JSONObject regionObj = (JSONObject) parsejson.get("city");
            JSONObject obj = new JSONObject();
            obj.put("region", regionObj.get("name"));
            DataCenter.Singleton().weatherList.add(obj);

            for(int i = 0; i < weatherDays5.size(); i++) {
                JSONObject resultObj = new JSONObject();
                JSONObject json = (JSONObject) weatherDays5.get(i);

                JSONObject mainObject = (JSONObject) json.get("main");
                JSONArray weatherList = (JSONArray) json.get("weather");
                JSONObject weatherObject = (JSONObject) weatherList.get(0);

                String dateCheck = json.get("dt_txt").toString();

                if(dateCheck.contains("09:00") || dateCheck.contains("15:00") || dateCheck.contains("21:00")) {
                    resultObj.put("temparature", mainObject.get("temp").toString());
                    resultObj.put("weather", weatherObject.get("main"));
                    resultObj.put("datetime", json.get("dt_txt"));

                    DataCenter.Singleton().weatherList.add(resultObj);
                }

            }

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
