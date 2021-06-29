import 'package:logger/logger.dart';
import 'dart:convert';
import 'www.dart';

class WebServer {
  static final WebServer _instance = WebServer.init();

  factory WebServer(){
    return _instance;
  }

  WebServer.init(){
    Logger().d("WebServer has created!!!");
  }

  void citySupport(String cityName) async {
    var json = await WWW().getServiceRegion();
    final jsonResult = jsonDecode(json);
    final data = jsonResult['response']['body']['items']['item']['cityname'];

    Logger().d("DATA : $data");


  }
}