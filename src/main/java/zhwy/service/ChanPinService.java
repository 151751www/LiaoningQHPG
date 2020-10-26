package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;
import java.util.Map;

public interface ChanPinService {
    public JSONArray getSumYrarDate(String stationType, String beginTime, String endTime, String stationNum)throws  Exception;
    public JSONArray getJiZhiDate(String stationType,String beginTime,String endTime,String stationNum)throws  Exception;
    public JSONArray getAvgMathDate(String stationType, String beginTime, String endTime, String stationNum,String obsv)throws  Exception;
    public List<Map<String,Object>> getAvgYearDate(String stationType, String beginTime, String endTime, String stationNum, String obsv)throws  Exception;
    public  void insertTitle(XWPFParagraph paragraph);
    public JSONArray getSectionData(String keyInParaText,String stationType, String beginTime, String endTime, String beginTime2, String endTime2, String stationNum, String stationName)throws Exception ;


    public String FileMake(String stationType, String beginTime, String endTime, String stationNum)throws  Exception;
    public void insertTable(XWPFDocument document, XWPFParagraph paragraph, JSONArray array, String [] tableTitle)throws Exception;
    public  void replaceTableName(XWPFParagraph paragraph,String staNum,String Year,String keyInParaText)throws  Exception;
}
