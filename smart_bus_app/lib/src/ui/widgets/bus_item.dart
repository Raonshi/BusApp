import 'dart:async';
import 'dart:core';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';


class BusItem extends StatefulWidget {
  final controller = Get.put(Controller());
  int index;
  BusItem({this.index});

  @override
  _BusItemState createState() => _BusItemState();
}

class _BusItemState extends State<BusItem> {
  Timer timer;
  int arrTime = 0;
  String time = "00:00";
  Bus bus;

  _BusItemState();

  @override
  void initState() {
    // TODO: implement initState
    bus = widget.controller.busList.value[widget.index];
    arrTime = int.parse(bus.arrTime);
    super.initState();
    calcTime();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    timer.cancel();

    Logger().d(widget.index);
    widget.controller.busList.removeAt(widget.index);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {

    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 10.0, vertical: 10.0),
      child: Card(
        color: Colors.greenAccent,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
              flex:1,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
                child: Text(
                  bus.routeNo,
                  textAlign: TextAlign.left,
                  style: TextStyle(fontSize: 20),
                ),
              ),
            ),

            Expanded(
              flex:1,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
                //child: Text(bus.arrTime, textAlign: TextAlign.right, style: TextStyle(fontSize: 20),),
                child: Text(time,
                  textAlign: TextAlign.right,
                  style: TextStyle(fontSize: 20),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void calcTime() {
    timer = Timer.periodic(Duration(seconds: 1), (timer) {
      setState(() {
        String minStr = "${(arrTime~/60).toInt()}".padLeft(2, '0');
        String secStr = "${arrTime%60}".padLeft(2, '0');

        time = "${minStr} : ${secStr}";
        arrTime--;

        if(arrTime <= 0){
          time = "00:00";
          dispose();
        }
      });
    });
  }
}
