import 'package:flutter/material.dart';

class SelectionWidget extends StatelessWidget {
  String departure;
  String destination;

  SelectionWidget(this.departure, this.destination, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('$departure → $destination'),
      ),
    );
  }
}
