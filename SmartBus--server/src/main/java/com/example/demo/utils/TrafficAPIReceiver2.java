package com.example.demo.utils;

import com.example.demo.controller.PathInfo;
import com.example.demo.datacenter.DataCenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

public class TrafficAPIReceiver2 extends Thread{
    private APIHandler APIHandler;
    PathInfo pathInfo;

    public boolean isDone = false;

    public TrafficAPIReceiver2(APIHandler APIHandler){
        this.APIHandler = APIHandler;
    }

    @Override
    public void run() {
        super.run();

        switch (APIHandler) {
            case TEST_FIND_WAY:
                getTestPathList(pathInfo.cityCode, pathInfo.deptId, pathInfo.destId);
        }
    }

    //#region 공통 메서드


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

        isDone = true;
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

    /**
     * 정류소별 도착예정 정보목록 조회
     * @param cityCode 도시 코드
     * @param nodeId 정류소 ID
     */
    void getSttnAcctoArvlPrearngeInfoList(String cityCode, String nodeId){
        try {
            System.out.println("cityCode : " + cityCode + " Node Id : " + nodeId);

            //요청 URL 정의
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);

            //데이터 파싱
            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().arrivalList.clear();

            //리스트에 삽입
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if(Integer.parseInt(getValue("arrtime", element)) >= 900){
                        continue;
                    }

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

            //arrtime기준으로 정렬
            Collections.sort(DataCenter.Singleton().arrivalList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject json1, JSONObject json2) {
                    String key = "arrtime";
                    Integer json1arrtime = new Integer(0);
                    Integer json2arrtime = new Integer(0);
                    try{
                        json1arrtime = Integer.parseInt(json1.get(key).toString());
                        json2arrtime = Integer.parseInt(json2.get(key).toString());
                    }
                    catch(Exception e){
                        e.getStackTrace();
                    }
                    return json1arrtime.compareTo(json2arrtime);
                }
            });

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }

    }


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
                    json.put("nodeord", Integer.parseInt(getValue("nodeord", element)));
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

    //#endregion

    void getTestPathList(String cityCode, String deptId, String destId) {

        DataCenter.Singleton().startNodeArrivalBusList.clear();
        getSttnAcctoArvlPrearngeInfoList(cityCode, deptId);

        DataCenter.Singleton().startNodeArrivalBusList.addAll(DataCenter.Singleton().arrivalList);


        DataCenter.Singleton().endNodeArrivalBusList.clear();
        getSttnAcctoArvlPrearngeInfoList(cityCode, destId);

        DataCenter.Singleton().endNodeArrivalBusList.addAll(DataCenter.Singleton().arrivalList);

        //출발지 도착 예정 버스가 존재하면 실행
        if(DataCenter.Singleton().startNodeArrivalBusList.size() == 0) {
            System.out.println("운행중인 버스 없음");
        }
        else {
            DataCenter.Singleton().directBusList.clear();

            //출발지 도착 예정 버스들과 도착지 도착 예정 버스들의 routeid 비교
            for(int i = 0; i < DataCenter.Singleton().startNodeArrivalBusList.size(); i++) {

                JSONObject obj = (JSONObject) DataCenter.Singleton().startNodeArrivalBusList.get(i);
                String startRouteId = obj.get("routeid").toString();

                for(int j = 0; j < DataCenter.Singleton().endNodeArrivalBusList.size(); j++) {

                    JSONObject obj2 = (JSONObject) DataCenter.Singleton().endNodeArrivalBusList.get(j);
                    String endRouteId = obj2.get("routeid").toString();

                    int startArrTime =  Integer.parseInt(obj.get("arrtime").toString());
                    int endArrTime = Integer.parseInt(obj2.get("arrtime").toString());

                    //두 routeid가 같고 도착지 도착 예정 시간이 더 크면 수행
                    if(startRouteId.equals(endRouteId)) {

                        //총 소요시간 계산
                        int totalTime = endArrTime - startArrTime;

                        if(totalTime < 0) {
                            obj.put("totaltime", "소요시간 알 수 없음");
                        }
                        else {
                            obj.put("totaltime", String.valueOf(totalTime));
                        }

                        DataCenter.Singleton().directBusList.add(obj);
                        break;
                    }

                }
            }
        }

        System.out.println("++++++++operation result++++++++");


        DataCenter.Singleton().finaldirectPathList.clear();
        DataCenter.Singleton().finalPathList.clear();

        //직통으로 가는 버스리스트가 비어있지 않으면 수행
        if(DataCenter.Singleton().directBusList.size() == 0) {
            System.out.println("직통으로 가는 버스 없음");

            transportation(cityCode, deptId, destId, DataCenter.Singleton().startNodeArrivalBusList, DataCenter.Singleton().endNodeArrivalBusList);

            isDone = true;
        }

        else {


            DataCenter.Singleton().accessStationList.clear();


            for(int i = 0; i < DataCenter.Singleton().directBusList.size(); i++) {

                JSONObject obj = (JSONObject) DataCenter.Singleton().directBusList.get(i);

                //직통 버스들의 routeno, arrtime, totaltime을 최종 결과 리스트에 삽입하기 위해 추출
                String directRouteId = obj.get("routeid").toString();
                String directRouteno = obj.get("routeno").toString();
                String directArrivalTime = obj.get("arrtime").toString();
                String directTotalTime = obj.get("totaltime").toString();

                getRouteAcctoThrghSttnList(cityCode, directRouteId);

                int startNodeOrd = 0;
                int endNodeOrd = 0;


                //버스 노선이 경유하는 모든 정류장 리스트에서 출발지 정류장과 매칭되는 정류장 순서를 integer 형으로 추출
                for(int j = 0; j < DataCenter.Singleton().accessStationList.size(); j++) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().accessStationList.get(j);
                    String pathNodeId = pathObj.get("nodeid").toString();

                    if(pathNodeId.equals(deptId)) {
                        startNodeOrd = (Integer) pathObj.get("nodeord");
                        break;
                    }
                }

                //버스 노선이 경유하는 모든 정류장 리스트에서 도착지 정류장과 매칭되는 정류장 순서를 integer 형으로 추출
                for(int j = DataCenter.Singleton().accessStationList.size() - 1; j >= 0; j--) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().accessStationList.get(j);
                    String pathNodeId = pathObj.get("nodeid").toString();

                    if(pathNodeId.equals(destId)) {
                        endNodeOrd = (Integer) pathObj.get("nodeord");
                        break;
                    }
                }


                //위에서 추출한 직통 버스들의 routeno, arrtime, totaltime을 최종 결과 리스트에 삽입


                DataCenter.Singleton().directPathList.clear();


                //출발지 정류장 순서부터 도착지 정류장 순서 안에있는 정류장들을 경로로 지정 후 리스트에 삽입
                for(int j = 0; j < DataCenter.Singleton().accessStationList.size(); j++) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().accessStationList.get(j);

                    int pathOrd = (Integer) pathObj.get("nodeord");

                    if(startNodeOrd <= pathOrd && pathOrd <= endNodeOrd) {
                        DataCenter.Singleton().directPathList.add(pathObj);
                    }
                }

                JSONObject routeNumObj = new JSONObject();
                routeNumObj.put("routeid", directRouteId);
                routeNumObj.put("routeno", directRouteno);
                routeNumObj.put("arrtime", directArrivalTime);
                routeNumObj.put("totaltime", directTotalTime);
                routeNumObj.put("pathStationList", DataCenter.Singleton().directPathList);

                if(startNodeOrd > endNodeOrd) {
                    break;
                }
                JSONArray pathDetail = new JSONArray();
                JSONObject subPath = new JSONObject();

                pathDetail.add(routeNumObj);

                subPath.put("subpath", pathDetail);


                DataCenter.Singleton().finaldirectPathList.add(subPath);

            }
            DataCenter.Singleton().finalPathList = DataCenter.Singleton().finaldirectPathList;



            isDone = true;
        }
    }

    public void transportation(String cityCode, String startNodeid, String endNodeid, JSONArray startNodeArrivalBusList, JSONArray endNodeArrivalBusList) {
        JSONArray tmpStartNodeArrivalBusList = new JSONArray();
        JSONArray tmpEndNodeArrivalBusList = new JSONArray();

        if(startNodeArrivalBusList.size() < 3) {
            tmpStartNodeArrivalBusList = startNodeArrivalBusList;
        }
        else {
            for(int i = 0; i < 2; i++) {
                JSONObject tmpObj = (JSONObject) startNodeArrivalBusList.get(i);
                tmpStartNodeArrivalBusList.add(tmpObj);
            }
        }

        if(endNodeArrivalBusList.size() < 3) {
            tmpEndNodeArrivalBusList = endNodeArrivalBusList;
        }
        else {
            for(int i = 0; i < 2; i++) {
                JSONObject tmpObj = (JSONObject) endNodeArrivalBusList.get(i);
                tmpEndNodeArrivalBusList.add(tmpObj);
            }
        }

        DataCenter.Singleton().startBusThroughList.clear();
        DataCenter.Singleton().endBusThroughList.clear();
        JSONObject putTmp1 = new JSONObject();
        JSONObject putTmp2 = new JSONObject();


        for(int i = 0; i < tmpStartNodeArrivalBusList.size(); i++) {
            JSONObject obj = (JSONObject) tmpStartNodeArrivalBusList.get(i);

            String currentRouteId = obj.get("routeid").toString();

            getRouteAcctoThrghSttnList(cityCode, currentRouteId);
            putTmp1.put("through", DataCenter.Singleton().accessStationList);
        }

        DataCenter.Singleton().startBusThroughList.add(putTmp1);

        for(int i = 0; i < tmpEndNodeArrivalBusList.size(); i++) {
            JSONObject obj = (JSONObject) tmpEndNodeArrivalBusList.get(i);

            String currentRouteId = obj.get("routeid").toString();
        }





    }

}
