package zhwy.dao;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

public interface DataMethodDao {
     JSONArray getXulieYanchangData(String stationType, String timeType, String beginTime, String endTime, String stationNum, String obsvName, String name, String tiaojian,String[] prarm) throws Exception;
     String saveDuanxulie(List<Map<String,Object>> list,String dateType);
     String getXulieDingZheng(String timeType,String beginTime,String endTime,String stationNum,String obsvName);
}
