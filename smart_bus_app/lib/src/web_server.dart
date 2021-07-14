import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/serialization/city.dart';


class WebServer{
  //#region Singleton Pattern
  static WebServer _instance = WebServer.init();

  factory WebServer(){
    return _instance;
  }

  WebServer.init(){
    Logger().d("WebServerCreated!");
  }
  //#endregion

  final endpoint = "127.0.0.1:8080";

  ///테스트 코드 -> 이 코드를 기반으로 데이터 처리
  void test(String cityName) async {
    final service = "list";
    final params = {
      "cityName" : cityName
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic result = await WWW().request(uri);

    Logger().d(result);
  }

  /// 도시 정보를 불러온다.
  ///
  /// [cityName]을 통해 도시이름을 전달받아 해당 도시가 지원될 경우,
  /// true를 반환한다.
  Future<bool> getCityInfo(String cityName) async {
    final service = "list";
    final params = {
      "cityName" : cityName
    };

    Uri uri = Uri.http(endpoint, service, params);
    dynamic response = await WWW().request(uri);

    print(response);

    try{
      City city = City.fromJson(jsonDecode(response));
      return true;
    }
    catch(e){
      Logger().d(e.toString());
      return false;
    }
  }
  ///
}


class WWW{
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