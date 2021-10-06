import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/data/data.dart';
import 'package:smart_bus_app/src/ui/item.dart';

import '../web_server.dart';

class FindWayProvider with ChangeNotifier{
  ///출발지 정류장 정보
  Station _departure;
  Station get departure => _departure;

  ///도착지 정류장 정보
  Station _destination;
  Station get destination => _destination;

  ///경로 리스트
  List<dynamic> _wayList = [];
  List<dynamic> get wayList => _wayList;

  ///JSON데이터를 담기위한 임시 리스트
  //List<Way> tmpList = new List();

  FindWayProvider(this._departure, this._destination);

  void setDeparture(Station station) {
    _departure = station;
    notifyListeners();
  }
  void setDestination(Station station){
    _destination = station;
    notifyListeners();
  }

  ///출발지와 도착지 정보를 통해 경로를 탐색한다.
  void finding() async {
    List<Way> tmpList = await WebServer().getWayList(_departure.nodeId, _destination.nodeId);

    tmpList.forEach((element) {
      _wayList.add(WayItem(element));
    });

    //tmpList = tmpList.isEmpty ? await WebServer().getWayList(_departure.nodeId, _destination.nodeId) : tmpList;

    /*
    //테스트코드
    final testJson = {
      "deptnodenm": "출발지정거장",
      "destnodenm": "도착지정거장",
      "deptarrtime": "1806",
      "destarrtime": "3643",
      "deptarrprevstationcnt": "15",
      "destarrprevstationcnt": "27",
      "routetp": "좌석",
      "routeno": "999-9",
      "vehicletp": "저상",
    };

    for(int i = 0; i < 10; i++){
      _wayList.add(WayItem(Way.fromJson(testJson)));
    }
    */
    notifyListeners();
  }





}