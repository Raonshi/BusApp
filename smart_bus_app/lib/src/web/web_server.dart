import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/data/city_item.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_station_item.dart';
import 'package:smart_bus_app/src/provider/departure_provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

enum SearchType{
  BUS_NUMBER,
  STATION_NAME
}

class WebServer {
  final String ip = "127.0.0.1";
  final String port = "8080";
  final cityName = "청주";

  static final WebServer _instance = WebServer.init();

  factory WebServer(){
    return _instance;
  }

  WebServer.init(){
    Logger().d("WebServer has created!!!");
  }

  /// 도시 지원 여부를 확인하고 지원되는 도시일 경우 City객체를 반환한다.
  Future<bool> citySupport(String cityName) async {
    final address = ip + ":" + port;
    final service = "/getCity";
    final params = {
      "cityName" : cityName,
    };
    Uri uri = Uri.http(address, service, params);
    dynamic jsonString = await request(uri);
    return jsonString != null;
  }


  /// 검색창에 입력한 검색어를 통해 노선과 정류장을 찾는다.
  void searchKeyword(String keyword, SearchType type) {
    if(citySupport(cityName) == false){
      return;
    }

    switch(type){
      case SearchType.BUS_NUMBER:
        searchBusNum(keyword);
        break;
      case SearchType.STATION_NAME:
        searchStation(keyword);
        break;
    }
  }


  /// 버스 정류장을 검색한다.
  void searchStation(String station) async {
    final address = ip + ":" + port;
    final service = "/searchStation";
    final params = {
      "cityName" : cityName,
      "station" : station
    };
    Uri uri = Uri.http(address, service, params);
    dynamic jsonString = await request(uri);
  }


  /// 버스 노선 번호를 검색한다.
  void searchBusNum(String busNum) async {
    final address = ip + ":" + port;
    final service = "/searchBus";
    final params = {
      "cityName" : cityName,
      "busNum" : busNum
    };
    Uri uri = Uri.http(address, service, params);
    dynamic jsonString = await request(uri);
  }


  /// 출발지와 도착지 사이의 경로를 요청한다.


  /// 즐겨찾기에 등록한다.


  /// 즐겨찾기에서 삭제한다.

  
  /// 최근기록에 등록한다.


  /// 서버에 메시지를 요청한 뒤 응답을 반환한다.
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
}