/// 본 스크립트는 앱의 메인 화면을 구성하는 위젯의 스크립트가 작성되어 있다.
/// find_way_widget.dart스크립트는 버스 노선 및 길찾기 관련 스크립트이다.
/// favorite_widget.dart스크립트는 즐겨찾기에 등록한 경로 목록을 관리하는 스크립트이다.
/// recently_widget.dart스크립트는 최근 검색기록을 보여주는 스크립트이다.
/// 작성자 : 홍순원

import 'package:flutter/material.dart';
import '../ui/favorite/favorite_widget.dart';
import '../ui/recently/recently_widget.dart';
import 'find_way_widget.dart';


class HomePage extends StatefulWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with SingleTickerProviderStateMixin {
  TabController tabController;
  bool isVisible = true;

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
      floatingActionButton: new Visibility(
          visible: isVisible,
          child: FloatingActionButton(
            backgroundColor: Color.fromARGB(255, 255, 184, 48),
            child: Text("삭제", style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.black),),
            onPressed: (){},
          )),
    );
  }
}