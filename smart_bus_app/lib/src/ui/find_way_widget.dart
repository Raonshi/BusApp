import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/data/data.dart';
import 'package:smart_bus_app/src/provider/find_way_provider.dart';
import 'package:smart_bus_app/src/provider/search_provider.dart';
import 'package:smart_bus_app/src/ui/depature_widget.dart';
import 'package:smart_bus_app/src/ui/destination_widget.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';
import 'package:smart_bus_app/src/ui/search_widget.dart';
import 'package:smart_bus_app/src/ui/selection_widget.dart';

import 'depature_widget.dart';


class FindWayWidget extends StatelessWidget {

  //String departure;
  //String destination;
  //String busNum;
  Station departure;
  Station destination;
  Bus busNum;

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => FindWayProvider(departure, destination)),
        ChangeNotifierProvider(create: (_) => SearchProvider())
      ],
      child: Center(
        child: Body()
      ),
    );
  }
}

class Body extends StatefulWidget {
  const Body({Key key}) : super(key: key);

  @override
  _BodyState createState() => _BodyState();
}

class _BodyState extends State<Body> {
  String keyword;

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<FindWayProvider>(context);

    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children:[
        //버스 노선으로 찾기
        Padding(
          padding: const EdgeInsets.only(bottom: 20.0),
          child: ElevatedButton(
            onPressed: (){
              getBusNum(context, provider);
            },
            style: ElevatedButton.styleFrom(primary: Colors.blue),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical : 10.0),
              child: Text("버스 노선 찾기", style: TextStyle(fontSize: 20,),),
            )
          )
        ),


        //출발지 버튼
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 20.0),
          child: ElevatedButton(
            onPressed: (){
              getDeparture(context, provider);
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
              getDestination(context, provider);
            },
            style: ElevatedButton.styleFrom
              (primary: Colors.redAccent),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 30.0),
              child: Text("도착", style: TextStyle(fontSize: 50,),),
            ),
          ),
        ),
      ],
    );
  }

  void getDeparture(BuildContext context, FindWayProvider provider) async {
    Station departure = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => SearchStationWidget()
        )
    );

    provider.setDeparture(departure);

    /*
    //알림 출력
    ScaffoldMessenger.of(context)
      ..removeCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text('출발지 설정 : $departure')));

     */
  }

  void getDestination(BuildContext context, FindWayProvider provider) async {
    Station destination = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => SearchStationWidget()
        )
    );

    provider.setDestination(destination);


    /*
    //알림 출력
    ScaffoldMessenger.of(context)
      ..removeCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text('도착지 설정 : $destination')));

     */

    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => SelectionWidget(provider)
        )
    );
  }

  void getBusNum(BuildContext context, FindWayProvider provider) async {
    BusItem busItem = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => SearchBusWidget()
        )
    );

    //provider.search(, Type.BUS);

    //알림 출력
    /*
    ScaffoldMessenger.of(context)
      ..removeCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text('출발지 설정 : $departure')));
     */
  }
}
