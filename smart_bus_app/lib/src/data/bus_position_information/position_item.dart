
class PositionItem{
  String _routeName, _routeType;
  String _xPos, _yPos;
  String _nodeOrder, _nodeName, _nodeId;
  String _vehicleNum;

  String get routeName => _routeName;
  String get routeType => _routeType;
  String get xPos => _xPos;
  String get yPos => _yPos;
  String get nodeOrder => _nodeOrder;
  String get nodeName => _nodeName;
  String get nodeId => _nodeId;
  String get vehicleNum => _vehicleNum;

  PositionItem(
    this._routeName,
    this._routeType,
    this._xPos,
    this._yPos,
    this._nodeId,
    this._nodeName,
    this._nodeOrder,
    this._vehicleNum
  );
}