/// 본 스크립트는 서버와 통신하기 위한 함수 및 클래스가 정의되어 있다.
/// 작성자 : 홍순원

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';

import 'business_logic/data_define.dart';

///<h1>클라이언트 서버 접속 모듈</h1>
///<p>[WebServer]는 클라이언트가 서버에 접속하기 위해 이용하는 모듈이다.</p>
///<p></p>
///<p></p>
///<p></p>
///<p></p>
///<p></p>
///<p></p>
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
  //final endpoint = "220.86.224.184:12010";
  final endpoint = "10.0.2.2:8080";


  //#region GET

  ///서버로부터 미세먼지 정보를 가져온다. <br>
  ///params : String latitude, String logitude <br>
  ///return : List<Dust>
  Future<List> getDustInfo(double latitude, double longitude) async {

    Logger().d("Latitude : $latitude || Longitude : $longitude");

    final service = "getDustInfo";
    final params = {
      "latitude": latitude.toString(),
      "longitude": longitude.toString()
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Dust.fromJson(e)).toList();

    return list;
  }


  ///서버로부터 날씨 정보를 가져온다. <br>
  ///params : String latitude, String logitude <br>
  ///return : List<Weather>
  Future<List> getWeatherInfo(double latitude, double longitude) async {
    final service = "getWeatherInfo";
    final params = {
      "latitude": latitude.toString(),
      "longitude": longitude.toString()
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);

    var jsonArray = jsonDecode(jsonString) as List;

    List<dynamic> list = [];

    for(int i = 1; i < jsonArray.length; i++){
      list.add(Weather.fromJson(jsonArray[i]));
    }
    //List list = jsonArray.map((e) => Weather.fromJson(e)).toList();

    return list;
  }


  ///<h2>주변 정류장 정보 조회</h2>
  ///<p>[Controller]의 latitude, longitude정보를 기반으로 주변 정류장 값을 받아온다.</p>
  ///<p>params : double latitude, double longitude</p>
  ///<p>return : List<Station></p>
  Future<List> getStationByLocation(double latitude, double longitude) async {
    final service = "getBusList";
    final params = {
      "latitude": latitude.toString(),
      "longitude": longitude.toString()
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Station.fromJson(e)).toList();

    return list;
  }








  ///지정된 도시의 버스 노선 정보 중 [busNum]이 포함되는 모든 버스번호를 불러온다.
  Future<List> getBusList(String busNum) async {
    final service = "getBusList";
    final params = {
      "cityName": "청주",
      "routeNo": busNum
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Bus.fromJson(e)).toList();

    return list;
  }


  ///지정된 도시의 버스정류장 정보 중 [station]이 포함되는 모든 정류장을 불러온다.
  Future<List> getStationList(String station) async {
    final service = "getStationList";
    final params = {
      "cityName": "청주",
      "nodeNm": station
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);

    var jsonArray = jsonDecode(jsonString) as List;

    Logger().d(jsonArray);

    List list = jsonArray.map((e) => Station.fromJson(e)).toList();

    return list;
  }


  ///출발지와 도착지의 [nodeId]정보를 서버로 보낸 뒤 경로데이터 목록을 받는다.
  Future<List> getWayList(String departure, String destination) async {
    final service = "getWayList";
    final params = {
      "cityName":"청주",
      "deptId": departure,
      "destId": destination
    };

    //Logger().d(departure);
    //Logger().d(destination);


    Uri uri = Uri.http(endpoint, service, params);
    dynamic jsonString = await get(uri);
    Logger().d(jsonString);


    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Way.fromJson(e)).toList();

    return list;
  }

  //#endregion


  //#region POST

  Future<List> postRecently(Station departure, Station destination) async {
    final service = "postRecently";
    final header = {
      "operation":"postRecently"
    };
    final body = {
      "departure": departure.nodeId,
      "destination": destination.nodeId
    };

    Uri uri = Uri.http(endpoint, service);
    dynamic jsonString = await post(uri, header, body);

    var jsonArray = jsonDecode(jsonString) as List;
    List list = jsonArray.map((e) => Way.fromJson(e)).toList();

    return list;
  }


  void postIMEI(String imei) async {
    final service = "postImei";
    final header = {
      "operation":"postIMEI"
    };
    final body = {
      "imei" : imei,
    };

    Uri uri = Uri.http(endpoint, service);
    dynamic jsonString = await post(uri,header, body);
  }

  //#endregion





  /// 서버로 post 요청을 보낸 뒤 결과값을 받는다.
  Future<dynamic> post(Uri uri, dynamic header, dynamic body) async {
    http.Response response = await http.post(uri, headers: header, body: body, encoding: utf8);

    if(response.statusCode == 200){
      Logger().d("Success");
      return utf8.decode(response.bodyBytes);
    }
    else{
      Logger().d("Failed : ${response.statusCode}");
    }
  }


  /// 서버로 get 요청을 보낸 뒤 결과 값을 받는다.
  Future<String> get(Uri uri) async {
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