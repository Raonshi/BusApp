import 'package:geolocator/geolocator.dart';
import 'package:get/get.dart';
import 'package:imei_plugin/imei_plugin.dart';
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
  RxDouble longitude = 0.0.obs;
  RxBool isLoading = false.obs;

  //IMEI정보
  RxString imei = "Unknown".obs;
  RxString uuid = "Unknown".obs;

  //미세먼지 정보
  RxList<Dust> dustList = [].obs;

  //날씨 정보
  RxList<Weather> weatherList = [].obs;



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

  void getNearStation() async{
    dynamic list = await WebServer().getStationByLocation(latitude.value, longitude.value);

    if(list.length == 0){
      return;
    }

    nearStation.value = list[0];
  }


  void getIdentifier() async {
    imei.value = await ImeiPlugin.getImei();
    uuid.value = await ImeiPlugin.getId();
  }


  void getWeatherInfo() async{
    List<Weather> list = await WebServer().getWeatherInfo(latitude.value, longitude.value);

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


  void getDustInfo() async{
    List<Dust> list = await WebServer().getDustInfo(latitude.value, longitude.value);

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
}