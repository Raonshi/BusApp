import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/item.dart';
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
    List tmpList;

    //리스트 초기화
    _list.clear();

    switch(_type){
      case Type.BUS:
        tmpList = await WebServer().getBusList(_keyword);
        tmpList.forEach((element) {
          _list.add(BusItem(element));
        });
        break;
      case Type.STATION:
        tmpList = await WebServer().getStationList(_keyword);
        tmpList.forEach((element) {
          _list.add(StationItem(element));
        });
        break;
    }
    notifyListeners();
  }
}

