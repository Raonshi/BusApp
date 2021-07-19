import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/data/data.dart';
import 'package:smart_bus_app/src/provider/find_way_provider.dart';
import 'package:smart_bus_app/src/web_server.dart';

class SelectionWidget extends StatelessWidget {
  FindWayProvider provider;

  SelectionWidget(this.provider, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text('${provider.departure.nodeName} → ${provider.destination.nodeName}'),
      ),
      body: MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => FindWayProvider(provider.departure, provider.destination)),
        ],
        child: Body(),
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
  List<dynamic> wayList;
  bool recentlyUpLoad = false;

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<FindWayProvider>(context);

    Logger().d("Selection Widget Updated!");

    provider.finding();
    //saveRecently(provider.departure, provider.destination);
    wayList = provider.wayList;

    return Center(
      child: Column(
        children: [
          Text('${provider.departure.nodeName} 부터 ${provider.destination.nodeName}'),
          //검색 결과 목록 출력
          Expanded(
            child: GestureDetector(
              onTap: () => FocusScope.of(context).unfocus(),
              child: ListView.builder(
                itemBuilder: (context, index){
                  return wayList[index];
                },
                itemCount: wayList.length,
                reverse: false,
              ),
            ),
          ),
        ],
      ),
    );
  }

  ///최근 기록 db에 저장
  void saveRecently(Station departure, Station destination){
    if(recentlyUpLoad){
      return;
    }

    WebServer().postRecently(departure, destination);

    recentlyUpLoad = true;
  }
}

