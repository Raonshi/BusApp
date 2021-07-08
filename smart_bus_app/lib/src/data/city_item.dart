class City{
  int _cityCode;
  String _cityName;

  int get cityCode => _cityCode;
  String get cityName => _cityName;

  City(this._cityCode, this._cityName);

  factory City.fromJson(dynamic json){
    return City(json['citycode'], json['cityname']);
  }
}