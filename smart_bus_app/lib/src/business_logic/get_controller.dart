import 'dart:io';

import 'package:geolocator/geolocator.dart';
import 'package:get/get.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/web_server.dart';
import 'data_define.dart';

class Controller extends GetxController{
  static Controller _instance = Controller.init();

  factory Controller(){
    return _instance;
  }

  Controller.init(){
    Logger().d("WebServerCreated!");
  }



  //가까운 정류장
  Rx nearStation = Rx(Station).obs;
  RxList<dynamic> nearStationList = [].obs;

  //위치 정보
  RxString place = "Unknown".obs;
  RxDouble latitude = 0.0.obs;
  RxDouble longtitude = 0.0.obs;
  RxBool isLoading = false.obs;


  void getGPS() async {
    isLoading.value = true;

    GPS gps = new GPS();
    Position pos = await gps.getLocation();
    String place = await gps.getAddress();

    latitude.value = pos.latitude;
    longtitude.value = pos.longitude;
    this.place.value = place;

    isLoading.value = false;
  }

  void getNearStation() async{
    dynamic list = await WebServer().getStationByLocation(latitude.value, longtitude.value);

    if(list.length == 0){
      return;
    }

    nearStation.value = list[0];
  }
}