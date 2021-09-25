import 'package:flutter/material.dart';

class RecentlyWidget extends StatelessWidget {
  List list = List<RecentlyItem>(10);

  @override
  Widget build(BuildContext context) {

    list[0] = RecentlyItem("지하상가", "청주고등학교");
    list[1] = RecentlyItem("도청", "상당산성");
    list[2] = RecentlyItem("청주체육관", "청주의료원");
    list[3] = RecentlyItem("명암저수지", "사직사거리");
    list[4] = RecentlyItem("중앙여자고등학교", "사창사거리");
    list[5] = RecentlyItem("육거리", "충북고등학교");
    list[6] = RecentlyItem("상당공원", "서원대학교");
    list[7] = RecentlyItem("우암초등학교", "청주대학교");
    list[8] = RecentlyItem("지하상가", "산업단지입구");
    list[9] = RecentlyItem("원불교충북교구청", "청주동물원");

    return Container(
      child: ListView.builder(
        itemBuilder: (BuildContext, index){
          return list[index];
        },
        itemCount: list.length,
      ),
    );
  }
}

class RecentlyItem extends StatelessWidget {
  String start, end;

  RecentlyItem(this.start, this.end);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(8.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Card(
            color: Color.fromARGB(255, 181, 222, 255),
            child: Padding(
              padding: const EdgeInsets.all(12.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Expanded(flex: 4, child: Text(start, textAlign: TextAlign.right, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),)),
                  Expanded(flex:1, child: Icon(Icons.double_arrow, color: Color.fromARGB(255, 255, 184, 48),)),
                  Expanded(flex: 4, child: Text(end, textAlign: TextAlign.left, style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),)),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}

