import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/ui/home_page.dart';
import 'package:smart_bus_app/src/web/web_server.dart';

<<<<<<< HEAD
void main() {


=======
void main(){
  //앱 실행 시 모든 데이터를 불러와야한다.
  //WebServer().getRouteNo("청주", "862");
  WebServer().testing();
>>>>>>> b15eccf323b68e6d4464957950d60a0ca817181d
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
