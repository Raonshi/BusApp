package com.example.demo.api_controller;

import com.example.demo.Function;
import com.example.demo.SmartBusApplication;
import com.example.demo.data.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.json.simple.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Receiver extends Thread {
    private Function _function;
    SmartBusApplication sba;

    public Receiver(Function function){
        _function = function;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("==========Receiver Thread Generated!!==========");
        
        switch(_function){
            case ROUTE_CITY_LIST:
                getCityList();
                break;
            case ROUTE_INFO:
                getRouteInfoItem(sba.cityCode, sba.routeId);
                break;
            case ROUTE_NUMBER_LIST:
                //getCityList();
                getRouteNoList(sba.cityCode, sba.routeNo);
                break;
            case STATION_NUMBER_LIST:
                getStationNumList(sba.cityCode, sba.nodeNm);
                break;
            case ROUTE_THROUGH_STATION_LIST:
                getRouteAcctoThrghSttnList(sba.cityCode, sba.routeId);
                break;
            case FIND_WAY:
                getWayList(sba.cityCode, sba.deptId, sba.destId);
                break;
            /*case LOCATION_CITY_LIST:

                break;
            case LOCATION_BUS_LIST:

                break;
            case LOCATION_SPECIFY_STATION_ACCESS_BUS_LIST:

                break;
            case ARRIVE_CITY_LIST:

                break;
            case ARRIVE_BUS_LIST:

                break;
            case ARRIVE_SPECIFY_STATION_ACCESS_BUS_LIST:

                break;*/
        }
    }


    //#region 국토교통부-버스노선정보 API
    /**
     * <p>도시코드 목록 조회</p>
     * <p>서비스 가능 지역들의 도시코드 목록을 조회한 뒤 {@link City}객체에 저장한다.</p>
     * <p>{@link City}객체는 {@link DataController}클래스의 cityList에 저장된다.</p>
     */
    void getCityList(){

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getCtyCodeList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            String url = urlBuilder.toString();
            NodeList list = getData(url);

            DataController.Singleton().cityList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("cityCode", getValue("citycode", element));
                    json.put("cityName", getValue("cityname", element));

                    //City item = new City(getValue("citycode", element), getValue("cityname", element));
                    //System.out.println(json.get("cityName").toString());
                    DataController.Singleton().cityList.add(json);
                }
            }
            
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    /**
     * <p>노선정보 항목 조회</p>
     * <p>노선의 기본정보를 조회한 뒤 {@link RouteInfo}객체에 저장한다.</p>
     * <p>{@link RouteInfo}객체는 {@link DataController}클래스의 routeInfoList에 저장된다.</p>
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param routeId 각 노선별로 부여된 고유한 아이디 값
     */
    void getRouteInfoItem(String cityCode, String routeId){

        try{

            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteInfoIem");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + routeId);
            String url = urlBuilder.toString();
            NodeList list = getData(url);

            DataController.Singleton().routeInfoList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("routeId", getValue("routeid", element));
                    json.put("routeno", getValue("routeno", element));
                    json.put("routetp", getValue("routetp", element));
                    json.put("startnodenm", getValue("startnodenm", element));
                    json.put("endnodenm", getValue("endnodenm", element));
                    json.put("startvehicletime", getValue("startvehicletime", element));
                    json.put("endvehicletime", getValue("endvehicletime", element));
                    json.put("intervaltime", getValue("intervaltime", element));
                    json.put("intervalsattime", getValue("intervalsattime", element));
                    json.put("intervalsuntime", getValue("intervalsuntime", element));

                    DataController.Singleton().routeInfoList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    /**
     * <p>노선 번호 목록 조회</p>
     * <p>버스 노선번호의 목록을 조회한 뒤 {@link RouteNum}에 저장한다.</p>
     * <p>{@link RouteNum}객체는 {@link DataController}클래스의 routeNumList에 저장된다.</p>
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param routeNo 버스를 식별할 수 있는 노선 번호(예 : 502, 10-1, 811-2)
     */
    public void getRouteNoList(String cityCode, String routeNo){

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + URLEncoder.encode(cityCode, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("routeNo","UTF-8") + "=" + URLEncoder.encode(routeNo, "UTF-8"));
            String url = urlBuilder.toString();
            NodeList list = getData(url);

            DataController.Singleton().routeNumList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();

                    json.put("routeid", getValue("routeid", element));
                    json.put("routeno", getValue("routeno", element));
                    json.put("routetp", getValue("routetp", element));
                    json.put("startnodenm", getValue("startnodenm", element));
                    json.put("endnodenm", getValue("endnodenm", element));
                    json.put("startvehicletime", getValue("startvehicletime", element));
                    json.put("endvehicletime", getValue("endvehicletime", element));

                    DataController.Singleton().routeNumList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }


    /**
     * <p>정류소 번호 목록 조회</p>
     * <p>노선별로 경유하는 정류장의 목록을 조회한 뒤 {@link StationNumList}객체에 저장한다.</p>
     * <p>{@link StationNumList}객체는 {@link DataController}클래스의 stationNumList에 저장된다.</p>
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param nodeNm 정류소 이름
     */
    void getStationNumList(String cityCode, String nodeNm) {

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getSttnNoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeNm","UTF-8") + "=" + URLEncoder.encode(nodeNm, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));

            NodeList list = getData(urlBuilder.toString());
            DataController.Singleton().stationNumList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();

                    json.put("gpslati", getValue("gpslati", element));
                    json.put("gpslong", getValue("gpslong", element));
                    json.put("nodeid", getValue("nodeid", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("nodeno", getValue("nodeno", element));


                    DataController.Singleton().stationNumList.add(json);

                }
            }

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    /**
     * <p>노선별 경유 정류소 목록 조회</p>
     * <p>노선별로 경유하는 정류장의 목록을 조회한 뒤 {@link AccessStation}객체에 저장한다.</p>
     * <p>{@link AccessStation}객체는 {@link DataController}클래스의 accessStationList에 저장된다.</p>
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param routeId 각 노선별로 부여된 고유한 아이디 값
     */
    void getRouteAcctoThrghSttnList(String cityCode, String routeId){
        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + "100");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + routeId);

            NodeList list = getData(urlBuilder.toString());
            DataController.Singleton().accessStationList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("routeId", getValue("routeid", element));
                    json.put("nodeid", getValue("nodeid", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("nodeno", getValue("nodeno", element));
                    json.put("nodeord", getValue("nodeord", element));
                    json.put("gpslong", getValue("gpslong", element));
                    json.put("gpslati", getValue("gpslati", element));
                    json.put("updowncd", getValue("updowncd", element));

                    DataController.Singleton().accessStationList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    //#endregion



    //#region 국토교통부-버스위치정보 API

    //#endregion



    //#region 국토교통부-버스도착정보 API

    void getSttnAcctoArvlPrearngeInfoList(String cityCode, String nodeId){
        try {
            System.out.println("Node Id : " + nodeId);

            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);

            NodeList list = getData(urlBuilder.toString());
            DataController.Singleton().arrivalList.clear();

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("nodeid", getValue("nodeid", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("routeid", getValue("routeid", element));
                    json.put("routeno", getValue("routeno", element));
                    json.put("routetp", getValue("routetp", element));
                    json.put("arrprevstationcnt", getValue("arrprevstationcnt", element));
                    json.put("vehicletp", getValue("vehicletp", element));
                    json.put("arrtime", getValue("arrtime", element));

                    DataController.Singleton().arrivalList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    //#endregion



    //#region 버스 경로 탐색

    /**
     * 출발지 정류장과 도착지 정류장 간의 경로 탐색을 수행한 뒤 결과를 {@link DataController}의 {@link JSONArray}에 저장한다.
     * @param deptId 출발지의 id값
     * @param destId 도착지의 id값
     */
    void getWayList(String cityCode, String deptId, String destId){
        //1. 출발지 정류장에 도착할 버스 목록을 구한다.
        getSttnAcctoArvlPrearngeInfoList(cityCode, deptId);
        JSONArray deptArrivalList = DataController.Singleton().arrivalList;

        System.out.println(deptArrivalList.size());

        //2. 도착지 정류장에 도착할 버스 목록을 구한다.
        getSttnAcctoArvlPrearngeInfoList(cityCode, destId);
        JSONArray destArrivalList = DataController.Singleton().arrivalList;

        System.out.println(destArrivalList.size());

        //3. 두 버스 목록에서 겹치는 버스가 있을 경우 직통버스로 간주한다.
        deptArrivalList.forEach(dept -> {
            destArrivalList.forEach(dest -> {
                JSONObject deptJson = (JSONObject)dept;
                JSONObject destJson = (JSONObject)dest;

                //출발지와 도착지에 동일한 버스노선 번호가 존재할 경우
                if(deptJson.get("routeno").toString().equals(destJson.get("routeno").toString())){
                    DataController.Singleton().wayList.add(deptJson);
                }
            });
        });
    }

    //#endregion

    //#region 공통 메서드
    public NodeList getData(String url) throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(url);

        document.getDocumentElement().normalize();

        return document.getElementsByTagName("item");
    }

    public static String getValue(String tag, Element element){

        try{
            NodeList list = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = (Node)list.item(0);
            return node.getNodeValue();
        }
        catch (NullPointerException nullE){
            return "미지원";
        }
    }

    //#endregion
}
