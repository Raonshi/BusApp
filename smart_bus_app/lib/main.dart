import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/serialization/city.dart';
import 'package:smart_bus_app/src/ui/home_page.dart';
import 'package:smart_bus_app/src/web_server.dart';


void main(){
  //WebServer().getBusList("82");
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Smart Bus",
      home: HomePage(),
    );
  }
}
