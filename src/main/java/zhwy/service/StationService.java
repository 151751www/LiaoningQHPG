package zhwy.service;

public interface StationService {
    public  String  getCity(String  stationType);
    public String getCounty(String stationType, String city);
    public String getRegStation(String stationType,String city,String cnty);
    String getAreaCode(String areaName, String areaType);
}
