import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/provider/find_way_provider.dart';
import 'package:smart_bus_app/src/provider/search_provider.dart';

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


  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // 최근기록에 저장한다.
    saveRecently();
  }

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<FindWayProvider>(context);

    return Center(
      child: Text('${provider.departure} 부터 ${provider.destination}'),
    );
  }

  ///최근 기록 db에 저장
  void saveRecently(){

  }
}

