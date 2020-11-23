package zhwy.service;

public interface StationService {
    public  String  getCity(String  stationType,String pageType);
    public String getCounty(String stationType, String city,String pageType);
    public String getRegStation(String stationType,String city,String cnty,String pageType);
    String getAreaCode(String areaName, String areaType);
    String checkFileName(String fileName) throws Exception;
}
