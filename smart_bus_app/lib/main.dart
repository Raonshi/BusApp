/// 본 스크립트는 앱이 실행될 때 최초도 실핼되는 스크립트이다.
/// 작성자 : 홍순원

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/ui/widgets/bus_item.dart';
import 'package:smart_bus_app/src/web_server.dart';
import 'src/ui/find_way/station_search_widget.dart';

void main() async {
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
  bool titleLoad = false;
  List list = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];



  @override
  Widget build(BuildContext context) {
    controller.infomationInit(type: 0);

    return Scaffold(
      body: Container(
        child: Obx(() => controller.titleLoading.value ? titleLoading() : mainScreen(context)),
      ),
    );

    //return controller.titleLoading.value ? titleLoading() : mainScreen(context);
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
                    controller.infomationInit(type: 1);
                    //controller.getGPS();
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


  Widget weatherInfomation(int index){
    if(controller.weatherList.value == null){
      return Icon(Icons.warning_rounded, size: 50,);
    }

    Weather weather = controller.weatherList.value[index];

    if(weather.type == WEATHER_TYPE.SUN) { return Icon(Icons.wb_sunny_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.CLOUD) { return Icon(Icons.wb_cloudy_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.RAIN) { return Icon(Icons.beach_access_rounded, size: 50,);}
    else if(weather.type == WEATHER_TYPE.SNOW) { return Icon(Icons.ac_unit_rounded, size: 50,);}
    else { return Icon(Icons.warning_amber_rounded, size: 50,);}
  }


  Color dustWidgetColor(String str){
    Color color;
    if(str == "청정"){color = new Color.fromARGB(255, 100, 180, 255);}
    else if(str == "보통"){color = new Color.fromARGB(255, 100, 255, 180);}
    else if(str == "높음"){color = new Color.fromARGB(255, 255, 180, 100);}
    else if(str == "위험"){color = new Color.fromARGB(255, 255, 75, 75);}
    else{color = new Color.fromARGB(255, 150, 150, 150);}
    return color;
  }


  Widget titleLoading(){
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("로 딩 중 . . .", style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),),
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CircularProgressIndicator(),
          ],
        ),
      ],
    );
  }

  Widget mainScreen(BuildContext context){
    return Column(
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
                Expanded(child: weatherInfomation(0)),
                Expanded(child: weatherInfomation(1)),
                Expanded(child: weatherInfomation(2)),
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
                child: Obx(() => Card(
                  color: dustWidgetColor(controller.dustStr.value),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 5.0),
                    child: Text(
                      controller.dustStr.value,
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                  ),),
                ),
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
            itemCount: controller.busList.length,
            itemBuilder: (context, index) {
              Bus bus = controller.busList.value[index];
              return BusItem(bus: bus);
            },
          ),
        ),

        //버스 검색
        Expanded(
          flex:1,
          child: ElevatedButton(
            onPressed: () => Get.to(StationSearchPage()),
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
    );
  }
}

