/// [data.dart] 정의
/// 앱에서 사용되는 모든 데이터의 클래스가 정의된 문서
///
/// 작성자 : 홍순원

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


class Recently{
  Station _departure, _destination;

  Station get departure => _departure;
  Station get destination => _destination;

  Recently(this._departure, this._destination);

  factory Recently.fromJson(dynamic json){
    return Recently(
        Station.fromJson(json['departure']),
        Station.fromJson(json['destination'])
    );
  }
}