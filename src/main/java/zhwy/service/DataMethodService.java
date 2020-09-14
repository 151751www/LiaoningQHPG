package zhwy.service;

import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface DataMethodService {
     List<Map<String,Object>> getXulieYanchangData(String stationType,String timeType, String beginTime, String endTime, String stationNum, String obsvName,String name,String tiaojian) throws Exception;
     List<Map<String,Object>> getYanchangDataResult(List<Map<String,Object>> dtData)throws Exception;
     List<Map<String,Object>> getHeBingDataResult(List<Map<String,Object>> LData,List<Map<String,Object>> SData,String timeType) throws ParseException;
     String getFileContent(MultipartFile multipartFile,String type,String dataType) ;
     String saveDuanxulie(List<Map<String,Object>> list,String dateType);
     String getXulieDingZheng(String timeType, String beginTime, String endTime, String stationNum, String obsvName);
}
