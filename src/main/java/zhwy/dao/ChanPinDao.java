package zhwy.dao;

import com.alibaba.fastjson.JSONArray;

public interface ChanPinDao {

    public JSONArray getSumYrarDate(String stationType,String beginTime,String endTime,String stationNum)throws  Exception;
    public JSONArray getJiZhiDate(String stationType,String beginTime,String endTime,String stationNum)throws  Exception;
    public JSONArray getAvgMathDate( String beginTime, String endTime, String stationNum,String obsv,String[] key,String tableName)throws  Exception;
}
