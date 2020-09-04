package zhwy.dao;

public interface StationDao {
    public String  getCity(String stationType);
    public String getCounty(String stationType, String city);
    public String getStation(String stationType,String city,String cnty);

    String getAreaCode(String areaName, String areaType) throws Exception;
}
