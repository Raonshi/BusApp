import 'package:flutter/material.dart';


class FindWayWidget extends StatelessWidget {

  TextEditingController textController;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        child: Column(
          children: [
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 10.0),
              child: Row(
                children: [
                  Expanded(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10.0),
                        child: TextField(
                          controller: textController,
                          style: TextStyle(fontSize: 30),
                        ),
                      ),
                  ),
                  ElevatedButton(
                      onPressed: (){

                      },
                      style: ElevatedButton.styleFrom(primary: Colors.orange),
                      child: Padding(
                        padding: const EdgeInsets.all(3.0),
                        child: Text('검색', style: TextStyle(fontSize: 30),),
                      ))
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20.0),
              child: ElevatedButton(
                onPressed: (){

                },
                style: ElevatedButton.styleFrom(primary: Colors.green),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 30.0),
                  child: Text("출발", style: TextStyle(fontSize: 50,),),
                ),
              ),
            ),
            Padding(
              padding: EdgeInsets.symmetric(vertical: 10.0),
              child: Image.asset("images/line_circle.png", color: Colors.greenAccent,),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20.0),
              child: ElevatedButton(
                onPressed: (){

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
