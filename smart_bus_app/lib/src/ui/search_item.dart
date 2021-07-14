import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/data/data.dart';
import 'package:smart_bus_app/src/ui/find_way_widget.dart';

/*
enum TYPE {
  DEPARTURE,
  DESTINATION,
}
TYPE type;
*/

class BusItem extends StatelessWidget {
  Bus _busItem;

  Bus get busItem => _busItem;

  BusItem(this._busItem);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          //버스 아이콘
          Icon(Icons.directions_bus_sharp, color: Colors.green,),
          Row(
            children: [
              //첫번째 줄
              Column(
                children: [
                  //버스 번호
                  Text(_busItem.routeNum, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                  //시내, 일반, 좌석 버스 구분
                  Card(
                    color: Colors.green,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(50)),
                    child: Text("시내"),
                  )
                ],
              ),
              //두번째 줄
              Text("출발지 < - > 도착지"),
            ],
          )
        ],
      ),
    );
  }
}




class SearchItem extends StatelessWidget {
  String _id, _no;
  String _startTime, _endTime;
  String _startNode, _endNode;

  String get id => _id;
  String get no => _no;
  String get startTime => _startTime;
  String get startNode => _startNode;
  String get endTime => _endTime;
  String get endNode => _endNode;

  SearchItem(this._id, this._no, this._startTime, this._endTime, this._startNode, this._endNode);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: ListTile(
        leading: Icon(Icons.directions_bus_rounded),
        title: Text("$no"),
        subtitle: Text("첫차 $startTime     막차 $endTime"),
        onTap: (){
          Navigator.pop(context, no);
        },
        enabled: true,
      ),
    );
  }
}

