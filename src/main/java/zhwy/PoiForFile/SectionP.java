package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

@Service()
public class SectionP extends BaseSection {

    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="1.7 气压";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{

        double P_avg_min=0;
        String P_min_month="";
        double P_avg_max=0.0;
        String P_max_month="";
        String P_avg_year="";
        JSONObject PObject;
        for (int i=0;i<leinianzhi.size();i++){
            PObject=leinianzhi.getJSONObject(i);
            if(PObject.getString("气象要素").startsWith("气压")){
                P_avg_year=PObject.getString("累年平均值");
            }
        }
        for (int i=0;i<avgMonth.size();i++){
            PObject=avgMonth.getJSONObject(i);
            if(PObject.containsKey("val")){
                JSONArray valarr=PObject.getJSONArray("val");
                if(valarr.get(11)!=null){
                    P_avg_min=Double.parseDouble(valarr.get(11).toString());
                    P_min_month=12+"";
                    P_avg_max=Double.parseDouble(valarr.get(11).toString());
                    P_max_month=12+"";
                }
                for (int v=0;v<valarr.size()-1;v++){
                    Object object=valarr.get(v);
                    if(object!=null){
                        if(Double.parseDouble(object.toString())>P_avg_max){
                            P_avg_max=Double.parseDouble(object.toString());
                            P_max_month=(v+1)+"";
                        }else if(Double.parseDouble(object.toString())<P_avg_min){
                            P_avg_min=Double.parseDouble(object.toString());
                            P_min_month=(v+1)+"";
                        }
                    }
                }
            }
        }
        String body =beginTime+"~"+endTime+"年，"+stationName+"地区年平均气压为"+P_avg_year+"hPa。冬季气压高、夏季气压低，"+P_max_month+"月气压最高（"+P_avg_max+"hPa），"+P_min_month+"月最低（"+P_avg_min+"hPa）。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        String chartName="图5.11 "+stationName+"气象站各月累年平均气压";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
