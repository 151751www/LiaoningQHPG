package zhwy.dao;

import java.util.List;
import java.util.Map;

public interface DataMethodDao {
     List<Map<String,Object>> getXulieYanchangData(String stationType,String timeType, String beginTime, String endTime, String stationNum, String obsvName,String name,String tiaojian) throws Exception;
     String saveDuanxulie(List<Map<String,Object>> list,String dateType);
     String getXulieDingZheng(String timeType,String beginTime,String endTime,String stationNum,String obsvName);
}
