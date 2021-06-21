import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';

class DestinationProvider with ChangeNotifier{
  String _destination = '';
  List<SearchItem> _destinationList = [];

  String get destination => _destination;
  setDeparture(String value) {
    _destination = value;
    notifyListeners();
  }

  get destinationList => _destinationList;

  void filtering(){
    //테스트용 데이터
    List<SearchItem> tmpList = [
      SearchItem('aaa', TYPE.DESTINATION),
      SearchItem('aab', TYPE.DESTINATION),
      SearchItem('aba', TYPE.DESTINATION),
      SearchItem('abb', TYPE.DESTINATION),
      SearchItem('baa', TYPE.DESTINATION),
      SearchItem('bab', TYPE.DESTINATION),
      SearchItem('bba', TYPE.DESTINATION),
      SearchItem('bbb', TYPE.DESTINATION),
    ];

    //리스트 초기화
    _destinationList = [];

    //테스트용 리스트 탐색
    for(SearchItem item in tmpList){
      if(item.cityName.contains(_destination)){
        _destinationList.add(item);
      }
    }
    notifyListeners();
  }

}