package com.example.demo.utils;

import com.example.demo.controller.BusInfo;
import com.example.demo.controller.PathInfo;
import com.example.demo.controller.PublicOperation;
import com.example.demo.controller.StationInfo;
import com.example.demo.datacenter.DataCenter;
import com.example.demo.dto.*;
import org.json.simple.parser.JSONParser;
import org.springframework.cache.annotation.Cacheable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.json.simple.*;


import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;



public class TrafficAPIReceiver extends Thread {
    private APIHandler APIHandler;

    BusInfo busInfo;
    StationInfo stationInfo;
    PathInfo pathInfo;
    PublicOperation publicOperation;

    public TrafficAPIReceiver(APIHandler APIHandler){
        this.APIHandler = APIHandler;
    }

    @Override
    @Cacheable(value = "trafficDataCache")
    public void run() {
        super.run();
        System.out.println("==========Receiver Thread Generated!!==========");
        
        switch(APIHandler){
            //버스 노선 정보 API
            //사용중이 아닌 API는 주석처리
            case ROUTE_CITY_LIST:
                getCityList(0);
                break;
            case ROUTE_NUMBER_LIST:
                getRouteNoList(publicOperation.cityCode, publicOperation.routeNo);
                break;
            case ROUTE_THROUGH_STATION_LIST:
                getRouteAcctoThrghSttnList(busInfo.cityCode, busInfo.routeId);
                break;
            case ROUTE_INFO:
                getRouteInfoItem(busInfo.cityCode, busInfo.routeId);
                break;

            //버스 도착 정보 API
            case ARRIVE_CITY_LIST:
                getCityList(1);
                break;
            case ARRIVE_BUS_LIST:
                getSttnAcctoArvlPrearngeInfoList(stationInfo.cityCode, stationInfo.nodeId);
                break;
            case ARRIVE_SPECIFY_STATION_ACCESS_BUS_LIST:
                //getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList(sba.cityCode, sba.nodeId, sba.routeId);
                break;

            //버스 정류장 정보 API
            case STATION_CITY_LIST:
                getCityList(2);
                break;
            case STATION_NUMBER_LIST:
                getStationNumList(publicOperation.cityCode, publicOperation.nodeNm);
                break;
            case STATION_SPECIFY_LOCATION_LIST:
                getCrdntPrxmtSttnList(stationInfo.xPos, stationInfo.yPos);
                break;

            //버스 위치 정보 API
            case LOCATION_CITY_LIST:
                getCityList(3);
                break;
            case LOCATION_BUS_LIST:
                //getRouteLocationList(busInfo.cityCode, busInfo.routeId);
                break;
            case LOCATION_SPECIFY_STATION_ACCESS_BUS_LIST:
                //getRouteAcctoSpcifySttnAccesBusLcInfo(sba.cityCode, sba.routeId, sba.nodeId);
                break;

            //경로 연산
            case FIND_WAY:
                getWayList(pathInfo.cityCode, pathInfo.deptId, pathInfo.destId);
                break;
            case GET_COORDINATE:
                getCoordinate(stationInfo.cityName, stationInfo.place);
        }
    }
    //#region 공통 메서드

    /**
     * <p>도시코드 목록 조회</p>
     * <p>서비스 가능 지역들의 도시코드 목록을 조회한 뒤 {@link City}객체에 저장한다.</p>
     * <p>{@link City}객체는 {@link DataCenter}클래스의 cityList에 저장된다.</p>
     * @param type 각 API서비스별 지원 도시목록을 확인하기 위한 구분 파라미터
     */
    void getCityList(int type){

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/");

            switch(type){
                case 0:
                    urlBuilder.append("BusRouteInfoInqireService/getCtyCodeList");
                    break;
                case 1:
                    urlBuilder.append("ArvlInfoInqireService/getCtyCodeList");
                    break;
                case 2:
                    urlBuilder.append("BusSttnInfoInqireService/getCtyCodeList");
                    break;
                case 3:
                    urlBuilder.append("BusLcInfoInqireService/getCtyCodeList");
                    break;
            }

            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            String url = urlBuilder.toString();
            NodeList list = getData(url);

            DataCenter.Singleton().cityList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("cityCode", getValue("citycode", element));
                    json.put("cityName", getValue("cityname", element));
                    DataCenter.Singleton().cityList.add(json);
                }
            }

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }


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

    //#region 국토교통부-버스노선정보 API



    /**
     * <p>노선 번호 목록 조회</p>
     * <p>버스 노선번호의 목록을 조회한 뒤 {@link RouteNum}에 저장한다.</p>
     * <p>{@link RouteNum}객체는 {@link DataCenter}클래스의 routeNumList에 저장된다.</p>
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

            DataCenter.Singleton().routeNumList.clear();

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

                    DataCenter.Singleton().routeNumList.add(json);
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
     * <p>{@link AccessStation}객체는 {@link DataCenter}클래스의 accessStationList에 저장된다.</p>
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
            DataCenter.Singleton().accessStationList.clear();

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

                    DataCenter.Singleton().accessStationList.add(json);
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
     * <p>{@link RouteInfo}객체는 {@link DataCenter}클래스의 routeInfoList에 저장된다.</p>
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

            DataCenter.Singleton().routeInfoList.clear();

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

                    DataCenter.Singleton().routeInfoList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    //#endregion


    //#region 국토교통부-버스도착정보 API

    void getSttnAcctoArvlPrearngeInfoList(String cityCode, String nodeId){
        try {
            System.out.println("cityCode : " + cityCode + " Node Id : " + nodeId);

            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().arrivalList.clear();

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

                    DataCenter.Singleton().arrivalList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    /**
     * 정류소별 특정노선 버스 도착예정 정보 목록 조회
     * @param cityCode 조회하려는 도시
     * @param nodeId 조회하려는 정류소 이름
     * @param routeId 조회하려는 버스노선
     */
    void getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList(String cityCode, String nodeId, String routeId){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);
            urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + routeId);

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().arrivalList.clear();

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

                    DataCenter.Singleton().arrivalList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    //#endregion


    //#region 국토교통부-버스정류소정보 API

    /**
     * <p>정류소 번호 목록 조회</p>
     * <p>노선별로 경유하는 정류장의 목록을 조회한 뒤 {@link StationNumList}객체에 저장한다.</p>
     * <p>{@link StationNumList}객체는 {@link DataCenter}클래스의 stationNumList에 저장된다.</p>
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param nodeNm 정류소 이름
     */
    void getStationNumList(String cityCode, String nodeNm) {

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getSttnNoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeNm","UTF-8") + "=" + URLEncoder.encode(nodeNm, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nodeno", "UTF-8") + "=");

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().stationNumList.clear();

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

                    DataCenter.Singleton().stationNumList.add(json);

                }
            }

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }


    /**
     * <p>좌표 기반 정류장 목록 조회</p>
     * @param xPos 경도 좌표
     * @param yPos 위도 좌표
     */
    void getCrdntPrxmtSttnList(String xPos, String yPos) {
        System.out.println(xPos + " " + yPos);
        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("gpsLati","UTF-8") + "=" + URLEncoder.encode(yPos, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("gpsLong","UTF-8") + "=" + URLEncoder.encode(xPos, "UTF-8"));

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().gpsStationList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();

                    json.put("gpslati", getValue("gpslati", element));
                    json.put("gpslong", getValue("gpslong", element));
                    json.put("nodeid", getValue("nodeid", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("citycode", getValue("citycode", element));

                    DataCenter.Singleton().gpsStationList.add(json);
                }
            }

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

    //#endregion


    //#region 국토교통부-버스위치정보 API

    /**
     * 노선의 현재 위치 정보 및 정류소 정보
     * @param cityCode 조회하려는 도시
     * @param routeId 조회하려는 버스노선
     */
    void getRouteLocationList (String cityCode, String routeId){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + routeId);

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().routeLocationList.clear();

            for(int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("routenm", getValue("routenm", element));
                    json.put("gpslati", getValue("gpslati", element));
                    json.put("gpslong", getValue("gpslong", element));
                    json.put("nodeord", getValue("nodeord", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("nodeid", getValue("nodeid", element));
                    json.put("routetp", getValue("routetp", element));
                    json.put("vehicleno", getValue("vehicleno", element));

                    DataCenter.Singleton().routeLocationList.add(json);
                }
            }

        }catch(ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * 노선의 현재 위치 정보 및 정류소 정보
     * @param cityCode 조회하려는 도시
     * @param routeId 조회하려는 버스노선
     */
    void getRouteAcctoSpcifySttnAccesBusLcInfo (String cityCode, String routeId, String nodeId){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + routeId);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);

            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().arrivalRouteList.clear();

            for(int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();
                    json.put("routenm", getValue("routenm", element));
                    json.put("gpslati", getValue("gpslati", element));
                    json.put("gpslong", getValue("gpslong", element));
                    json.put("nodenm", getValue("nodenm", element));
                    json.put("routetp", getValue("routetp", element));

                    DataCenter.Singleton().arrivalRouteList.add(json);
                }
            }

        }catch(ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    //#endregion

    //#region 버스 경로 탐색

    /**
     * 출발지 정류장과 도착지 정류장 간의 경로 탐색을 수행한 뒤 결과를 {@link DataCenter}의 {@link JSONArray}에 저장한다.
     * @param deptId 출발지의 id값
     * @param destId 도착지의 id값
     */
    void getWayList(String cityCode, String deptId, String destId){
        //1. 출발지 정류장에 도착할 버스 목록을 구한다.
        DataCenter.Singleton().arrivalList.clear();
        getSttnAcctoArvlPrearngeInfoList(cityCode, deptId);

        JSONArray deptArrivalList = new JSONArray();
        deptArrivalList.addAll(DataCenter.Singleton().arrivalList);

        //2. 도착지 정류장에 도착할 버스 목록을 구한다.
        DataCenter.Singleton().arrivalList.clear();
        getSttnAcctoArvlPrearngeInfoList(cityCode, destId);

        JSONArray destArrivalList = new JSONArray();
        destArrivalList.addAll(DataCenter.Singleton().arrivalList);

        //3. 직통 버스 경로 구하기
        System.out.println("=================CALL!!!!====================");

        directWayList(deptArrivalList, destArrivalList);
        //transportWayList(deptArrivalList, destArrivalList);
    }

    /**
     * 직통버스 구하기
     * @param deptArrivalList 출발지 버스 리스트
     * @param destArrivalList 도착지 버스 리스트
     */

    void directWayList(JSONArray deptArrivalList,JSONArray destArrivalList){
        //3. 두 버스 목록에서 겹치는 버스가 있을 경우 직통버스로 간주한다.
        //-> 그 외의 경우에는 환승으로 간주한다 -> 환승은 최대 1회를 넘기지 않는다.

        for (Object o : deptArrivalList) {
            for (Object value : destArrivalList) {
                JSONObject deptJson = (JSONObject) o;
                JSONObject destJson = (JSONObject) value;

                //출발지와 도착지에 동일한 버스노선 번호가 존재할 경우
                if (deptJson.get("routeno").toString().equals(destJson.get("routeno").toString())) {
                    //새로운 JSONObject를 만든다.
                    JSONObject way = new JSONObject();
                    way.put("deptnodenm", deptJson.get("nodenm"));
                    way.put("deptarrtime", deptJson.get("arrtime"));
                    way.put("deptarrprevstationcnt", deptJson.get("arrprevstationcnt"));

                    way.put("destnodenm", destJson.get("nodenm"));
                    way.put("destarrtime", destJson.get("arrtime"));
                    way.put("destarrprevstationcnt", destJson.get("arrprevstationcnt"));

                    way.put("routeno", deptJson.get("routeno"));
                    way.put("routetp", deptJson.get("routetp"));
                    way.put("vehicletp", deptJson.get("vehicletp"));

                    //way를 wayList에 담는다.
                    DataCenter.Singleton().wayList.add(way);
                }
            }
        }

        //임시 리스트 생성
        JSONArray tmpArray = new JSONArray();

        //경로 목록 탐색
        for(int i = 0; i < DataCenter.Singleton().wayList.size(); i++){
            JSONObject json = (JSONObject) DataCenter.Singleton().wayList.get(i);

            String deptStr = json.get("deptnodenm").toString();
            String destStr = json.get("destnodenm").toString();

            //출발지와 목적지 이름이 같은경우 필터링
            if (!deptStr.equals(destStr)) {
                tmpArray.add(json);
            }
        }

        if(tmpArray.isEmpty()){
            DataCenter.Singleton().wayList = transportWayList(deptArrivalList, destArrivalList);
        }
        else{
            DataCenter.Singleton().wayList = tmpArray;
        }
    }


    /**
     * 환승버스 경로 구하기
     * @param deptArrivalList 출발지 버스 리스트
     * @param destArrivalList 도착지 버스 리스트
     */
    JSONArray transportWayList(JSONArray deptArrivalList, JSONArray destArrivalList) {
        JSONArray result = new JSONArray();

        JSONArray deptStationList = new JSONArray();
        JSONArray destStationList = new JSONArray();

        //1. 출발지 버스 리스트에서는 출발지부터 경유 정거장을 순차적으로 탐색
        DataCenter.Singleton().accessStationList.clear();
        for (Object o : deptArrivalList) {
            JSONObject deptBus = (JSONObject) o;

            int arrTime = Integer.parseInt(deptBus.get("arrtime").toString());
            if (arrTime > 600) {
                continue;
            }

            getRouteAcctoThrghSttnList(pathInfo.cityCode, deptBus.get("routeid").toString());
            deptStationList.addAll(DataCenter.Singleton().accessStationList);

        }

        //2. 도착지 버스 리스트에서는 도착지부터 경유 정거장을 역순으로 탐색
        DataCenter.Singleton().accessStationList.clear();
        for(int i = destArrivalList.size()-1; i >= 0; i--){
            JSONObject destBus = (JSONObject)destArrivalList.get(i);

            int arrTime = Integer.parseInt(destBus.get("arrtime").toString());
            if(arrTime > 600){
                continue;
            }

            getRouteAcctoThrghSttnList(pathInfo.cityCode, destBus.get("routeid").toString());
            JSONArray destBusStationList = new JSONArray();
            destBusStationList.addAll(DataCenter.Singleton().accessStationList);


            for(int j = destBusStationList.size()-1; j >= 0; j--){
                destStationList.add(destBusStationList.get(j));
            }

        }

        //3. 만나지 않으면 fail
        if(deptStationList.isEmpty() || destStationList.isEmpty()){
            System.out.println("가는 버스 찾을 수 없음");
            return result;
        }

        //4. 두 버스가 만나는 지점이 환승 지점
        for(int i = 0; i < deptStationList.size(); i++){
            for(int j = 0; j < destStationList.size(); j++){
                JSONObject dept = (JSONObject)deptStationList.get(i);
                JSONObject dest = (JSONObject)destStationList.get(j);

                //두 정거장 이름이 같은 경우 : 환승지점인 경우
                if(dept.get("nodenm").toString().equals(dest.get("nodenm").toString())){
                    //출발지~환승지역까지 노선 리스트 생성
                    JSONArray deptToTransList = new JSONArray();
                    for(int k = 0; k <= i; k++){
                        deptToTransList.add(deptStationList.get(k));
                    }

                    //환승지역~도착지까지 노선 리스트 생성
                    JSONArray transToDestList = new JSONArray();
                    for(int k = j; k >= 0; k--) {
                        transToDestList.add(destStationList.get(k));
                    }

                    //두 리스트를 JSON으로 묶어서 객체로 만들고 JSONArray로 감싸기
                    JSONObject way = new JSONObject();
                    way.put("first", deptToTransList);
                    way.put("second", transToDestList);

                    result.add(way);
                }
            }
        }
        return result;
    }


    void getCoordinate(String cityName, String place) {

        try {
            StringBuilder url = new StringBuilder("https://dapi.kakao.com/v2/local/search/keyword.json");
            url.append("?" + URLEncoder.encode("query","UTF-8") + "=" + URLEncoder.encode(place,"UTF-8"));
            String auth = "KakaoAK " + "2c6d565839233668c5eda759b5ecdc4d";

            URL Url = new URL( url.toString());
            HttpsURLConnection conn = (HttpsURLConnection) Url.openConnection();
            conn.setRequestMethod( "GET" );
            conn.setRequestProperty( "Authorization", auth );

            BufferedReader br;

            int responseCode = conn.getResponseCode();
            if( responseCode == 200 ) {  // 호출 OK
                br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {  // 에러
                br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            //API 요청


            DataCenter.Singleton().placeCoordinate.clear();


            String jsonString = "";
            String stringLine;
            while ((stringLine= br.readLine()) != null ) {
                jsonString += stringLine;
            }
            //요청받은 JSON 파일 문자열 변환


            JSONParser jsonParser = new JSONParser();
            //다시 JSON으로 파싱

            JSONObject parsejson = (JSONObject) jsonParser.parse(jsonString);

            JSONArray regionArray = (JSONArray) parsejson.get("documents");
            //documents에 해당하는 리스트 요소들을 얻어옴


            for(int i = 0; i < regionArray.size(); i++) {
                JSONObject object = (JSONObject) regionArray.get(i);

                if(object.get("road_address_name").toString().contains(cityName)) {
                    DataCenter.Singleton().placeCoordinate.put("longitude", object.get("x"));
                    DataCenter.Singleton().placeCoordinate.put("latitude", object.get("y"));
                    //결과 데이터에 좌표값 추가
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //#endregion
}
