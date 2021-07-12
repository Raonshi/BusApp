
class City{
  String cityName;
  String cityCode;

  City(this.cityName, this.cityCode);

  factory City.fromJson(dynamic json){
    return City(json['_cityName'] as String, json['_cityCode'] as String);
  }
}