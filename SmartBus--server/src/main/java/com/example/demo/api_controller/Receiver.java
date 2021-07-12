package com.example.demo.api_controller;

import com.example.demo.Function;
import com.example.demo.data.*;
import com.example.demo.api_controller.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class Receiver extends Thread{
    private Function _function;
    RouteInfo rouif;
    
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
                //테스트 값
                //getRouteInfoItem(rouif);
                break;
            case ROUTE_NUMBER_LIST:
                getRouteAcctoThrghSttnList(10, 1, "25", "DJB30300052");
                break;
            case ROUTE_THROUGH_STATION_LIST:

                break;
            case LOCATION_CITY_LIST:

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

                break;
        }
    }


    //#region 국토교통부-버스노선정보 API
    /**
     * <p>도시코드 목록 조회</p>
     * <p>서비스 가능 지역들의 도시코드 목록을 조회한 뒤 {@link City}객체에 저장한다.</p>
     * <p>{@link City}객체는 {@link DataController}클래스의 cityList에 저장된다.</p>
     */
    void getCityList(){
        String endPoint = "http://openapi.tago.go.kr/openapi/service";
        String service = "BusRouteInfoInqireService/getCtyCodeList";
        String serviceKey = "jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D&";

        String url = endPoint + "/" + service + "?serviceKey=" + serviceKey;

        try{
            NodeList list = getData(url);

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    City item = new City(getValue("citycode", element), getValue("cityname", element));
                    DataController.Singleton().cityList.add(item);
                    //System.out.println(item.get_cityName());
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
    public void getRouteInfoItem(RouteInfo rouif){
        String endPoint = "http://openapi.tago.go.kr/openapi/service";
        String service = "BusRouteInfoInqireService/getRouteInfoIem";
        String serviceKey = "jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D&";
        String cityCode = rouif.get_in_cityCode();
        String routeId = rouif.get_in_routeId();
        
        String url = endPoint + "/" + service + "?serviceKey=" + serviceKey + "&cityCode=" + cityCode + "&routeId=" + routeId;

        try{
            NodeList list = getData(url);
            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    RouteInfo item = new RouteInfo(
                            getValue("routeid", element),
                            getValue("routeno", element),
                            getValue("routetp", element),
                            getValue("startnodenm", element),
                            getValue("endnodenm", element),
                            getValue("startvehicletime", element),
                            getValue("endvehicletime", element),
                            getValue("intervaltime", element),
                            getValue("intervalsattime", element),
                            getValue("intervalsuntime", element)
                            );
                    DataController.Singleton().routeInfoList.add(item);
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
     * @param numOfRows 한 페이지에 출력할 데이터 개수
     * @param pageNo 출력할 페이지 번호
     * @param cityCode 각 도시별로 부여된 고유한 아이디 값
     * @param routeId 각 노선별로 부여된 고유한 아이디 값
     */
    void getRouteAcctoThrghSttnList(int numOfRows, int pageNo, String cityCode, String routeId){
        String endPoint = "http://openapi.tago.go.kr/openapi/service";
        String service = "BusRouteInfoInqireService/getRouteAcctoThrghSttnList";
        String serviceKey = "jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D&";
        String params = numOfRows + "&" + pageNo + "&" + cityCode + "&" + routeId;

        String url = endPoint + "/" + service + "?serviceKey=" + serviceKey + "?" + params;

        try{
            NodeList list = getData(url);
            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    AccessStation item = new AccessStation(
                            getValue("routeid", element),
                            getValue("nodeid", element),
                            getValue("nodenm", element),
                            getValue("nodeord", element),
                            getValue("nodeno", element),
                            getValue("gpslong", element),
                            getValue("gpslati", element),
                            getValue("updowncd", element)
                    );
                    DataController.Singleton().accessStationList.add(item);
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
     * @param routeNum 버스를 식별할 수 있는 노선 번호(예 : 502, 10-1, 811-2)
     */
    void getRouteNoList(String cityCode, String routeNum){
        String endPoint = "http://openapi.tago.go.kr/openapi/service";
        String service = "BusRouteInfoInqireService/getRouteNoList";
        String serviceKey = "jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D&";

        String url = endPoint + "/" + service + "?serviceKey=" + serviceKey;

        try{
            NodeList list = getData(url);
            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    RouteNum item = new RouteNum(
                            getValue("routeid", element),
                            getValue("routeno", element),
                            getValue("routetp", element),
                            getValue("startnodenm", element),
                            getValue("endnodenm", element),
                            getValue("startvehicletime", element),
                            getValue("endvehicletime", element)
                    );
                    DataController.Singleton().routeNumList.add(item);
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
        NodeList list = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node)list.item(0);

        if(node == null){
            return null;
        }
        return node.getNodeValue();
    }

    //#endregion
}
