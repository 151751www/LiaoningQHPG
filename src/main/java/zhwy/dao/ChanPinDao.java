package zhwy.dao;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

public interface ChanPinDao {

    public JSONArray getSumYrarDate(String stationType,String beginTime,String endTime,String stationNum)throws  Exception;
    public JSONArray getJiZhiDate(String stationType,String beginTime,String endTime,String stationNum)throws  Exception;
    public List<Map<String,Object>> getAvgMathDate(String beginTime, String endTime, String stationNum, String obsv, String[] key, String tableName)throws  Exception;
    public List<Map<String,Object>> getAvgYearDate(String beginTime, String endTime, String stationNum, String obsv, String tableName)throws  Exception;
}
