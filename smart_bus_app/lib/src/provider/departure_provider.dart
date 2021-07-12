import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';
import 'package:smart_bus_app/src/web/web_server.dart';

class DepartureProvider with ChangeNotifier{
  String _departure = '';
  List<SearchItem> _departureList = [];


  String get departure => _departure;
  setDeparture(String value){
    _departure = value;
    notifyListeners();
  }


  List<SearchItem> get departureList => _departureList;

  ///출발지 선택 화면 검색어 필터링
  void filtering() async {
    //await WebServer().searchKeyword(_departure);

    //리스트 초기화
    _departureList = [];

    notifyListeners();
  }

}