import 'package:flutter/material.dart';
import 'favorite_widget.dart';
import 'recently_widget.dart';
import 'find_way_widget.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with SingleTickerProviderStateMixin {
  TabController tabController;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    tabController = TabController(
        vsync: this,
        length: 3
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
        appBar: AppBar(
          centerTitle: true,
          title: Text("찾 길", style: TextStyle(fontSize: 30,),),
          bottom: TabBar(
            controller: tabController,
            tabs: [
              Tab(text: '경로찾기', icon: Icon(Icons.directions_bus),),
              Tab(text: '최근기록', icon: Icon(Icons.list_alt_rounded)),
              Tab(text: '즐겨찾기', icon: Icon(Icons.star)),
            ],
          ),
        ),
        body: TabBarView(
          controller: tabController,
          children: [FindWayWidget(), RecentlyWidget(), FavoriteWidget()],
        ),
    );
  }
}