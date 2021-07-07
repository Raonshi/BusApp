
class ApproachItem{
  String _routeName, _routeType;
  String _nodeName;
  String _xPos, _yPos;

  String get routeName => _routeName;
  String get routeType => _routeType;
  String get nodeName => _nodeName;
  String get xPos => _xPos;
  String get yPos => _yPos;

  ApproachItem(
    this._routeName,
    this._routeType,
    this._nodeName,
    this._xPos,
    this._yPos
  );
}