import 'package:flutter/widgets.dart';
import 'package:smart_bus_app/src/data/data.dart';

class FindWayProvider with ChangeNotifier{
  ///출발지 정류장 정보
  //String _departure;
  //get departure => _departure;
  Station _departure;
  Station get departure => _departure;

  ///도착지 정류장 정보
  //String _destination;
  //get destination => _destination;
  Station _destination;
  Station get destination => _destination;

  FindWayProvider(this._departure, this._destination);

  void setDeparture(Station station) => _departure = station;
  void setDestination(Station station) => _destination = station;

  ///출발지와 도착지 정보를 통해 경로를 탐색한다.
  void finding(){
    //1. 출발지를 지나가는 모든 노선 탐색

    //2. 도착지를 지나가는 모든 노선 탐색

    //3. 출발지와 도착지 노선들 중 서로 중복되는 노선이 있다면
    //3-1. 도착지의 예상 도착시간 - 출발지의 예상 도착시간 = 총 소요시간

    //4. 출발지와 도착지의 모든 노선에 대하여 각 노선별 경유 정류장을 모두 탐색
    //4-1. 노선별 경유 정류장 중

  }





}