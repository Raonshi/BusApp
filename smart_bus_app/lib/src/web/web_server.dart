import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_info_item.dart';
import 'package:smart_bus_app/src/data/city_item.dart';
import 'package:smart_bus_app/src/data/bus_node_information/route_station_item.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';
import 'www.dart';

class WebServer {
  List<String> nodeList;
  List<String> routeList;

  static final WebServer _instance = WebServer.init();

  factory WebServer(){
    return _instance;
  }

  WebServer.init(){
    Logger().d("WebServer has created!!!");

    //모든 정거장과 버스 번호 목록을 가져온다.
  }

  Future<dynamic> testing() async {
    final city = await WWW().testing("청주");
    //Logger().d("XML : ${xml.toXmlString(pretty: true, indent: '\t')}");
  }

  ///서버로부터 도시 정보를 수신받는다.
  Future<CityItem> citySupport(String cityName) async {
    List<CityItem> cities = await WWW().getCityCodeList(0);

    for(int i = 0; i < cities.length; i++){
      if(cities.elementAt(i).cityName.contains(cityName)){
        return cities.elementAt(i);
      }
    }
  }


  ///버스 번호를 검색하면 해당하는 내용을 SearchItem객체에 담는다.
  Future<List<SearchItem>> getRouteNo(String cityName, String routeNo) async {
    List<SearchItem> itemList = List();

    CityItem city = await citySupport(cityName);


    if(city != null){
      Logger().d("${city.cityName} || ${city.cityCode}");

      RouteInfoItem route = await WWW().getRouteInfo(city.cityCode, routeNo);
      SearchItem(route.id, route.no, route.startTime, route.endTime, route.startNode, route.endNode);
    }
  }


  ///버스노선별 경유 정류장 목록을 수신받는다.
  void getNodeList(String cityName, String routeId) async {
    CityItem city = await citySupport(cityName);

    if(city != null){
      print("OK");
      List<RouteStationItem> stations = await WWW().getRouteAccToThrghSttnList(10, 1, city.cityCode, routeId);
    }
    else{
      print("NOT OK");
    }
  }

}