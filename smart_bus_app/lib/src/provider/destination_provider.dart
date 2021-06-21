import 'package:flutter/widgets.dart';

class DestinationProvider with ChangeNotifier{
  String _destination = '';

  String get destination => _destination;
  setDeparture(String value) => _destination = value;


}