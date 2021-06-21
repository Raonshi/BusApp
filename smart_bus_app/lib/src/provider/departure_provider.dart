import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';

class DepartureProvider with ChangeNotifier{
  String _departure = '';
  List<SearchItem> _departureList = [];


  String get departure => _departure;
  setDeparture(String value){
    _departure = value;
    notifyListeners();
  }


  get departureList => _departureList;



  void filtering(){
    //테스트용 데이터
    List<SearchItem> tmpList = [
      SearchItem('aaa'),
      SearchItem('aab'),
      SearchItem('aba'),
      SearchItem('abb'),
      SearchItem('baa'),
      SearchItem('bab'),
      SearchItem('bba'),
      SearchItem('bbb'),
    ];

    //리스트 초기화
    _departureList = [];

    //테스트용 리스트 탐색
    for(SearchItem item in tmpList){
      if(item.cityName.contains(_departure)){
        _departureList.add(item);
      }
    }
    notifyListeners();
  }

}