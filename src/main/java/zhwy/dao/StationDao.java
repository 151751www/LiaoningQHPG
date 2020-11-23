package zhwy.dao;

public interface StationDao {
    public String  getCity(String stationType,String pageType);
    public String getCounty(String stationType, String city,String pageType);
    public String getStation(String stationType,String city,String cnty,String pageType);

    String getAreaCode(String areaName, String areaType) throws Exception;

    Boolean getstation(String iiiii) throws Exception;
}
