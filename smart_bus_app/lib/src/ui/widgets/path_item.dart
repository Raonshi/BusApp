import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';


class PathItem extends StatelessWidget {
  //Way path;
  //PathItem(this.path);

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Color.fromARGB(0, 0, 0, 0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,

        children: [
          Expanded(
            flex: 1,
            child:Text("출발"),
          ),

          Expanded(
            flex: 1,
            child: Text("도착"),
          ),
        ],
      ),
    );
  }
}
