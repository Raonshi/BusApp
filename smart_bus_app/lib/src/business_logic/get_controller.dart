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

  factory Controller(){return _instance;}
  Controller.init() {Logger().d("Controller Created!");}

  //출발지, 도착지 정류장
  Rx<Station> deptStation = Station().obs;
  Rx<Station> destStation = Station().obs;

  //가까운 정류장
  Rx<Station> nearStation = Station().obs;

  //버스 리스트
  RxList busList = [].obs;

  //검색 정류장 리스트
  RxList searchStationList = [].obs;

  //경로 리스트
  RxList subPathList = [].obs;

  //경로
  Rx<Path> path = Path().obs;

  //위치 정보
  RxString place = "Unknown".obs;
  RxDouble latitude = 0.0.obs;
  RxDouble longitude = 0.0.obs;

  //로딩 여부
  RxBool titleLoading = false.obs;
  RxBool isLoading = false.obs;

  //IMEI정보
  RxString imei = "Unknown".obs;
  RxString uuid = "Unknown".obs;

  //미세먼지 정보
  Rx<Dust> dust = Dust().obs;
  RxString dustStr = "불명".obs;

  //날씨 정보
  RxList weatherList = [].obs;

  //음성 인식 텍스트
  RxString ttsText = "Unknown".obs;


  void infomationInit({int type}) async{
    if(type == 0){titleLoading.value = true;}
    else if(type == 1){isLoading.value = true;}

    Logger().d("IMEI START");
    await getIdentifier();   // IMEI정보 얻기

    Logger().d("GPS START");
    await getGPS();          // GPS 정보 얻기

    Logger().d("DUST START");
    await getDustInfo();     // 현재 위치의 미세먼지 정보 얻기

    Logger().d("WEATHER START");
    await getWeatherInfo();  // 현재 위치의 날씨 정보 얻기

    Logger().d("STATION START");
    await getNearStation();  // 가장 가가운 정류장 찾기
    await getStationThroughBusList(nearStation.value.nodeId);

    if(type == 0){titleLoading.value = false;}
    else if(type == 1){isLoading.value = false;}

  }


  ///<h2>길찾기</h2>
  ///<p>[Controller]의 deptStation, destStation 통해 최적의 경로를 찾는다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> pathfinding() async {
    Logger().d("PATH FINDING START");
    setDeptStation(nearStation.value);

    Logger().d("Dept : ${deptStation.value.nodeId}");

    List pathList = [];
    for(int i = 0; i < searchStationList.length; i++){
      setDestStation(searchStationList.value[i]);
      List list = await WebServer().getWayList(deptStation.value.nodeId, destStation.value.nodeId);

      Logger().d(list);

      pathList.addAll(list);
    }
    path.value = pathList[0];
    subPathList.value = path.value.subPath;

    Logger().d( "PATH LIST : ${path.value.subPath}");
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
  Future<void> getStationByKeyword(String keyword) async {
    dynamic list = await WebServer().getStationList(keyword);
    searchStationList.value = list;
  }


  ///<h2>위치 정보 조회</h2>
  ///<p>클라이언트의 현재 위치를 위도, 경도 값으로 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> getGPS() async {
    //isLoading.value = true;

    GPS gps = new GPS();
    Position pos = await gps.getLocation();
    String place = await gps.getAddress();

    latitude.value = pos.latitude;
    longitude.value = pos.longitude;
    this.place.value = place;

    //isLoading.value = false;
    Logger().d("getGPS");
  }


  ///<h2>주변 정류장 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 가장 가까운 정류장 리스트를 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> getNearStation() async{
    nearStation.value = null;
    Station station = await WebServer().getStationByLocation(latitude.value, longitude.value);

    if(station == null){
      return;
    }

    Logger().d("Station : ${station.nodeId}");
    nearStation.value = station;
  }


  ///<h2>정류장 경유 버스 노선 정보 조회</h2>
  ///<p>매개변수로 주어진 nodeId를 통해 해당하는 정류장을 경유하는 버스의 정보를 조회한다.</p>
  ///<p>params : [String] nodeId</p>
  ///<p>return : void </p>
  Future<void> getStationThroughBusList(String nodeId) async {
    List<dynamic> list = await WebServer().getStationThroughBusList(nodeId);

    Logger().d(list);

    if(list.length <= 0){
      Logger().d("버스 리스트 길이가 0입니다.");
      return;
    }

    busList.value = list;
  }


  ///<h2>클라이언트 IMEI, UUID 정보 조회</h2>
  ///<p>클라이언트의 IMEI와 UUID정보를 구한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> getIdentifier() async {
    imei.value = await ImeiPlugin.getImei();
    uuid.value = await ImeiPlugin.getId();
    await Future.delayed(new Duration(milliseconds: 500));

    Logger().d("IMEI : ${imei.value}");
  }


  ///<h2>날씨 정보 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 현재 지역의 날씨 정보를 조회한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> getWeatherInfo() async{
    List<dynamic> list = await WebServer().getWeatherInfo(latitude.value, longitude.value);

    if(list.length <= 0){
      Logger().d("날씨 정보 수신 실패!");
      return;
    }

    for(int i = 0; i < list.length; i++){
      list[i].setType();
    }

    Logger().d("getWeather");
    weatherList.value = list;
  }


  ///<h2>미세먼지 정보 조회</h2>
  ///<p>클라이언트의 위치 좌표를 기반으로 현재 지역의 미세먼지 정보를 조회한다.</p>
  ///<p>params : none</p>
  ///<p>return : void</p>
  Future<void> getDustInfo() async {
    dust.value = await WebServer().getDustInfo(latitude.value, longitude.value);

    if(dust.value == null){
      Logger().d("미세먼지 정보 수신 실패!");
      return;
    }

    //미세먼지 알림 타입 설정
    //dust.value = list[0] as Dust;
    dust.value.setType();
    dustInfomation();
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


  ///<h2>미세먼지 상태 화면 이벤트</h2>
  ///<p>현재 위치의 미세먼지 정보 값을 화면에 출력하는 형태의 텍스트로 변환한다.</p>
  ///<p>params : none </p>
  ///<p>return : void </p>
  void dustInfomation(){
    switch(dust.value.type){
      case DUST_TYPE.LOW:
        dustStr.value = "청정";
        break;
      case DUST_TYPE.MID:
        dustStr.value = "보통";
        break;
      case DUST_TYPE.HIGH:
        dustStr.value = "높음";
        break;
      case DUST_TYPE.DANGER:
        dustStr.value = "위험";
        break;
      default:
        dustStr.value = "불명";
    }
  }
}