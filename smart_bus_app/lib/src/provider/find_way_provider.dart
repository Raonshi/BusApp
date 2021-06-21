import 'package:flutter/widgets.dart';

class FindWayProvider with ChangeNotifier{
  String _departure;
  get departure => _departure;
  void setDeparture(String value){
    _departure = value;
    notifyListeners();
  }

  String _destination;
  get destination => _destination;
  void setDestination(String value){
    _destination = value;
    notifyListeners();
  }


}