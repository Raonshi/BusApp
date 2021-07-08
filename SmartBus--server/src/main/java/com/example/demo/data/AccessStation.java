package com.example.demo.data;

public class AccessStation {
    private String _routeId;
    private String _nodeId, _nodeName, _nodeOrder, _nodeNum;
    private String _xPos, _yPos;
    private String _direction;

    public AccessStation(String routeId,
                         String nodeId, String nodeName, String nodeOrder, String nodeNum,
                         String xPos, String yPos, String direction){

        this._routeId = routeId;
        this._nodeId = nodeId;
        this._nodeName = nodeName;
        this._nodeOrder = nodeOrder;
        this._nodeNum = nodeNum;
        this._xPos = xPos;
        this._yPos = yPos;
        this._direction = direction;
    }

    public String get_routeId() {
        return _routeId;
    }

    public String get_nodeId() {
        return _nodeId;
    }

    public String get_nodeName() {
        return _nodeName;
    }

    public String get_nodeOrder() {
        return _nodeOrder;
    }

    public String get_nodeNum() {
        return _nodeNum;
    }

    public String get_xPos() {
        return _xPos;
    }

    public String get_yPos() {
        return _yPos;
    }

    public String get_direction() {
        return _direction;
    }
}
