package com.example.demo.utils;

import com.example.demo.controller.RegionInfo;
import com.example.demo.datacenter.DataCenter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

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
                getRegionDust(regionInfo.sido);
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


    void getRegionDust(String sido) {

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=xKQb85gLMzaWSIlF7L%2BADI5652d752CRPIul%2FAlv5KLdQLJYl4eMRLZ25kSnDA0dvj2iYXfceDNWcPB7j4%2BdiA%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=xml");
            //urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=100");
            //urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=1");
            urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode(sido, "UTF-8"));
            //urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=1.0");

            String url = urlBuilder.toString();
            NodeList list = getData(url);

            DataCenter.Singleton().dustList.clear();

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    JSONObject json = new JSONObject();

                    json.put("sidoName", getValue("sidoName", element));
                    json.put("stationName", getValue("stationName", element));
                    json.put("pm10Value", getValue("pm10Value", element));

                    DataCenter.Singleton().dustList.add(json);
                }
            }


        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
