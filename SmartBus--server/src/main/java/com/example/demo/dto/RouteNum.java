package com.example.demo.dto;

public class RouteNum {
    private String _routeId, _routeNum, _routeType;
    private String _startNode, _endNode;
    private String _startTime, _endTime;

    public RouteNum(String routeId, String routeNum, String routeType,
                     String startNode, String endNode, String startTime, String endTime){

        this._routeId = routeId;
        this._routeNum = routeNum;
        this._routeType = routeType;
        this._startNode = startNode;
        this._endNode = endNode;
        this._startTime = startTime;
        this._endTime = endTime;
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
}
