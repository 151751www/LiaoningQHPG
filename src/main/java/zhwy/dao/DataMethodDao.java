package zhwy.dao;

import java.util.List;
import java.util.Map;

public interface DataMethodDao {
     List<Map<String,Object>> getXulieYanchangData(String timeType, String beginTime, String endTime, String stationNum, String obsvName) throws Exception;
}
