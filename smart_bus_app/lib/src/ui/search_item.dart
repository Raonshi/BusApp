import 'package:flutter/material.dart';
import 'package:smart_bus_app/src/ui/find_way_widget.dart';

enum TYPE {
  DEPARTURE,
  DESTINATION,
}
TYPE type;


class SearchItem extends StatelessWidget {
  String cityName;

  SearchItem(this.cityName, TYPE type, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: ListTile(
        leading: Icon(Icons.directions_bus_rounded),
        title: Text(cityName),
        onTap: (){
          Navigator.pop(context, cityName);
          /*
          switch(type){
            case TYPE.DEPARTURE:
              return Navigator.pop(context, cityName);
              break;
            case TYPE.DESTINATION:
              return Navigator.pop(context, cityName);
              break;
          }

           */
        },
        enabled: true,
      ),
    );
  }
}

