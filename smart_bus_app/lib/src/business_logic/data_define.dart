import 'package:flutter/services.dart';
import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';
import 'package:imei_plugin/imei_plugin.dart';
import 'package:logger/logger.dart';

//날씨
enum WEATHER_TYPE{ SUN, RAIN, CLOUD, SNOW, }

class Weather{

}

//미세먼지
enum DUST_TYPE{ LOW, MID, HIGH, DANGER, }

class Dust{

}


//정거장 객체
/*
class Station{
  String name;
  String number;

  Station(this.name, this.number);
}
*/
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



//GPS정보
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


//디바이스 정보
class DeviceInfo{
  String _imei = "Unknown";
  String _uid = "Unknown";

  void getImei() async {
    String imei, uid;
    try{
      imei = await ImeiPlugin.getImei(shouldShowRequestPermissionRationale: false);
      uid = await ImeiPlugin.getId();
    }
    on PlatformException{
      imei = "Failed_IMEI";
      uid = "Failed_UID";
    }

    _imei = (imei != "Failed_IMEI") ? imei : _imei;
    _uid = (uid != "Failed_UID") ? uid : _uid;
  }
}