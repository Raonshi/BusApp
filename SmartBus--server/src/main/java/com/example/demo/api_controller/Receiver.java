package com.example.demo.api_controller;

import com.example.demo.data.DataController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Receiver {

    public void getCityList(){
        String endPoint = "http://openapi.tago.go.kr/openapi/service";
        String service = "BusRouteInfoInqireService/getCtyCodeList";
        String serviceKey = "jQtEtCvhFPgTRrmSxikfgvg1fMV%2FH19VWwaxeLb3X%2BfiVfNhWybyEsq%2FTnv1uQtBMITUQNlWlBPaV3lqr3pTHQ%3D%3D&";

        String url = endPoint + "/" + service + "?serviceKey=" + serviceKey;

        getData(url);
    }

    public void getData(String url) {
        try{
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url);

            document.getDocumentElement().normalize();
            System.out.println("Response : " + document.getDocumentElement().getNodeValue());

            NodeList list = document.getElementsByTagName("item");

            for(int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    DataController.Singleton().cityTable.put(
                            getValue("cityname", element),
                            getValue("citycode", element)
                    );
                }
            }
        }
        catch (ParserConfigurationException e){
            System.out.println(e.getMessage());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String tag, Element element){
        NodeList list = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node)list.item(0);

        if(node == null){
            return null;
        }
        return node.getNodeValue();
    }
}
