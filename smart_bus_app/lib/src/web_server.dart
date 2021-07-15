import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/serialization/city.dart';

import 'data/data.dart';


class WebServer {
  //#region Singleton Pattern
  static WebServer _instance = WebServer.init();

  factory WebServer(){
    return _instance;
  }

  WebServer.init(){
    Logger().d("WebServerCreated!");
  }

  //#endregion

  ///접속 주소
  final endpoint = "127.0.0.1:8080";

  ///지정된 도시의 버스 노선 정보 중 [busNum]이 포함되는 모든 버스번호를 불러온다.
  Future<List> getBusList(String busNum) async {
    final service = "getBusList";
    final params = {
      "cityName": "청주",
      "routeNo": busNum
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await request(uri);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Bus.fromJson(e)).toList();

    return list;
  }

  ///지정된 도시의 버스정류장 정보 중 [station]이 포함되는 모든 정류장을 불러온다.
  Future<List> getStationList(String station) async {
    final service = "getStationList";
    final params = {
      "cityName": "청주",
      "routeNo": station
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await request(uri);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Station.fromJson(e)).toList();

    return list;
  }


  /// [uri]로 전달 받은 주소를 통해 데이터를 받아온다.
  Future<String> request(Uri uri) async {
    http.Response response = await http.get(uri);

    if(response.statusCode == 200){
      Logger().d("Success");
      return utf8.decode(response.bodyBytes);
    }
    else{
      Logger().d("Failed : ${response.statusCode}");
    }
  }
}