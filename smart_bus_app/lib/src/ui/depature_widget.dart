import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/provider/departure_provider.dart';
import 'package:smart_bus_app/src/ui/search_item.dart';


class DepartureWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<DepartureProvider>(
      create: (_) => DepartureProvider(),
      child: SearchDeparture(),
    );
  }
}


class SearchDeparture extends StatefulWidget {
  const SearchDeparture({Key key}) : super(key: key);

  @override
  _SearchDepartureState createState() => _SearchDepartureState();
}

class _SearchDepartureState extends State<SearchDeparture> {

  TextEditingController textController = TextEditingController();
  List<SearchItem> departureList = [];


  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<DepartureProvider>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text('출발지 검색', style: TextStyle(fontSize: 30),),
      ),
      body: Column(
        children: [
          Row(
            children: [
              Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: textController,
                      onSubmitted: (String str){
                        provider.setDeparture(str);
                        departureList = provider.filtering();
                      },
                    ),
                  )
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: () {
                    provider.setDeparture(textController.text);
                    departureList = provider.filtering();
                  },
                  child: Text('검색'),
                ),
              ),
            ],
          ),
          //Text(provider.departure),
          Expanded(
              child: ListView.builder(
                  itemBuilder: (context, index){
                    return departureList[index];
                  },
                itemCount: departureList.length,
                reverse: false,
              ),
          ),
        ],
      ),
    );
  }
}

