import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/business_logic/data_define.dart';


class BusItem extends StatelessWidget {
  Bus bus;
  BusItem({Key key, this.bus}):super(key: key);

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
                child: Text(bus.nodeName, textAlign: TextAlign.left, style: TextStyle(fontSize: 20),),
              ),
            ),

            Expanded(
              flex:1,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
                child: Text(bus.arrTime, textAlign: TextAlign.right, style: TextStyle(fontSize: 20),),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
