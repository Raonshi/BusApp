import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';


//경로 탐색
class PathFindingPage extends StatelessWidget {
  final controller = Get.put(Controller());
  SubPath path;
  PathFindingPage({this.path});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Spacer(),

          //#region 검색 타입 선택
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
                    color: Color.fromARGB(0, 0, 0, 0),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Text("버스노선", textAlign: TextAlign.center, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),),
                    ),
                  ),
                ),

                Expanded(
                  flex:1,
                  child: Card(
                    color: Color.fromARGB(255, 72, 192, 72),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Text("경로탐색", textAlign: TextAlign.center, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),),
                    ),
                  ),
                ),

              ],
            ),
          ),
          //#endregion

          //#region 정류장 정보 , 노선정보, 경로정보 출력
          Expanded(
            flex:15,
            child: ListView.builder(
              itemCount: path.pathStationList.length,
              itemBuilder: (context, index){
                PathStation station = path.pathStationList[index];
                Icon icon;
                if(index == 0 || index == path.pathStationList.length - 1){
                  icon = Icon(Icons.directions_bus);
                }
                else{
                  icon = Icon(Icons.arrow_drop_down_circle);
                }

                return ListTile(
                  leading: Icon(Icons.location_on),
                  title: Text(station.nodeName),
                  trailing: icon,
                );
              },
            ),
          ),
          //#endregion


          Spacer(flex:2),
        ],),
    );
  }
}