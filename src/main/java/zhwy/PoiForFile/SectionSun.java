package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service()
public class SectionSun extends BaseSection {

    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="1.5 日照时数";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{

        double sum_avg_min=0;
        String sum_min_month="";
        double sum_avg_max=0.0;
        String sum_max_month="";
        int len=0;

        JSONObject sumObjece;
        Map<String,Double>map=new HashMap<>();
        double sum=0.0;
        double avg;
        String sum_avg_year=leinianzhi.getJSONObject(0).getString("val");
        for (int i=0;i<avgMonth.size();i++){
            sumObjece=avgMonth.getJSONObject(i);
            if(sumObjece.containsKey("val")){
                JSONArray valarr=sumObjece.getJSONArray("val");
                if(valarr.get(11)!=null){
                    sum_avg_min=Double.parseDouble(valarr.get(11).toString());
                    sum_min_month=12+"";
                    sum_avg_max=Double.parseDouble(valarr.get(11).toString());
                    sum_max_month=12+"";
                    len=1;
                    sum=Double.parseDouble(valarr.get(11).toString());
                }
                for (int v=0;v<valarr.size()-1;v++){
                    Object object=valarr.get(v);
                    if(object!=null){
                        if(Double.parseDouble(object.toString())>sum_avg_max){
                            sum_avg_max=Double.parseDouble(object.toString());
                            sum_max_month=(v+1)+"";
                        }else if(Double.parseDouble(object.toString())<sum_avg_min){
                            sum_avg_min=Double.parseDouble(object.toString());
                            sum_min_month=(v+1)+"";
                        }
                        sum+=Double.parseDouble(object.toString());
                        len+=1;
                        if(v==1||v==4||v==7||v==10){
                            avg=sum/len;
                            avg= BigDecimal.valueOf(avg).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
                            len=0;
                            sum=0.0;
                            if(v==1){
                                map.put("冬",avg);
                            }else if(v==4){
                                map.put("春",avg);
                            }else if(v==7){
                                map.put("夏",avg);
                            }else if(v==10){
                                map.put("秋",avg);
                            }
                        }
                    }
                }
            }
        }
        String body =beginTime+"~"+endTime+"年，"+stationName+"地区年平均日照时数为"+sum_avg_year+"小时，其中，"+sum_max_month+"月份日照时数最多，为"+sum_avg_max+"小时，" +
                sum_min_month+"月份最少，为"+sum_avg_min+"小时。冬季（12～次年2月）各月日照时数为"+map.get("冬")+"小时左右，夏季（6～8月）各月日照时数为"+map.get("夏")+"小时左右。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        int year=Integer.parseInt(endTime)-Integer.parseInt(beginTime)+1;
        String chartName="图5.9 "+stationName+"气象站各月累年平均日照时数（近"+year+"年）";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
