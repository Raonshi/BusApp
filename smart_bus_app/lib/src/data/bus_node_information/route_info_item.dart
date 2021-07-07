class RouteInfoItem{
  String _id;
  String _no;
  String _type;
  String _startNode, _endNode;
  String _startTime, _endTime;
  String _interval, _intervalSat, _intervalSun;

  String get id => _id;
  String get no => _no;
  String get type => _type;
  String get startNode => _startNode;
  String get endNode => _endNode;
  String get startTime => _startTime;
  String get endTime => _endTime;
  String get interval => _interval;
  String get intervalSat => _intervalSat;
  String get intervalSun => _intervalSun;

  RouteInfoItem(
      this._id,
      this._no,
      this._type,
      this._startNode,
      this._endNode,
      this._startTime,
      this._endTime,
      this._interval,
      this._intervalSat,
      this._intervalSun
      );
}