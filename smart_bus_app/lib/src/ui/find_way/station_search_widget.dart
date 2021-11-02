import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';
import 'bus_search_widget.dart';

//정류장 검색
class StationSearchPage extends StatelessWidget {
  final controller = Get.put(Controller());
  final textController = TextEditingController();

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
                    color: Color.fromARGB(255, 72, 192, 72),
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
          Expanded(
            flex:15,
            child: Column(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
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
                    child:Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      mainAxisSize: MainAxisSize.max,
                      children: [
                        //목적지 텍스트
                        Expanded(
                          flex:1,
                          child: Text("목적지 : ", style: TextStyle(fontSize: 20),),
                        ),

                        //텍스트 입력
                        Expanded(
                            flex:3,
                            child: TextField(
                              controller: textController,
                              onSubmitted: (text) {
                                textController.text = text;

                                controller.getStationByKeyword(textController.text);
                              },
                            )
                        ),

                        //검색 버튼
                        Expanded(
                          flex:1,
                          child: ElevatedButton(
                            onPressed: (){
                              controller.getStationByKeyword(textController.text);
                            },
                            child: Text("입력", style: TextStyle(fontSize: 20)),
                          ),
                        ),
                      ],
                    )
                ),


                //음성 인식 아이콘
                Expanded(
                  flex: 4,
                  child: IconButton(
                    onPressed: () async {
                      Logger().d("Icon Btn Pressed");
                      controller.textToSpeech();
                      controller.getStationByKeyword(controller.ttsText.value);
                    },

                    icon: Icon(Icons.mic_rounded),
                    iconSize: 60,
                    color: Color.fromARGB(255, 78, 223, 78),
                  ),
                ),
              ],
            ),
          ),


          Expanded(
            flex:1,
            child: ElevatedButton(
              onPressed: (){
                Get.to(() => BusSearchPage());
              },
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 10.0),
                child: Text(
                  "버스 노선 검색",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),),
            ),
          ),

          Spacer(),
        ],),
    );
  }
}