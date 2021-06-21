import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/provider/departure_provider.dart';
import 'package:smart_bus_app/src/ui/depature_widget.dart';
import 'package:smart_bus_app/src/ui/destination_widget.dart';

import 'depature_widget.dart';


class FindWayWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children:[

            //출발지 버튼
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20.0),
              child: ElevatedButton(
                onPressed: (){
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => DepartureWidget()
                      )
                  );
                },
                style: ElevatedButton.styleFrom(primary: Colors.green),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 30.0),
                  child: Text("출발", style: TextStyle(fontSize: 50,),),
                ),
              ),
            ),

            //점선
            Padding(
              padding: EdgeInsets.symmetric(vertical: 10.0),
              child: Image.asset("images/line_circle.png", color: Colors.greenAccent,),
            ),

            //도착지 버튼
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20.0),
              child: ElevatedButton(
                onPressed: (){
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => DestinationWidget()
                      )
                  );
                },
                style: ElevatedButton.styleFrom(primary: Colors.redAccent),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 30.0),
                  child: Text("도착", style: TextStyle(fontSize: 50,),),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
