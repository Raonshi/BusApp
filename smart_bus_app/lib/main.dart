/// 본 스크립트는 앱이 실행될 때 최초도 실핼되는 스크립트이다.
/// 작성자 : 홍순원

import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/ui/home_page.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Smart Bus",
      home: HomePage(),
    );
  }
}
