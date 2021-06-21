import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smart_bus_app/src/provider/destination_provider.dart';

class DestinationWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<DestinationProvider>(
      create: (_) => DestinationProvider(),
      child: SearchDestination(),
    );
  }
}


class SearchDestination extends StatefulWidget {

  @override
  _SearchDestinationState createState() => _SearchDestinationState();
}

class _SearchDestinationState extends State<SearchDestination> {

  TextEditingController textController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<DestinationProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text('도착지 검색', style: TextStyle(fontSize: 30),),
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
                    ),
                  )
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: () {
                    provider.setDeparture(textController.text);
                  },
                  child: Text('검색'),
                ),
              ),
            ],
          ),
          Text(provider.destination),
        ],
      ),
    );
  }
}
