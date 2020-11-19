package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

@Service()
public class SectionHumi extends BaseSection {

    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="1.6 相对湿度";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{

        double humi_avg_min=0;
        String humi_min_month="";
        double humi_avg_max=0.0;
        String humi_max_month="";
        String humi_avg_year="";
        JSONObject humiObject;
        for (int i=0;i<leinianzhi.size();i++){
            humiObject=leinianzhi.getJSONObject(i);
            if(humiObject.getString("气象要素").startsWith("相对湿度")){
                humi_avg_year=humiObject.getString("累年平均值");
            }
        }
        for (int i=0;i<avgMonth.size();i++){
            humiObject=avgMonth.getJSONObject(i);
            if(humiObject.containsKey("val")){
                JSONArray valarr=humiObject.getJSONArray("val");
                if(valarr.get(11)!=null){
                    humi_avg_min=Double.parseDouble(valarr.get(11).toString());
                    humi_min_month=12+"";
                    humi_avg_max=Double.parseDouble(valarr.get(11).toString());
                    humi_max_month=12+"";
                }
                for (int v=0;v<valarr.size()-1;v++){
                    Object object=valarr.get(v);
                    if(object!=null){
                        if(Double.parseDouble(object.toString())>humi_avg_max){
                            humi_avg_max=Double.parseDouble(object.toString());
                            humi_max_month=(v+1)+"";
                        }else if(Double.parseDouble(object.toString())<humi_avg_min){
                            humi_avg_min=Double.parseDouble(object.toString());
                            humi_min_month=(v+1)+"";
                        }
                    }
                }
            }
        }
        String body =beginTime+"~"+endTime+"年，"+stationName+"地区年平均相对湿度为"+humi_avg_year+"%，其中，"+humi_max_month+"月最大，为"+humi_avg_max+"%，"+humi_min_month+"月最小，为"+humi_avg_min+"%。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        String chartName="图5.10 "+stationName+"气象站各月累年平均相对湿度";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
