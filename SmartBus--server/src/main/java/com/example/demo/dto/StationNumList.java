package com.example.demo.dto;

public class StationNumList {
    private String _gpslati, _gpslong;
    private String _nodeId, _nodeNm, _nodeNo;


    public StationNumList() {

    }

    public StationNumList(String gpslati, String gpslong, String nodeId, String nodeNm, String nodeNo) {
        this._gpslati = gpslati;
        this._gpslong = gpslong;
        this._nodeId = nodeId;
        this._nodeNm = nodeNm;
        this._nodeNo = nodeNo;
    }

    public String get_gpslati() {
        return _gpslati;
    }

    public String get_gpslong() {
        return _gpslong;
    }

    public String get_nodeId() {
        return _nodeId;
    }

    public String get_nodeNm() {
        return _nodeNm;
    }

    public String get_nodeNo() {
        return _nodeNo;
    }
}
