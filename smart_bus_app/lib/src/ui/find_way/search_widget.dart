import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/provider/search_provider.dart';


class SearchBusWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<SearchProvider>(
      create: (_) => SearchProvider(),
      child: SearchBus(),
    );
  }
}


class SearchBus extends StatefulWidget {
  const SearchBus({Key key}) : super(key: key);

  @override
  _SearchBusState createState() => _SearchBusState();
}

class _SearchBusState extends State<SearchBus> {

  TextEditingController textController = TextEditingController();
  List<dynamic> busList = [];

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<SearchProvider>(context);

    print('call');
    return Scaffold(
      appBar: AppBar(
        title: Text('버스 노선 검색', style: TextStyle(fontSize: 30),),
      ),
      body: Column(
        children: [
          Row(
            children: [
              //검색창 입력
              Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: textController,
                      onSubmitted: (String str){
                        provider.search(str, Type.BUS);
                        provider.filtering();
                        busList = provider.list;
                      },
                    ),
                  )
              ),

              //검색창 입력 버튼
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: () {
                    provider.search(textController.text, Type.BUS);
                    provider.filtering();
                    busList = provider.list;
                  },
                  child: Text('검색'),
                ),
              ),
            ],
          ),

          //검색 결과 목록 출력
          Expanded(
              child: GestureDetector(
                onTap: () => FocusScope.of(context).unfocus(),
                child: ListView.builder(
                    itemBuilder: (context, index){
                      return busList[index];
                    },
                  itemCount: busList.length,
                  reverse: false,
                ),
              ),
          ),
        ],
      ),
    );
  }
}


class SearchStationWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<SearchProvider>(
      create: (_) => SearchProvider(),
      child: SearchStation(),
    );
  }
}

class SearchStation extends StatefulWidget {
  const SearchStation({Key key}) : super(key: key);

  @override
  _SearchStationState createState() => _SearchStationState();
}

class _SearchStationState extends State<SearchStation> {
  TextEditingController textController = TextEditingController();
  List<dynamic> stationList = [];

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<SearchProvider>(context);

    print('call');
    return Scaffold(
      appBar: AppBar(
        title: Text('정거장 검색', style: TextStyle(fontSize: 30),),
      ),
      body: Column(
        children: [
          Row(
            children: [
              //검색창 입력
              Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: textController,
                      onSubmitted: (String str){
                        provider.search(str, Type.STATION);
                        provider.filtering();
                        stationList = provider.list;
                      },
                    ),
                  )
              ),

              //검색창 입력 버튼
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: () {
                    provider.search(textController.text, Type.STATION);
                    provider.filtering();
                    stationList = provider.list;
                  },
                  child: Text('검색'),
                ),
              ),
            ],
          ),

          //검색 결과 목록 출력
          Expanded(
            child: GestureDetector(
              onTap: () => FocusScope.of(context).unfocus(),
              child: ListView.builder(
                itemBuilder: (context, index){
                  return stationList[index];
                },
                itemCount: stationList.length,
                reverse: false,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

