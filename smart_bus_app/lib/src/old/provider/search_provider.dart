import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/old/data/data.dart';
import 'package:smart_bus_app/src/old/item.dart';
import 'package:smart_bus_app/src/web_server.dart';

enum Type{ BUS, STATION }

class SearchProvider with ChangeNotifier{
  String _keyword;
  Type _type;

  String get keyword => _keyword;
  Type get type => _type;

  List<dynamic> _list = [];
  List<dynamic> get list => _list;

  search(String keyword, Type type){
    _keyword = keyword;
    _type = type;

    notifyListeners();
  }

  ///출발지 선택 화면 검색어 필터링
  void filtering() async {
    List tmpList = new List();

    //리스트 초기화
    _list.clear();

    switch(_type){
      case Type.BUS:
        /*
        final testBus = {
          'routetp': '좌석',
          'routeid': 'CJB454636',
          'routeno': '863-2',
          'startvehicletime': '0603',
          'endvehicletime': '1835',
          'startnodenm': '출발정류장',
          'endnodenm': '종점정류장'
        };
        for(int i = 0; i < 10; i++){
          tmpList.add(Bus.fromJson(testBus));
        }
         */
        tmpList = await WebServer().getBusList(_keyword);

        tmpList.forEach((element) {
          _list.add(BusItem(element));
        });
        break;
      case Type.STATION:
        /*
        final testStation = {
          'nodenm': '정거장 이름',
          'nodeid': '정거장 아이디',
          'nodeno': '정거장 번호',
          'gpslong': 'x축 좌표 ',
          'gpslati': 'y축 좌표'
        };
        for(int i = 0; i < 10; i++){
          tmpList.add(Station.fromJson(testStation));
        }
        */
        tmpList = await WebServer().getStationList(_keyword);

        tmpList.forEach((element) {
          _list.add(StationItem(element));
        });
        break;
    }

    notifyListeners();
  }
}

