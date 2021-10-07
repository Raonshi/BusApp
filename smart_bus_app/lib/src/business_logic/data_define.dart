
//날씨
enum Weather{ SUN, RAIN, CLOUD, SNOW, }
//미세먼지
enum DUST{ LOW, MID, HIGH, DANGER, }

//정거장 객체
class Station{
  String name;
  String number;

  Station(this.name, this.number);
}