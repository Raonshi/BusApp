import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/ui/home_page.dart';
import 'package:smart_bus_app/src/web/web_server.dart';

void main(){
  WebServer().citySupport("평택시");


  //runApp(MyApp());
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
