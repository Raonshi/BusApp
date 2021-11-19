import 'dart:async';

import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';
import 'package:get/get.dart';
import 'package:smart_bus_app/src/business_logic/get_controller.dart';
import 'package:smart_bus_app/src/ui/find_way/path_finding_widget.dart';


class PathItem extends StatelessWidget {
  Path path;
  final controller = Get.put(Controller());

  PathItem({this.path});

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.greenAccent,
      child: ListTile(
        leading: Icon(Icons.directions_bus, size: 30, color: Colors.blueAccent,),
        title: Text(path.subPath[0].routeno),
        trailing: Text(calcTotalTime()),
        onTap: (){
          Get.to(() => PathFindingPage(path: path.subPath[0]));
        },
      ),
    );
  }

  String calcTotalTime(){
    int totalTime = int.parse(path.subPath[0].totaltime);
    String minStr = "${totalTime ~/ 60}".padLeft(2, '0');
    return "$minStr 분 소요";
  }
}
