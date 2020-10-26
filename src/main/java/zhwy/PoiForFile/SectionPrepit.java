package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service()
public class SectionPrepit extends BaseSection {

    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="1.3 降水量";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{
        String pre_avg_year="";
        double pre_avg_max=0.0;
        double pre_max_spring=0.0;
        double pre_min_spring=0.0;
        double pre_min_summ=0.0;
        double pre_max_summ=0.0;
        double pre_max_autumn=0.0;
        double pre_min_autumn=0.0;
        double pre_min_winter=0.0;
        double pre_max_winter=0.0;
        double spring=0.0;
        double summ=0.0;
        double autumn=0.0;
        double winter=0.0;
        String season="";
        JSONObject temObjece;
        for (int i=0;i<leinianzhi.size();i++){
            temObjece=leinianzhi.getJSONObject(i);
            if(temObjece.getString("气象要素").startsWith("降水量")){
                pre_avg_year=temObjece.getString("累年平均值");
            }
        }
        for (int i=0;i<avgMonth.size();i++){
            temObjece=avgMonth.getJSONObject(i);
            if(temObjece.containsKey("val")){
                JSONArray valarr=temObjece.getJSONArray("val");
                for (int v=0;v<valarr.size();v++){
                    Object object=valarr.get(v);
                    if(object!=null){
                    if(v==3||v==4||v==5){//春
                        spring=spring+Double.parseDouble(object.toString());
                        if(pre_max_spring<Double.parseDouble(object.toString())){
                            pre_max_spring=Double.parseDouble(object.toString());
                        }
                        if(v==3){
                            pre_min_spring=Double.parseDouble(object.toString());
                        }else if(pre_min_spring>Double.parseDouble(object.toString())){
                            pre_min_spring=Double.parseDouble(object.toString());
                        }
                    }else if(v==6||v==7||v==8){//
                        summ=summ+Double.parseDouble(object.toString());
                        if(pre_max_summ<Double.parseDouble(object.toString())){
                            pre_max_summ=Double.parseDouble(object.toString());
                        }
                        if(v==6){
                            pre_min_summ=Double.parseDouble(object.toString());
                        }else if(pre_min_summ>Double.parseDouble(object.toString())){
                            pre_min_summ=Double.parseDouble(object.toString());
                        }
                    }else if(v==9||v==10||v==11){
                        autumn=autumn+Double.parseDouble(object.toString());
                        if(pre_max_autumn<Double.parseDouble(object.toString())){
                            pre_max_autumn=Double.parseDouble(object.toString());
                        }
                        if(v==9){
                            pre_min_autumn=Double.parseDouble(object.toString());
                        }else if(pre_min_autumn>Double.parseDouble(object.toString())){
                            pre_min_autumn=Double.parseDouble(object.toString());
                        }
                    }else if(v==12||v==1||v==2) {
                        winter=winter+Double.parseDouble(object.toString());
                        if(pre_max_winter<Double.parseDouble(object.toString())){
                            pre_max_winter=Double.parseDouble(object.toString());
                        }
                        if(v==1){
                            pre_min_winter=Double.parseDouble(object.toString());
                        }else if(pre_min_winter>Double.parseDouble(object.toString())){
                            pre_min_winter=Double.parseDouble(object.toString());
                        }
                    }
                    }
                }
            }
        }
        double[] pre_seaon=new double[]{spring,summ,autumn,winter};
        String[] seaon=new String[]{"春季","夏季","秋季","冬季"};
        double[][] pre_month = {{pre_max_spring,pre_min_spring},{pre_max_summ,pre_min_summ},{pre_max_autumn,pre_min_autumn},{pre_max_winter,pre_min_winter}};
        String[] month=new String[]{"3~5","6~8","9~11","12~2"};
        String fanwei="";
        String PreFanwei="";
        for (int i=0;i<pre_seaon.length;i++){
            if(pre_avg_max<pre_seaon[i]){
                pre_avg_max=pre_seaon[i];
                season=seaon[i];
                PreFanwei=pre_month[i][1]+"~"+pre_month[i][0];
                fanwei=month[i];
            }
        }
        pre_avg_max= BigDecimal.valueOf(pre_avg_max).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  ;

        String body =beginTime+"~"+endTime+"年，"+stationName+"地区平均年降水量为"+pre_avg_year+"mm，"+season+"（"+fanwei+"月）降水量较多" +
                "，为"+pre_avg_max+"mm，各月在"+PreFanwei+"mm之间。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        int year=Integer.parseInt(endTime)-Integer.parseInt(beginTime)+1;
        String chartName=stationName+"气象站各月累年平均降水量";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
