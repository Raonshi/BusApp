package com.example.demo.data;

public class RouteInfo {
	private String in_routeId, in_cityCode;
	
    private String _routeId, _routeNum, _routeType;
    private String _startNode, _endNode;
    private String _startTime, _endTime;
    private String _interval, _intervalSat, _intervalSun;
    
    public RouteInfo() {
    	
    }

    public RouteInfo(String routeId, String routeNum, String routeType,
                     String startNode, String endNode, String startTime, String endTime,
                     String interval, String intervalSat, String intervalSun){

        this._routeId = routeId;
        this._routeNum = routeNum;
        this._routeType = routeType;
        this._startNode = startNode;
        this._endNode = endNode;
        this._startTime = startTime;
        this._endTime = endTime;
        this._interval = interval;
        this._intervalSat = intervalSat;
        this._intervalSun = intervalSun;
    }
    
    public void set_in_cityCode(String in_cityCode) {
    	this.in_cityCode = in_cityCode;
    }
    
    public void set_in_routeId(String in_routeId) {
    	this.in_routeId = in_routeId;
    }
    
    public String get_in_cityCode() {
    	return in_cityCode;
    }
    
    public String get_in_routeId() {
    	return in_routeId;
    }
    
    

    public String get_routeId() {
        return _routeId;
    }

    public String get_routeNum() {
        return _routeNum;
    }

    public String get_routeType() {
        return _routeType;
    }

    public String get_startNode() {
        return _startNode;
    }

    public String get_endNode() {
        return _endNode;
    }

    public String get_startTime() {
        return _startTime;
    }

    public String get_endTime() {
        return _endTime;
    }

    public String get_interval() {
        return _interval;
    }

    public String get_intervalSat() {
        return _intervalSat;
    }

    public String get_IntervalSun() {
        return _intervalSun;
    }
}
