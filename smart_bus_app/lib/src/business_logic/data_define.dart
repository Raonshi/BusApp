import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';

//#region 날씨
enum WEATHER_TYPE{ SUN, RAIN, CLOUD, SNOW, }

class Weather{
  String dateTime;
  double temp;
  String weather;
  WEATHER_TYPE type;

  Weather(this.dateTime, this.temp, this.weather);

  factory Weather.fromJson(dynamic json){
    double value = double.parse(json['temparature'].toString());
    return Weather(json['datetime'].toString(), value, json['weather'].toString());
  }

  void setType(){
    switch(weather){
      case "Clear":
        type = WEATHER_TYPE.SUN;
        break;
      case "Clouds":
        type = WEATHER_TYPE.CLOUD;
        break;
      default:
        type = WEATHER_TYPE.CLOUD;
        break;
    }
  }
}
//#endregion


//#region 미세먼지
enum DUST_TYPE{ LOW, MID, HIGH, DANGER, }

class Dust{
  DUST_TYPE type;
  int pm10Value;
  String stationName;

  Dust(this.pm10Value, this.stationName);

  factory Dust.fromJson(dynamic json){
    int value = int.parse(json['pm10value'] as String);
    return Dust(value, json['stationName'] as String);
  }

  void setType(){
    if(pm10Value <= 30){
      type = DUST_TYPE.LOW;
    }
    else if(pm10Value <= 80){
      type = DUST_TYPE.MID;
    }
    else if(pm10Value <= 150){
      type = DUST_TYPE.HIGH;
    }
    else{
      type = DUST_TYPE.DANGER;
    }
  }
}
//#endregion


//#region 정거장
class Station{
  String _nodeName, _nodeId, _nodeNum;
  String _xPos, _yPos;

  String get nodeName => _nodeName;
  String get nodeId => _nodeId;
  String get nodeNum => _nodeNum;
  String get xPos => _xPos;
  String get yPos => _yPos;

  Station(this._nodeName, this._nodeId, this._nodeNum, this._xPos, this._yPos);

  factory Station.fromJson(dynamic json){
    return Station(
        json['nodenm'] as String,
        json['nodeid'] as String,
        json['nodeno'] as String,
        json['gpslong'] as String,
        json['gpslati'] as String
    );
  }
}
//#endregion


//#region 버스노선
class Bus{
  String _routeType, _routeId, _routeNum;
  String _startTime, _endTime;
  String _startStation, _endStation;

  String get routeType => _routeType;
  String get routeNum => _routeNum;
  String get routeId => _routeId;
  String get startTime => _startTime;
  String get endTime => _endTime;
  String get startStation => _startStation;
  String get endStation => _endStation;

  Bus(this._routeType, this._routeId, this._routeNum, this._startTime, this._endTime, this._startStation, this._endStation);

  factory Bus.fromJson(dynamic json){
    return Bus(
        json['routetp'] as String,
        json['routeid'] as String,
        json['routeno'] as String,
        json['startvehicletime'] as String,
        json['endvehicletime'] as String,
        json['startnodenm'] as String,
        json['endnodenm'] as String
    );
  }
}
//#endregion


//#region 경로
class Way{
  String _deptName, _destName;
  String _deptArrTime, _destArrTime;
  String _deptArrStationCnt, _destArrStationCnt;
  String _routeType, _routeNum, _vehicleType;

  String get deptName => _deptName;
  String get destName => _destName;
  String get deptArrTime => _deptArrTime;
  String get destArrTime => _destArrTime;
  String get deptArrStationCnt => _deptArrStationCnt;
  String get destArrStationCnt => _destArrStationCnt;
  String get routeType => _routeType;
  String get routeNum => _routeNum;
  String get vehicleType => _vehicleType;

  Way(this._deptName, this._destName,
      this._deptArrTime, this._destArrTime,
      this._deptArrStationCnt, this._destArrStationCnt,
      this._routeType, this._routeNum, this._vehicleType);

  factory Way.fromJson(dynamic json){
    return Way(
      json['deptnodenm'] as String,
      json['destnodenm'] as String,
      json['deptarrtime'] as String,
      json['destarrtime'] as String,
      json['deptarrprevstationcnt'] as String,
      json['destarrprevstationcnt'] as String,
      json['routetp'] as String,
      json['routeno'] as String,
      json['vehicletp'] as String,
    );
  }
}
//#endregion


//#region GPS
class GPS{
  Future<Position> getLocation() async {
    bool serviceEnabled;
    LocationPermission permission;

    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if(serviceEnabled == false){
      return Future.error('Location service are disabled');
    }

    permission = await Geolocator.checkPermission();
    if(permission == LocationPermission.denied){
      permission = await Geolocator.requestPermission();

      if(permission == LocationPermission.denied){
        return Future.error('Location permission are denied');
      }
    }

    if(permission == LocationPermission.deniedForever){
      return Future.error('Location permission are permanently denied, we cannot request permission');
    }

    return await Geolocator.getCurrentPosition();
  }

  Future<String> getAddress() async {
    Position pos = await getLocation();
    List<Placemark> placeList = await placemarkFromCoordinates(pos.latitude, pos.longitude);
    Placemark place = placeList[0];

    if(placeList.length == 0){
      return "getAddress() ERROR";
    }
    return "${place.country}, ${place.locality}, ${place.street}, ${place.subLocality}, ${place.postalCode}, ${place.name}, ${place.administrativeArea}, ${place.country}";
  }
}
//#endregion