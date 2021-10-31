import 'dart:io';

import 'package:flutter_tts/flutter_tts.dart';
import 'package:geolocator/geolocator.dart';
import 'package:get/get.dart';
import 'package:imei_plugin/imei_plugin.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/web_server.dart';
import 'data_define.dart';


///<h1>클라이언트 비즈니스 로직 모듈</h1>
///<p>[Controller]클래스는 GetX패턴을 활용하여 작성된 클라이언트 비즈니스 모듈이다.</p>
///<p>모든 비즈니스 로직은 [Controller]에서 처리되며 이를 위해 Singleton패턴을 사용하여 유일한 객체로 생성된다.</p>
///<p>[Controller]내의 모든 변수는 [Rx]타입으로 저장되며 모든 함수는 void로 반환한다.</p>
class Controller extends GetxController{
  static Controller _instance = Controller.init();

  factory Controller(){
    return _instance;
  }

  Controller.init(){
    Logger().d("WebServerCreated!");
  }



  //출발지, 도착지 정류장
  Rx<Station> deptStation;
  Rx<Station> destStation;

  //가까운 정류장
  Rx nearStation = Rx(Station).obs;
  //RxList<Station> nearStationList = [].obs;

  //검색 정류장 리스트
  //RxList<Station> searchStationList = [].obs;

  //경로 리스트
  //RxList<Way> pathList = [].obs;

  //위치 정보
  RxString place = "Unknown".obs;
  RxDouble latitude = 0.0.obs;
  RxDouble longitude = 0.0.obs;

  //로딩 여부
  RxBool isLoading = false.obs;

  //IMEI정보
  RxString imei = "Unknown".obs;
  RxString uuid = "Unknown".obs;

  //미세먼지 정보
  RxList<dynamic> dustList = [].obs;

  //날씨 정보
  RxList<dynamic> weatherList = [].obs;

  //음성 인식 텍스트
  RxString ttsText = "Unknown".obs;



  ///<h2>길찾기</h2>
  ///<p>[Controller]의 deptStation, destStation 통해 최적의 경로를 찾는다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void pathfinding() async {
    while(true){
      if(isLoading.value == false){
        isLoading.value = true;
        break;
      }
      sleep(new Duration(milliseconds: 1000));
    }

    //pathList = await WebServer().getWayList(deptStation.value.nodeName, destStation.value.nodeName);

    isLoading.value = false;
  }


  ///<h2>출발지 지정</h2>
  ///<p>[Controller]의 nearStation을 출발지로 지정한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void setDeptStation(Station station) {
    deptStation.value = station;
  }


  ///<h2>도착지 지정</h2>
  ///<p>클라이언트가 선택한 정류장을 도착지로 지정한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void setDestStation(Station station) {
    destStation.value = station;
  }


  ///<h2>정류장 조회</h2>
  ///<p>사용자가 입력한 키워드를 통해 정류장을 조회한다.</p>
  ///<p>params : String keyword</p>
  ///<p>return : void </p>
  void getStationByKeyword(String keyword) async {
    dynamic list = await WebServer().getStationList(keyword);
    //searchStationList.value = list;
  }



  ///<h2>위치 정보 조회</h2>
  ///<p>클라이언트의 현재 위치를 위도, 경도 값으로 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void getGPS() async {
    isLoading.value = true;

    GPS gps = new GPS();
    Position pos = await gps.getLocation();
    String place = await gps.getAddress();

    latitude.value = pos.latitude;
    longitude.value = pos.longitude;
    this.place.value = place;

    isLoading.value = false;
  }

  ///<h2>주변 정류장 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 가장 가까운 정류장 리스트를 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void getNearStation() async{
    dynamic list = await WebServer().getStationByLocation(latitude.value, longitude.value);

    if(list.length <= 0 || list == null){
      return;
    }

    nearStation.value = list[0];
  }


  ///<h2>클라이언트 IMEI, UUID 정보 조회</h2>
  ///<p>클라이언트의 IMEI와 UUID정보를 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void getIdentifier() async {
    imei.value = await ImeiPlugin.getImei();
    uuid.value = await ImeiPlugin.getId();
  }


  ///<h2>날씨 정보 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 현재 지역의 날씨 정보를 조회한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void getWeatherInfo() async{
    List<dynamic> list = await WebServer().getWeatherInfo(latitude.value, longitude.value);

    if(list.length <= 0){
      Logger().d("날씨 정보 수신 실패!");
      return;
    }

    List<Weather> todayList = [];
    for(int i = 0; i < list.length; i++){
      DateTime date = DateTime.tryParse(list[i].dateTime);
      DateTime today = DateTime(DateTime.now().year, DateTime.now().month, DateTime.now().day);

      //오늘 날짜일 경우
      if(date.year == today.year && date.month == today.month && date.day == today.day){
        //6시 = 오전, 12시 = 점심, 18시 = 저녁
        if(date.hour == 6 || date.hour == 12 || date.hour == 18){
          list[i].setType();
          todayList.add(list[i]);
        }
      }
    }

    weatherList.value = todayList;
  }


  ///<h2>미세먼지 정보 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 현재 지역의 미세먼지 정보를 조회한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  void getDustInfo() async {
    List<dynamic> list = await WebServer().getDustInfo(latitude.value, longitude.value);

    if(list.length <= 0){
      Logger().d("미세먼지 정보 수신 실패!");
      return;
    }

    //미세먼지 알림 타입 설정
    for(int i = 0; i < list.length; i++){
      list[i].setType();
    }

    dustList.value = list;
  }


  ///<h2>음성 인식 텍스트 입력</h2>
  ///<p>음성을 입력받아 텍스트로 변환한다.</p>
  ///<p>params : none </p>
  ///<p>return : void </p>
  void textToSpeech() async {
    ttsText.value = "Unknown";

    FlutterTts tts = FlutterTts();

    await tts.setLanguage('ko-kr');
    tts.speak(ttsText.value);
  }



}