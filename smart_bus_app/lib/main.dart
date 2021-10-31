/// 본 스크립트는 앱이 실행될 때 최초도 실핼되는 스크립트이다.
/// 작성자 : 홍순원

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/web_server.dart';
import 'src/ui/find_way/station_search_widget.dart';

void main(){
  //전처리 - 웹서버 싱글톤 객체 생성
  new WebServer.init();

  //앱 실행
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: "Smart Bus",
      home: MainPage(),
    );
  }
}

class MainPage extends StatelessWidget {
  final controller = Get.put(Controller());
  List list = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];

  void init() async{
    await controller.getIdentifier();   // IMEI정보 얻기
    await controller.getGPS();          // GPS 정보 얻기
    await controller.getDustInfo();     // 현재 위치의 미세먼지 정보 얻기
    await controller.getWeatherInfo();  // 현재 위치의 날씨 정보 얻기
  }

  @override
  Widget build(BuildContext context) {
    init();

    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Spacer(),

          //날씨와 공기질 정보
          Expanded(
            flex: 2,
            child: Row(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Expanded(
                  flex: 1,
                  child: Card(
                    color: Color.fromARGB(0, 0, 0, 0),
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Text(
                        "날씨와 공기질 정보",
                        textAlign: TextAlign.center,
                        style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),

          //오전 오후 야간
          Expanded(
            flex:1,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              mainAxisSize: MainAxisSize.max,
              children: [
                Expanded(
                    flex: 1,
                    child: Text(
                      "오전",
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                ),
                Expanded(
                  flex: 1,
                  child: Text(
                    "오후",
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ),
                Expanded(
                  flex: 1,
                  child: Text(
                    "야간",
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
          ),

          //날씨 아이콘
          Expanded(
            flex:2,
            child: Card(
              child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Expanded(child: Obx(() => weatherInfomation(controller.weatherList.value[0] as Weather))),
                    Expanded(child: Obx(() => weatherInfomation(controller.weatherList.value[1] as Weather))),
                    Expanded(child: Obx(() => weatherInfomation(controller.weatherList.value[2] as Weather))),
                  ],
                ),
              ),
            ),

          //미세먼지정보
          Expanded(
            flex:2,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Expanded(
                    flex:1,
                    child: Card(
                      color: Color.fromARGB(0, 0, 0, 0),
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 5.0),
                        child: Obx(() => Text(
                            controller.dustStr.value,
                            textAlign: TextAlign.center,
                            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ),
                    )
                ),
              ],
            ),
          ),

          //지역 설정 버튼
          Expanded(
            flex:1,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () => onClickRegion(context, controller),
                  child: Text(
                      "지역 설정",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                  ),
                ),
              ],
            ),
          ),

          //버스 정보
          Expanded(
            flex:2,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              mainAxisSize: MainAxisSize.max,
              children: [
                Expanded(
                    flex: 1,
                    child: Card(
                      color: Color.fromARGB(0, 0, 0, 0),
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 10.0),
                        child: Text("버스 정보", textAlign: TextAlign.center, style: TextStyle(
                          fontSize: 20,
                          fontWeight: FontWeight.bold,
                        ),),
                      ),
                    ),
                ),
              ],
            ),
          ),

          //경로 리스트
          Expanded(
            flex: 10,
            child: ListView.builder(
              itemCount: list.length,
              itemBuilder: (context, p) {
                  return ListTile(title: Text('item ${list[p]}'),);
                },
            ),
          ),

          //버스 검색
          Expanded(
            flex:1,
            child: ElevatedButton(
              onPressed: onClickBusSearch,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20.0),
                child: Text(
                  "검색",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          ),

          Spacer()
        ],
      ),
    );
  }

  //지역 설정 버튼 이벤트
  void onClickRegion(BuildContext context, Controller controller){
    showDialog(
        context: context,
        builder: (BuildContext) {
          return AlertDialog(
            title: Text("지역 설정"),
            content: Obx((){
              if(controller.isLoading.value){
                return IntrinsicHeight(child: Center(child: CircularProgressIndicator()));
              }
              else{
                return Text("${controller.place.value}");
              }
            }),
            actions: [
              ElevatedButton(
                  onPressed: () async {
                    controller.getGPS();
                  },
                  child: Text("설정")
              ),
              ElevatedButton(
                  onPressed: (){
                    Get.back();
                  },
                  child: Text("닫기")
              ),
            ],
          );
        }
    );
  }


  //버스 검색 버튼 이벤트
  void onClickBusSearch(){
    Get.to(StationSearchPage());
  }


  Widget weatherInfomation(Weather weather){
    if(weather.type == WEATHER_TYPE.SUN) { return Icon(Icons.wb_sunny_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.CLOUD) { return Icon(Icons.wb_cloudy_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.RAIN) { return Icon(Icons.beach_access_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.SNOW) { return Icon(Icons.ac_unit_rounded, size: 50,);}
    else { return Icon(Icons.wb_cloudy_rounded, size: 50,);}
  }

}

