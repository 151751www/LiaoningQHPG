package zhwy.dao;

import com.alibaba.fastjson.JSONArray;

public interface DataAvgAndMDao {
    public String DataForAvg(String stationType,String dataType, String key,String value, String StartTime,String EndTime, String city,String stationNum,String suanfaType,String cnty);

    String getquahi(String stationType,String dataType, String obsveName, String startTime, String endTime, String stationNum,String city,String cnty);
    public double[] GetFileNiHeCanShuAB(JSONArray date, String X, String Y);
}
