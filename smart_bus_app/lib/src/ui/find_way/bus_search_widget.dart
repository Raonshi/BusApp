import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';
import 'package:smart_bus_app/src/ui/find_way/path_finding_widget.dart';



//버스 노선 검색
class BusSearchPage extends StatelessWidget {
  final controller = Get.put(Controller());
  List list = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];

  @override
  Widget build(BuildContext context) {
    search();

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
            child: Text("출발지 : ${controller.deptStation.value.nodeName}", style: TextStyle(fontSize: 20), maxLines: 2,),
          ),

          //목적지 입력
          Expanded(
            flex:1,
            child: Text("목적지 : ${controller.destStation.value.nodeName}", style: TextStyle(fontSize: 20), maxLines: 2,),
          ),


          //경로 리스트
          Expanded(
            flex: 8,
            child: ListView.builder(
              itemCount: controller.subPathList.length,
              itemBuilder: (context, index) {
                SubPath path = controller.subPathList.value[index] as SubPath;
                return ListTile(
                  leading: Icon(Icons.directions_bus),
                  title: Text("버스 번호"),
                  trailing: Text("남은 시간"),
                  onTap: (){
                    Get.to(() => PathFindingPage());
                  },
                );
              },
            ),
          ),



          Spacer(flex:2),
        ],),
    );
  }


  void search() async {
    await controller.pathfinding();
  }
}