package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface DataMethodService {
     JSONArray getXulieYanchangData(String stationType, String timeType, String beginTime, String endTime, String stationNum, String obsvName, String name, String tiaojian,String [] prarm) throws Exception;
     List<Map<String,Object>> getYanchangDataResult(List<Map<String,Object>> dtData)throws Exception;
     List<Map<String,Object>> getHeBingDataResult(List<Map<String,Object>> LData,List<Map<String,Object>> SData,String timeType) throws ParseException;
     String getFileContent(MultipartFile multipartFile,String type,String dataType) ;
     String saveDuanxulie(List<Map<String,Object>> list,String dateType);
     String getXulieDingZheng(String timeType, String beginTime, String endTime, String stationNum, String obsvName);
}
