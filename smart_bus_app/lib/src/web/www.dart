import 'dart:convert';
import 'package:smart_bus_app/src/data/bus_position_information/approach_info_item.dart';
import 'package:smart_bus_app/src/data/city_item.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_info_item.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_no_item.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_station_item.dart';
import 'package:smart_bus_app/src/data/bus_position_information/position_item.dart';
import 'package:xml/xml.dart';

import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';


class WWW{
  ///서버에 메시지를 요청한 뒤 응답을 반환한다.
  Future<dynamic> request(Uri uri) async {

    http.Response response = await http.get(uri);

    if(response.statusCode == 200) {
      final document = utf8.decode(response.bodyBytes);
      final jsonString = jsonDecode(document);
      return Future.delayed(Duration(milliseconds: 100), () => jsonString);
    }
    else{
      Logger().d("Fail : ${response.statusCode}");
    }
  }
  /*
   더이상 사용하지 않음

//#region 공통 API
  ///도시코드목록조회
  ///공공데이터포털에서 제공하는 버스노선 지원 도시 목록을 조회한다.
  Future<List<CityItem>> getCityCodeList(int type) async {
    final address = "openapi.tago.go.kr";

    String serviceName = "";
    switch(type){
      case 0 :
        serviceName = "/openapi/service/BusRouteInfoInqireService/getCtyCodeList";
        break;
      case 1 :
        serviceName = "/openapi/service/BusLcInfoInqireService/getCtyCodeList";
        break;
    }

    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ=="
    };
    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    List<CityItem> list = List();

    final items = xml.findAllElements('item');
    items.map((e) => e).forEach((element) {
      list.add(CityItem(element.getElement('citycode').text, element.getElement('cityname').text));
    });

    return Future.delayed(Duration(milliseconds: 100), () => list);
  }
//#endregion

//#region 국토교통부-버스노선정보 API
  ///노선정보항목조회
  ///노선 하나에 대하여 기본정보를 조회한다.
  Future<RouteInfoItem> getRouteInfo(String cityCode, String routeId) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusRouteInfoInqireService/getRouteInfoIem";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "cityCode" : cityCode,
      "routeId" : routeId
    };
    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    String id = xml.getElement('routeid') != null ? xml.getElement('routeid').text : '미지원';
    String no = xml.getElement('routeno') != null ? xml.getElement('routeno').text : '미지원';
    String type = xml.getElement('routetp') != null ? xml.getElement('routetp').text : '미지원';
    String startTime = xml.getElement('startvehicletime') != null ? xml.getElement('startvehicletime').text : '미지원';
    String endTime = xml.getElement('endvehicletime') != null ? xml.getElement('endvehicletime').text : '미지원';
    String startNode = xml.getElement('startnodenm') != null ? xml.getElement('startnodenm').text : '미지원';
    String endNode = xml.getElement('endnodenm') != null ? xml.getElement('endnodenm').text : '미지원';
    String interval = xml.getElement('intervaltime') != null ? xml.getElement('intervaltime').text : '미지원';
    String intervalSat = xml.getElement('intervalsattime') != null ? xml.getElement('intervalsattime').text : '미지원';
    String intervalSun = xml.getElement('intervalsuntime') != null ? xml.getElement('intervalsuntime').text : '미지원';

    RouteInfoItem item = RouteInfoItem(id, no, type, startNode, endNode, startTime, endTime, interval, intervalSat, intervalSun);

    return Future.delayed(Duration(milliseconds: 100), () => item);
  }


  ///노선번호목록조회
  ///버스 노선번호의 목록을 조회한다.
  Future<List<RouteNoItem>> getRouteNoList(String cityCode, String routeNo) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusRouteInfoInqireService/getRouteNoList";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "cityCode" : cityCode,
      "routeNo" : routeNo
    };

    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    //Logger().d("XML : ${xml.toXmlString(pretty: true, indent: '\t')}");
    List<RouteNoItem> list = List();

    final items = xml.findAllElements('item');
    items.map((e) => e).forEach((element) {
      String id = element.getElement('routeid') != null ? element.getElement('routeid').text : '미지원';
      String no = element.getElement('routeno') != null ? element.getElement('routeno').text : '미지원';
      String type = element.getElement('routetp') != null ? element.getElement('routetp').text : '미지원';
      String startTime = element.getElement('startvehicletime') != null ? element.getElement('startvehicletime').text : '미지원';
      String endTime = element.getElement('endvehicletime') != null ? element.getElement('endvehicletime').text : '미지원';
      String startNode = element.getElement('startnodenm') != null ? element.getElement('startnodenm').text : '미지원';
      String endNode = element.getElement('endnodenm') != null ? element.getElement('endnodenm').text : '미지원';

      list.add(RouteNoItem(id, no, type, startTime, endTime, startNode, endNode));
    });

    return Future.delayed(Duration(milliseconds: 100), () => list);
  }


  ///노선별 경유 정류소 목록 조회
  ///노선별로 경유하는 정류장의 목록을 조회한다
  Future<List<RouteStationItem>> getRouteAccToThrghSttnList(int numOfRows, int pageNo, String cityCode, String routeId) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "numOfRows" : numOfRows.toString(),
      "pageNo" : pageNo.toString(),
      "cityCode" : cityCode,
      "routeId" : routeId
    };

    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    List<RouteStationItem> list = List();

    final items = xml.findAllElements('item');
    items.map((e) => e).forEach((element) {
      String nodeId = element.getElement('nodeid') != null ? element.getElement('nodeid').text : '미지원';
      String nodeName = element.getElement('nodenm') != null ? element.getElement('nodenm').text : '미지원';
      String nodeOrder = element.getElement('nodeord') != null ? element.getElement('nodeord').text : '미지원';
      String nodeNum = element.getElement('nodeno') != null ? element.getElement('nodeno').text : '미지원';
      String xPos = element.getElement('gpslong') != null ? element.getElement('gpslong').text : '미지원';
      String yPos = element.getElement('gpslati') != null ? element.getElement('gpslati').text : '미지원';
      String direction = element.getElement('updowncd') != null ? element.getElement('updowncd').text : '미지원';

      list.add(RouteStationItem(routeId, nodeId, nodeName, nodeOrder, nodeNum, xPos, yPos, direction));
    });

    return Future.delayed(Duration(milliseconds: 100), () => list);
  }
//#endregion

//#region 국토교통부-버스위치정보 API
  ///노선별 버스 위치 목록 조회
  ///노선별로 버스의 GPS위치정보의 목록을 조회한다
  Future<List<PositionItem>> getRouteAcctoBusLcList(String cityCode, String routeId) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "cityCode" : cityCode,
      "routeId" : routeId
    };

    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    List<PositionItem> list = List();

    final items = xml.findAllElements('item');
    items.map((e) => e).forEach((element) {
      String routeName = element.getElement('routenm') != null ? element.getElement('routenm').text : '미지원';
      String routeType = element.getElement('routetp') != null ? element.getElement('routetp').text : '미지원';
      String nodeId = element.getElement('nodeid') != null ? element.getElement('nodeid').text : '미지원';
      String nodeName = element.getElement('nodenm') != null ? element.getElement('nodenm').text : '미지원';
      String nodeOrder = element.getElement('nodeord') != null ? element.getElement('nodeord').text : '미지원';
      String xPos = element.getElement('gpslong') != null ? element.getElement('gpslong').text : '미지원';
      String yPos = element.getElement('gpslati') != null ? element.getElement('gpslati').text : '미지원';
      String vehicleNum = element.getElement('vehicleno') != null ? element.getElement('vehicleno').text : '미지원';

      list.add(PositionItem(routeName, routeType, xPos, yPos, nodeId, nodeName, nodeOrder, vehicleNum));
    });

    return Future.delayed(Duration(milliseconds: 100), () => list);
  }


  ///노선별 특정 정류소 접근 버스 위치 정보 조회
  ///특정정류소에 근접한 버스의 GPS위치정보를 조회한다
  Future<List<ApproachItem>> getRouteAcctoSpcifySttnBusLcInfo(String routeId, String nodeId, String cityCode) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusLcInfoInqireService/getRouteAcctoSpcifySttnAccesBusLcInfo";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "routeId" : routeId,
      "nodeId" : nodeId,
      "cityCode" : cityCode,
    };

    Uri uri = Uri.http(address, serviceName, params);
    var xml = await _getData(uri);

    List<ApproachItem> list = List();

    final items = xml.findAllElements('item');
    items.map((e) => e).forEach((element) {
      String routeName = element.getElement('routenm') != null ? element.getElement('routenm').text : '미지원';
      String routeType = element.getElement('routetp') != null ? element.getElement('routetp').text : '미지원';
      String nodeName = element.getElement('nodenm') != null ? element.getElement('nodenm').text : '미지원';
      String xPos = element.getElement('gpslong') != null ? element.getElement('gpslong').text : '미지원';
      String yPos = element.getElement('gpslati') != null ? element.getElement('gpslati').text : '미지원';

      list.add(ApproachItem(routeName, routeType, nodeName, xPos, yPos));
    });

    return Future.delayed(Duration(milliseconds: 100), () => list);
  }

//#endregion




  ///[uri]를 통해 요청한 공공데이터를 반환한다.
  Future<XmlDocument> _getData(Uri uri) async {
    http.Response response = await http.get(uri);

    if(response.statusCode == 200) {
      final document = utf8.decode(response.bodyBytes);
      final xml = XmlDocument.parse(document);
      return Future.delayed(Duration(milliseconds: 100), () => xml);
    }
    else{
      Logger().d("Fail : ${response.statusCode}");
      return XmlDocument.parse("Failed");
    }
  }

   */
}