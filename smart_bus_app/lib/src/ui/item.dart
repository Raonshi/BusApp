import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:smart_bus_app/src/data/data.dart';

class BusItem extends StatelessWidget {
  Bus _busItem;

  Bus get busItem => _busItem;

  BusItem(this._busItem);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: ScreenUtilInit(
        designSize: Size(1440, 2560),
        builder: () => Container(
          margin: EdgeInsets.all(ScreenUtil().setWidth(10.0)),
          child: Card(
            color: Colors.blueAccent,
            shadowColor: Colors.blueGrey,
            elevation: 20.0,
            child: Row(
              children: [
                //버스 아이콘
                Padding(
                  padding: EdgeInsets.all(ScreenUtil().setWidth(10.0)),
                  child: Card(
                    color: Colors.white,
                    shadowColor: Colors.grey,
                    elevation: 20.0,
                    child: Icon(
                      Icons.directions_bus_sharp,
                      color: Colors.green,
                      size: 50.0,
                    ),
                  ),
                ),

                //버스 번호, 버스 종류
                Padding(
                  padding: EdgeInsets.all(ScreenUtil().setWidth(20.0)),
                  child: Column(
                    children: [
                      //첫번째 줄
                      Card(
                        color: Colors.white,
                        shadowColor: Colors.black,
                        elevation: 20.0,
                        child: Row(
                          children: [
                            //버스 번호
                            Container(
                              alignment: Alignment.centerLeft,
                              padding: EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
                              child: Text(
                                  _busItem.routeNum,
                                  style: TextStyle(
                                      fontSize: 20,
                                      fontWeight: FontWeight.bold
                                  )
                              ),
                            ),

                            //시내, 일반, 좌석 버스 구분
                            Container(
                              alignment: Alignment.centerRight,
                              child: Card(
                                color: Colors.green,
                                shadowColor: Colors.black,
                                elevation: 20.0,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(50)),
                                child: Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Text(_busItem.routeType),
                                ),
                              ),
                            )
                          ],
                        ),
                      ),
                      //두번째 줄
                      Container(
                        alignment: Alignment.centerLeft,
                        child: Card(
                          color: Colors.white,
                          shadowColor: Colors.black,
                          elevation: 20.0,
                          child: Padding(
                            padding: const EdgeInsets.all(3.0),
                            child: Text(
                                "${_busItem.startStation} <-> ${_busItem.endStation}",
                              style: TextStyle(fontSize: 15.0),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    onTap: () => Navigator.pop(context, _busItem),
    );
  }
}


class StationItem extends StatelessWidget {
  Station _stationItem;

  Station get stationItem => _stationItem;

  StationItem(this._stationItem);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: ScreenUtilInit(
        designSize: Size(1440, 2560),
        builder: () => Container(
          margin: EdgeInsets.all(ScreenUtil().setWidth(10.0)),
          child: Card(
            color: Colors.blueAccent,
            shadowColor: Colors.blueGrey,
            elevation: 20.0,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                //정거장 아이콘
                Expanded(
                  flex: 1,
                  child: Card(
                    color: Colors.white,
                    shadowColor: Colors.grey,
                    elevation: 20.0,
                    child: Icon(
                      Icons.where_to_vote_sharp,
                      color: Colors.green,
                      size: 50.0,
                    ),
                  ),
                ),

                //정거장 이름
                Expanded(
                  flex: 4,
                  child: Card(
                    color: Colors.white,
                    shadowColor: Colors.black,
                    elevation: 20.0,
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                          _stationItem.nodeName,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.bold
                          )
                      ),
                    ),
                  ),
                ),

                //정거장 번호
                Expanded(
                  flex: 1,
                  child: Card(
                    color: Colors.cyan,
                    shadowColor: Colors.black,
                    elevation: 20.0,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(50)),
                    child: Text(_stationItem.nodeNum, textAlign: TextAlign.center,),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    onTap: () => Navigator.pop(context, _stationItem),
    );
  }
}

class Wayitem extends StatelessWidget {

  String departure;
  String destination;

  Wayitem(String departure, String destination, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}

