package com.example.demo.utils;

import com.example.demo.controller.RegionInfo;
import com.example.demo.datacenter.DataCenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DustAPIReceiver extends Thread{

    private APIHandler APIHandler;
    RegionInfo regionInfo;

    public DustAPIReceiver(APIHandler APIHandler) {
        this.APIHandler = APIHandler;
    }

    @Override
    public void run() {
        super.run();

        switch(APIHandler) {
            case REGION_DUST_INFO:
                getDustInfo(regionInfo.latitude, regionInfo.longitude);
                break;
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

    void getDustInfo(String latitude, String longitude) {
        getlocationTransfer(latitude, longitude);

        ArrayList<String> tmxlist = DataCenter.Singleton().tmX;
        ArrayList<String> tmylist = DataCenter.Singleton().tmY;
        DataCenter.Singleton().dustList.clear();
        for(int i = 0; i < tmxlist.size(); i++) {
            getStationName(tmxlist.get(i), tmylist.get(i));
            System.out.println(tmxlist.get(i) + " " +  tmylist.get(i));
            String stationName = DataCenter.Singleton().stationNameList.get(i);
            System.out.println(stationName);

            try {
                StringBuilder url = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
                url.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=xKQb85gLMzaWSIlF7L%2BADI5652d752CRPIul%2FAlv5KLdQLJYl4eMRLZ25kSnDA0dvj2iYXfceDNWcPB7j4%2BdiA%3D%3D");
                //url.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=xml");
                url.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=100");
                url.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
                url.append("&" + URLEncoder.encode("stationName", "UTF-8") + "=" + URLEncoder.encode(stationName, "UTF-8"));
                url.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "=DAILY");
                //url.append("&" + URLEncoder.encode("ver", "UTF-8") + "=1.0");

                NodeList list = getData(url.toString());

                for(int j = 0; j < list.getLength(); j++) {
                    JSONObject json = new JSONObject();
                    Node node = list.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        json.put("stationName", stationName);
                        json.put("pm10Value", getValue("pm10Value", element));
                        json.put("dataTime", getValue("dataTime", element));

                        DataCenter.Singleton().dustList.add(json);
                    }

                }


            }catch(Exception e) {
                e.printStackTrace();
            }

        }

    }

    //위도 경도 - > TM_X, TM_Y 좌표 변환
    void getlocationTransfer(String latitude, String longitude) {

        try {
            DataCenter.Singleton().tmX.clear();
            DataCenter.Singleton().tmY.clear();

            StringBuilder url = new StringBuilder("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json");
            url.append("?" + URLEncoder.encode("x","UTF-8") + "=" + URLEncoder.encode(longitude,"UTF-8"));
            url.append("&" + URLEncoder.encode("y","UTF-8") + "=" + URLEncoder.encode(latitude,"UTF-8"));
            url.append("&" + URLEncoder.encode("output_coord","UTF-8") + "=TM");
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

            String jsonString = "";
            String stringLine;
            while ((stringLine= br.readLine()) != null ) {
                jsonString += stringLine;
            }

            JSONParser jsonParser = new JSONParser();

            JSONObject parsejson = (JSONObject) jsonParser.parse(jsonString);

            JSONArray regionArray = (JSONArray) parsejson.get("documents");

            for(int i = 0; i < regionArray.size(); i++) {
                JSONObject json = (JSONObject) regionArray.get(i);

                Double doubleTmx = (Double) json.get("x");
                Double doubleTmy = (Double) json.get("y");

                DataCenter.Singleton().tmX.add(String.valueOf(doubleTmx));
                DataCenter.Singleton().tmY.add(String.valueOf(doubleTmy));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getStationName(String tmX, String tmY) {

        try {
            DataCenter.Singleton().stationNameList.clear();

            StringBuilder url = new StringBuilder("http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList");
            url.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=xKQb85gLMzaWSIlF7L%2BADI5652d752CRPIul%2FAlv5KLdQLJYl4eMRLZ25kSnDA0dvj2iYXfceDNWcPB7j4%2BdiA%3D%3D");
            url.append("&" + URLEncoder.encode("tmX", "UTF-8") + "=" + URLEncoder.encode(tmX, "UTF-8"));
            url.append("&" + URLEncoder.encode("tmY", "UTF-8") + "=" + URLEncoder.encode(tmY, "UTF-8"));

            NodeList list = getData(url.toString());

            for(int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    DataCenter.Singleton().stationNameList.add(getValue("stationName", element));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


}
