package com.example.springboottest1.utils;

import com.example.springboottest1.data.DataCenter;
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
import java.util.Collections;

public class Ops {

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



    public void testCall(String startNodeid, String endNodeid) {

        int count = 0;

        //출발 지 정류장에서 도착 예정인 버스들 리스트 뽑기
        request(startNodeid);
        DataCenter.Singleton().startNodeArrivalBusList.clear();


        DataCenter.Singleton().startNodeArrivalBusList.addAll(DataCenter.Singleton().nodeArrivarBusList);
        System.out.println("==startNodeArrivalBusList==");
        for(int i = 0; i < DataCenter.Singleton().startNodeArrivalBusList.size(); i++) {
            System.out.println(DataCenter.Singleton().startNodeArrivalBusList.get(i));
        }

        System.out.println();


        //도착 지 정류장에서 도착 예정인 버스들 리스트 뽑기
        request(endNodeid);
        DataCenter.Singleton().endNodeArrivalBusList.clear();

        DataCenter.Singleton().endNodeArrivalBusList.addAll(DataCenter.Singleton().nodeArrivarBusList);
        System.out.println("==endNodeArrivalBusList==");
        for(int i = 0; i < DataCenter.Singleton().endNodeArrivalBusList.size(); i++) {
            System.out.println(DataCenter.Singleton().endNodeArrivalBusList.get(i));
        }

        System.out.println("====new ops====");



        if(DataCenter.Singleton().startNodeArrivalBusList.size() == 0) {
            System.out.println("운행중인 버스 없음");
        }
        else {
            DataCenter.Singleton().directBusList.clear();

            for(int i = 0; i < DataCenter.Singleton().startNodeArrivalBusList.size(); i++) {

                JSONObject obj = (JSONObject) DataCenter.Singleton().startNodeArrivalBusList.get(i);
                String startRouteId = obj.get("routeid").toString();

                for(int j = 0; j < DataCenter.Singleton().endNodeArrivalBusList.size(); j++) {

                    JSONObject obj2 = (JSONObject) DataCenter.Singleton().endNodeArrivalBusList.get(j);
                    String endRouteId = obj2.get("routeid").toString();

                    int startArrTime =  Integer.parseInt(obj.get("arrtime").toString());
                    int endArrTime = Integer.parseInt(obj2.get("arrtime").toString());

                    if(startRouteId.equals(endRouteId) &&  endArrTime > startArrTime) {

                        int totalTime = endArrTime - startArrTime;

                        obj.put("totaltime", String.valueOf(totalTime));

                        DataCenter.Singleton().directBusList.add(obj);
                        break;
                    }

                }
            }

        }
        System.out.println("}++++++++++++++++++++++++++++++++++++++++++{");
        for(int i = 0; i < DataCenter.Singleton().directBusList.size(); i++) {
            System.out.println(DataCenter.Singleton().directBusList.get(i));
        }

        System.out.println();
        System.out.println("++++++++operation result++++++++");

        if(DataCenter.Singleton().directBusList.size() == 0) {
            System.out.println("직통으로 가는 버스 없음");
        }
        else {
            DataCenter.Singleton().busPathList.clear();


            for(int i = 0; i < DataCenter.Singleton().directBusList.size(); i++) {

                JSONObject obj = (JSONObject) DataCenter.Singleton().directBusList.get(i);
                String directRouteId = obj.get("routeid").toString();
                String directRouteno = obj.get("routeno").toString();
                String directArrivalTime = obj.get("arrtime").toString();
                String directTotalTime = obj.get("totaltime").toString();

                request2(directRouteId);

                int startNodeOrd = 0;
                int endNoderOrd = 0;

                for(int j = 0; j < DataCenter.Singleton().busPathList.size(); j++) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().busPathList.get(j);
                    String pathNodeId = pathObj.get("nodeid").toString();

                    if(pathNodeId.equals(startNodeid)) {
                        startNodeOrd = (Integer) pathObj.get("nodeord");
                        break;
                    }
                }

                for(int j = 0; j < DataCenter.Singleton().busPathList.size(); j++) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().busPathList.get(j);
                    String pathNodeId = pathObj.get("nodeid").toString();

                    if(pathNodeId.equals(endNodeid)) {
                        endNoderOrd = (Integer) pathObj.get("nodeord");
                        break;
                    }
                }

                JSONObject routeNumObj = new JSONObject();
                routeNumObj.put("routeno", directRouteno);
                routeNumObj.put("arrtime", directArrivalTime);
                routeNumObj.put("totaltime", directTotalTime);

                DataCenter.Singleton().finaldirectPathList.add(routeNumObj);

                DataCenter.Singleton().directPathList.clear();

                for(int j = 0; j < DataCenter.Singleton().busPathList.size(); j++) {
                    JSONObject pathObj = (JSONObject) DataCenter.Singleton().busPathList.get(j);

                    int pathOrd = (Integer) pathObj.get("nodeord");

                    if(startNodeOrd <= pathOrd && pathOrd <= endNoderOrd) {
                        DataCenter.Singleton().directPathList.add(pathObj);
                    }

                }

                DataCenter.Singleton().finaldirectPathList.addAll(DataCenter.Singleton().directPathList);
            }
        }
        System.out.println("===========================================");

        for(int i = 0; i < DataCenter.Singleton().finaldirectPathList.size(); i++) {

            System.out.println(DataCenter.Singleton().finaldirectPathList.get(i));
        }




    }


    public void request(String nodeId) {
        String cityCode = "33010";

        try {
            //요청 URL 정의
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + nodeId);

            //데이터 파싱
            NodeList list = getData(urlBuilder.toString());
            DataCenter.Singleton().nodeArrivarBusList.clear();


            //리스트에 삽입
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (Integer.parseInt(getValue("arrtime", element)) >= 900) {
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

                    DataCenter.Singleton().nodeArrivarBusList.add(json);

                }
            }
        }catch (ParserConfigurationException | IOException | SAXException e) {
            e.getMessage();
        }
    }

    public void request2(String routeId) {
        String cityCode = "33010";

        try{
            StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + "100");
            urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + cityCode);
            urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + routeId);

            NodeList list = getData(urlBuilder.toString());

            DataCenter.Singleton().busPathList.clear();
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

                    DataCenter.Singleton().busPathList.add(json);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.getMessage();
        }
    }

}
