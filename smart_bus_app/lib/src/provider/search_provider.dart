import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';
import 'package:smart_bus_app/src/web_server.dart';

enum Type{ BUS, STATION }

class SearchProvider with ChangeNotifier{
  String _keyword = '';
  Type _type;

  String get keyword => _keyword;
  Type get type => _type;

  search(String keyword, Type type){
    _keyword = keyword;
    _type = type;

    notifyListeners();
  }


  List<dynamic> _departureList = [];

  List<SearchItem> get departureList => _departureList;

  ///출발지 선택 화면 검색어 필터링
  void filtering() async {
    List tmpList;
    switch(type){
      case Type.BUS:
        tmpList = await WebServer().getBusList(keyword);
        break;
      case Type.STATION:
        tmpList = await WebServer().getStationList(keyword);
        break;
    }

    //리스트 초기화
    _departureList = [];

    //리스트에 채워넣기
    tmpList.forEach((element) {
      _departureList.add(BusItem(element));
    });

    notifyListeners();
  }

}