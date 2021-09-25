import 'package:flutter/material.dart';

class FavoriteWidget extends StatelessWidget {
  List list = List<FavoriteItem>(10);

  @override
  Widget build(BuildContext context) {

    list[0] = FavoriteItem("지하상가", "청주고등학교", true);
    list[1] = FavoriteItem("도청", "상당산성", false);
    list[2] = FavoriteItem("청주체육관", "청주의료원", true);
    list[3] = FavoriteItem("명암저수지", "사직사거리", true);
    list[4] = FavoriteItem("중앙여자고등학교", "사창사거리", false);
    list[5] = FavoriteItem("육거리", "충북고등학교", false);
    list[6] = FavoriteItem("상당공원", "서원대학교", false);
    list[7] = FavoriteItem("우암초등학교", "청주대학교", false);
    list[8] = FavoriteItem("지하상가", "산업단지입구", true);
    list[9] = FavoriteItem("원불교충북교구청", "청주동물원", false);

    return Column(
      children: [
        Expanded(
        child: ListView.builder(
          itemBuilder: (BuildContext, index){
            return list[index];
          },
          itemCount: list.length,
        ),
      ),
    ]);
  }
}


class FavoriteItem extends StatelessWidget {
  String start, end;
  bool isCheck = false;

  FavoriteItem(this.start, this.end, this.isCheck);

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
              padding: const EdgeInsets.all(0.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Expanded(flex:1, child: Checkbox(value: isCheck,)),
                  Expanded(flex: 4,
                      child: Text(start, textAlign: TextAlign.right,
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold),)),
                  Expanded(flex: 1,
                      child: Icon(Icons.double_arrow,
                        color: Color.fromARGB(255, 255, 184, 48),)),
                  Expanded(flex: 4,
                      child: Text(end, textAlign: TextAlign.left,
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold),)),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}

