import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/ui/home_page.dart';
import 'package:smart_bus_app/src/web/web_server.dart';

void main(){
  //앱 실행 시 모든 데이터를 불러와야한다.
  WebServer().getRouteNo("청주", "862");
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
