import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';
import 'package:logger/logger.dart';

//#region 날씨
enum WEATHER_TYPE{ SUN, RAIN, CLOUD, SNOW, }

class Weather{
  String dateTime;
  String temp;
  String weather;
  WEATHER_TYPE type;

  Weather({this.dateTime, this.temp, this.weather});

  factory Weather.fromJson(dynamic json){
    return Weather(
      dateTime: json['datetime'] as String,
      temp: json['temparature'] as String,
      weather: json['weather'] as String,
    );
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
enum DUST_TYPE{ LOW, MID, HIGH, DANGER, UNKNOWN}

class Dust{
  DUST_TYPE type = DUST_TYPE.UNKNOWN;
  int pm10Value = 0;
  String stationName = "Unknown";

  Dust({this.pm10Value, this.stationName});

  factory Dust.fromJson(dynamic json){
    int value = int.parse(json['pm10Value'] as String);
    return Dust(
      pm10Value: value,
      stationName: json['stationName'] as String,
    );
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
  String nodeName = "Unknown";
  String gpsLong = "Unknown";
  String gpsLati = "Unknown";
  String cityCode = "Unknown";
  String nodeId = "Unknown";

  Station({this.nodeName, this.nodeId, this.gpsLati, this.gpsLong, this.cityCode});

  factory Station.fromJson(dynamic json){
    return Station(
        nodeName: json['nodenm'] as String,
        nodeId: json['nodeid'] as String,
        cityCode: json['citycode'] as String,
        gpsLong: json['gpslong'] as String,
        gpsLati: json['gpslati'] as String
    );
  }
}
//#endregion


//#region 버스노선
class Bus{
  String nodeName = "";
  String routeTp = "";
  String routeId ="";
  String arrPrevStationCnt = "";
  String vehicleTp = "";
  String nodeId = "";
  String routeNo = "";
  String arrTime = "";

  Bus({this.nodeName, this.nodeId, this.routeTp, this.routeId,
    this.routeNo, this.vehicleTp, this.arrPrevStationCnt, this.arrTime});

  factory Bus.fromJson(dynamic json){
    return Bus(
      nodeName: json['nodenm'] as String,
      nodeId: json['nodeid'] as String,
      routeTp: json['routetp'] as String,
      routeId: json['routeid'] as String,
      routeNo: json['routeno'] as String,
      vehicleTp: json['vehicletp'] as String,
      arrPrevStationCnt: json['arrprevstationcnt'] as String,
      arrTime: json['arrtime'] as String,
    );
  }
}
/*
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
 */
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