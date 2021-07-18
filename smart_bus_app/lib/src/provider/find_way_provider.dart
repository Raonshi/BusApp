import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/data/data.dart';

import '../web_server.dart';

class FindWayProvider with ChangeNotifier{
  ///출발지 정류장 정보
  Station _departure;
  Station get departure => _departure;

  ///도착지 정류장 정보
  Station _destination;
  Station get destination => _destination;

  ///경로 리스트
  List<Way> _wayList;
  List<Way> get wayList => _wayList;

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
    //_wayList = await WebServer().getWayList(_departure.nodeId, _destination.nodeId);

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
      _wayList.add(Way.fromJson(testJson));
    }

    notifyListeners();
  }





}