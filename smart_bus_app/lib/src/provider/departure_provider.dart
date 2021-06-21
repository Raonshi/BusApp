import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';

class DepartureProvider with ChangeNotifier{
  String _departure = '';
  List<SearchItem> _departureList = [
    SearchItem('aaa'),
    SearchItem('aab'),
    SearchItem('aba'),
    SearchItem('abb'),
    SearchItem('baa'),
    SearchItem('bab'),
    SearchItem('bba'),
    SearchItem('bbb'),
  ];


  String get departure => _departure;
  setDeparture(String value) => _departure = value;


  get departureList => _departureList;



  List<SearchItem> filtering(){
    List<SearchItem> list = _departureList;



    return list;
  }

}