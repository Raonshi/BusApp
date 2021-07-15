import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';
import 'package:smart_bus_app/src/web_server.dart';

enum Type{ BUS, STATION }

class SearchProvider with ChangeNotifier{
  /*
  String _busNum = '';
  String _departure = '';
  String _destination = '';
   */
  String _keyword;
  Type _type;

  /*
  String get busNum => _busNum;
  String get departure => _departure;
  String get destination => _destination;
   */

  String get keyword => _keyword;
  Type get type => _type;

  search(String keyword, Type type){
    /*
    switch(type){
      case Type.BUS:
        _busNum = keyword;
        break;
      case Type.STATION_DEPARTURE:
        _departure = keyword;
        break;
      case Type.STATION_DESTINATION:
        _destination = keyword;
        break;
    }
     */
    _keyword = keyword;
    _type = type;

    notifyListeners();
  }


  List<dynamic> _list = [];
  List<dynamic> get list => _list;

  ///출발지 선택 화면 검색어 필터링
  void filtering() async {
    List tmpList;

    //리스트 초기화
    _list.clear();

    switch(_type){
      case Type.BUS:
        tmpList = await WebServer().getBusList(_keyword);
        break;
      case Type.STATION:
        tmpList = await WebServer().getStationList(_keyword);
        break;
    }

    //리스트에 채워넣기
    tmpList.forEach((element) {
      _list.add(BusItem(element));
    });

    notifyListeners();
  }

}