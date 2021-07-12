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
    //리스트 초기화
    _destinationList = [];


    notifyListeners();
  }

}