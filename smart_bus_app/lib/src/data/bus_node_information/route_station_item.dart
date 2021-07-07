
class RouteStationItem {
  String _routeId;
  String _nodeId, _nodeName, _nodeOrder, _nodeNum;
  String _xPos, _yPos;
  String _direction;

  String get routeId => _routeId;
  String get nodeId => _nodeId;
  String get nodeName => _nodeName;
  String get nodeOrder => _nodeOrder;
  String get nodeNum => _nodeNum;
  String get xPos => _xPos;
  String get yPos => _yPos;
  String get direction => _direction;

  RouteStationItem(
    this._routeId,
    this._nodeId,
    this._nodeName,
    this._nodeOrder,
    this._nodeNum,
    this._xPos,
    this._yPos,
    this._direction
  );
}