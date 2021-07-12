
class RouteNoItem{
  String _id;
  String _no;
  String _type;
  String _startTime, _endTime;
  String _startNode, _endNode;

  String get id => _id;
  String get no => _no;
  String get type => _type;
  String get startTime => _startTime;
  String get startNode => _startNode;
  String get endTime => _endTime;
  String get endNode => _endNode;

  RouteNoItem(this._id, this._no, this._type, this._startTime, this._endTime, this._startNode, this._endNode);
}