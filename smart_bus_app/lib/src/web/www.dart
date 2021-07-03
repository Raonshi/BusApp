import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';
import 'package:xml2json/xml2json.dart';

class WWW{

  Future<String> getBusInfo(int cityCode, String routeId) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "cityCode" : cityCode.toString(),
      "routeId" : routeId
    };

    Uri uri = Uri.http(address, serviceName, params);
    http.Response response = await http.get(uri);

    if(response.statusCode == 200){
      Logger().d("Success!!!");
      Logger().d(utf8.decode(response.bodyBytes));
    }
    else{
      Logger().d("${response.statusCode} : Failed!!!");
    }
  }

  ///버스 정류장 정보를 반환한다.
  Future<String> getStationInfo(int cityCode, String routeId, String nodeId) async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusLcInfoInqireService/getRouteAcctoSpcifySttnAccesBusLcInfo";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
      "routeId" : routeId,
      "nodeId" : nodeId,
      "cityCode" : cityCode.toString(),
    };

    Uri uri = Uri.http(address, serviceName, params);
    return Future.delayed(Duration(seconds: 1), () => getData(uri));
  }

  ///서비스 가능 지역 목록을 반환한다.
  Future<String> getServiceRegion() async {
    final address = "openapi.tago.go.kr";
    final serviceName = "/openapi/service/BusLcInfoInqireService/getCtyCodeList";
    final params = {
      "serviceKey" : "jQtEtCvhFPgTRrmSxikfgvg1fMV/H19VWwaxeLb3X+fiVfNhWybyEsq/Tnv1uQtBMITUQNlWlBPaV3lqr3pTHQ==",
    };

    Uri uri = Uri.http(address, serviceName, params);
    var result = await getData(uri);
    return result.toString();
  }

  ///[uri]를 통해 요청한 공공데이터를 반환한다.
  Future<String> getData(Uri uri) async {
    final xml2json = Xml2Json();
    http.Response response = await http.get(uri);

    if(response.statusCode == 200){
      final xml = utf8.decode(response.bodyBytes);
      Logger().d("XML : $xml");
      xml2json.parse(xml);
      final json = xml2json.toParker();
      Logger().d("JSON : ${json[0]}");

      Map map = jsonDecode(json);

      Logger().d("MAP KEYS : ${map.keys}");
      Logger().d("MAP VALUES : ${map.values}");

      return Future.delayed(Duration(seconds: 1), () => json);
    }
    else{
      Logger().d("${response.statusCode} : Failed!!!");
    }
  }
}