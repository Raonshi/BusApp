import 'package:flutter/material.dart';

class SearchItem extends StatelessWidget {
  String cityName;

  SearchItem(this.cityName, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Row(children: [
        CircleAvatar(
          child: Text("a"),
          backgroundColor: Colors.lime,
        ),
        SizedBox(
          width: 16.0,
        ),
        Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(cityName, style: TextStyle(fontWeight: FontWeight.bold),),
              ],))
      ],),
    );
  }
}
