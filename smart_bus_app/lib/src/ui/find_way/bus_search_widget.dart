import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';



//버스 노선 검색
class BusSearchPage extends StatelessWidget {
  final controller = Get.put(Controller());
  List list = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Spacer(),

          //검색 타입 선택
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
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Text("정류장", textAlign: TextAlign.center, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),),
                    ),
                  ),
                ),

                Expanded(
                  flex:1,
                  child: Card(
                    color: Color.fromARGB(255, 72, 192, 72),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Text("버스노선", textAlign: TextAlign.center, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),),
                    ),
                  ),
                ),

                Expanded(
                  flex:1,
                  child: Card(
                    color: Color.fromARGB(0, 0, 0, 0),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Text("경로탐색", textAlign: TextAlign.center, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),),
                    ),
                  ),
                ),

              ],
            ),
          ),

          //정류장 정보 , 노선정보, 경로정보 출력

          //버스 정류장 정보
          Expanded(
              flex:1,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                mainAxisSize: MainAxisSize.max,
                children: [
                  Expanded(
                    child: Card(
                      color: Color.fromARGB(0, 0, 0, 0),
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 10.0),
                        child: Text(
                          "버스 정류장 정보",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                  )
                ],
              )
          ),

          //정류장 번호
          Expanded(
            flex:1,
            child: Text("정류장 번호 : ", style: TextStyle(fontSize: 20),),
          ),

          //정류장 이름
          Expanded(
            flex:1,
            child: Text("출발지 : ", style: TextStyle(fontSize: 20), maxLines: 2,),
          ),

          //목적지 입력
          Expanded(
            flex:1,
            child: Text("목적지 : ", style: TextStyle(fontSize: 20), maxLines: 2,),
          ),


          //경로 리스트
          Expanded(
            flex: 8,
            child: ListView.builder(
              itemCount: list.length,
              itemBuilder: (context, p) {
                return ListTile(title: Text('item ${list[p]}'),);
              },
            ),
          ),



          Spacer(flex:2),
        ],),
    );
  }
}