package zhwy.service;

import com.alibaba.fastjson.JSONArray;

public interface DataAvgAndMService {

    public String getDataForAvg (String stationType,String dataType,String  key,String value, String StartTime,String EndTime, String city,String stationNum,String suanfaType,String cnty) ;


    String getStationQUShi(String stationType,String dataType, String obsveName, String startTime, String endTime, String stationNum,String city,String cnty);
    public String getABR(JSONArray array, String type);
}
