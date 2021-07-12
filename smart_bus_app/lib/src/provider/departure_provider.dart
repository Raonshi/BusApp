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


  get departureList => _departureList;



  void filtering() async {
    List<SearchItem> routeList = await WebServer().getRouteNo("청주", _departure);

    //리스트 초기화
    _departureList = [];

    //테스트용 리스트 탐색
    for(SearchItem item in routeList){
      if(item.no.contains(_departure)){
        _departureList.add(item);
      }
    }
    notifyListeners();
  }

}