/// [data.dart] 정의
/// 앱에서 사용되는 모든 데이터의 클래스가 정의된 문서
///
/// 작성자 : 홍순원


class Bus{
  String _routeNum;
  String _routeId;

  String get routeNum => _routeNum;
  String get routeId => _routeId;

  Bus(this._routeNum, this._routeId);

  factory Bus.fromJson(dynamic json){
    return Bus(json['routeNum'] as String, json['routeNum'] as String);
  }
}

class Station{

}