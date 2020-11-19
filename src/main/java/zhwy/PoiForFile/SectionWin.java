package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service()
public class SectionWin extends BaseSection {

    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="1.4 风";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{
        String body="";
        if(keyInParaText.endsWith("body")){
            body="(1) 风速";
        }else if(keyInParaText.endsWith("body2")){
           body=getbody2( leinianzhi, avgMonth, beginTime, endTime ,stationName);
        }else if(keyInParaText.endsWith("body3")){
            body="(2) 风向";
        }else if(keyInParaText.endsWith("body4")){
           body=getbody4(leinianzhi, avgMonth, beginTime, endTime ,stationName);
        }
        poiUtils.setTextPro(paragraph,body);
    }

    public String getbody2(JSONArray leinianzhi,JSONArray avgMonth,String beginTime,String endTime,String stationName){
        double win_s_min=0;
        double win_s_max=0.0;
        String win_avg_year="";
        String win_avg_min_month="";
        String win_avg_max_month="";
        JSONObject temObjece;
        for (int i=0;i<leinianzhi.size();i++){
            temObjece=leinianzhi.getJSONObject(i);
            if(temObjece.getString("气象要素").startsWith("风速")){
                win_avg_year=temObjece.getString("累年平均值");
            }
        }
        for (int i=0;i<avgMonth.size();i++){
            temObjece=avgMonth.getJSONObject(i);
            if(temObjece.containsKey("val")){
                JSONArray valarr=temObjece.getJSONArray("val");
                for (int v=0;v<valarr.size();v++){
                    Object object=valarr.get(v);
                    if(object!=null){
                        if(v==0){
                            win_s_min=Double.parseDouble(object.toString());
                            win_s_max=Double.parseDouble(object.toString());
                        }
                        if(Double.parseDouble(object.toString())>win_s_max){
                            win_s_max=Double.parseDouble(object.toString());
                            win_avg_max_month=String.valueOf(v+1);
                        }else if(Double.parseDouble(object.toString())<win_s_min){
                            win_s_min=Double.parseDouble(object.toString());
                            win_avg_min_month=String.valueOf(v+1);
                        }
                    }
                }
            }
        }
       String  body =beginTime+"~"+endTime+"年，"+stationName+"地区年平均风速为"+win_avg_year+"m/s。其中，"+win_avg_max_month+"月最大，为"+win_s_max+"m/s；，"+win_avg_min_month+"月风速最小，为"+win_s_min+"m/s。";
        return  body;
    }
    public String getbody4(JSONArray leinianzhi,JSONArray avgMonth,String beginTime,String endTime,String stationName){
        Map<String,String> winDir=new HashMap<>();
        String[] _arrWindDirChinese16 = new String[] { "北", "北东北", "东北", "东东北", "东", "东东南", "东南", "南东南", "南", "南西南", "西南", "西西南", "西", "西西北", "西北", "北西北" };
        String[] _arrWindDirEnglish16 = new String[] { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW" };
        for (int i=0;i<_arrWindDirEnglish16.length;i++){
            winDir.put(_arrWindDirEnglish16[i],_arrWindDirChinese16[i]);
        }
        double year_max=0.0;
        String year_fangwei_max="";
        String year_fangwei_max_ch="";
        double year_sen=0.0;
        String year_fangwei_sen="";
        String year_fangwei_sen_ch="";
        if(leinianzhi.size()>0){
            JSONObject object= leinianzhi.getJSONObject(0);
            Iterator iter = object.entrySet().iterator();
            Map<String,Double> map=new HashMap<>();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if(entry.getValue()!=null){
                    map.put(entry.getKey().toString(),Double.parseDouble(entry.getValue().toString()));
                }else {
                    map.put(entry.getKey().toString(),null);
                }
            }
            List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map.entrySet());
            //升序排列逻辑
            Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            year_max=list.get(0).getValue();
            year_fangwei_max=list.get(0).getKey();
            for(Map.Entry<String,Double> mapping:list){
                if(mapping.getValue()!=year_max){
                    year_sen=mapping.getValue();
                    year_fangwei_sen=mapping.getKey();
                    break;
                }
            }
        }
        if(year_fangwei_max.length()==3){
            year_fangwei_max_ch=winDir.get(year_fangwei_max).substring(1)+"偏"+winDir.get(year_fangwei_max).substring(0,1)+"风";
        }else{
            year_fangwei_max_ch=winDir.get(year_fangwei_max)+"风";
        }
        if(year_fangwei_sen.length()==3){
            year_fangwei_sen_ch=winDir.get(year_fangwei_sen).substring(1)+"偏"+winDir.get(year_fangwei_sen).substring(0,1)+"风";
        }else{
            year_fangwei_sen_ch=winDir.get(year_fangwei_sen)+"风";
        }
        JSONObject jsonObject=null;
        JSONObject jijieObject=new JSONObject();
        int[] montg={1,4,7,10};

        for (int i=0;i<avgMonth.size();i++){
            jsonObject=avgMonth.getJSONObject(i);
            for(Map.Entry<String, Object> map:jsonObject.entrySet()){
                String fangwei=map.getKey().toString();
                double avg=0.0;
                JSONArray jijiearr=new JSONArray();
                JSONArray fangweidate = JSONArray.parseArray(map.getValue().toString());
                if(fangweidate.size()==12){
                    double sum=0.0;
                    int length=0;
                    for(int l=0;l<fangweidate.size()-1;l++){
                        if(l==0&&fangweidate.get(11)!=null){
                            sum=Double.parseDouble(fangweidate.get(11).toString());
                            length=1;
                        }
                        if(fangweidate.get(l)!=null){
                            sum+=Double.parseDouble(fangweidate.get(l).toString());
                            length++;
                        }
                        for (int m=0;m<montg.length;m++){
                            if(l==montg[m]){
                                avg=sum/length;
                                avg=new BigDecimal(avg).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();;
                                jijiearr.add(m,avg);
                                sum=0.0;
                                length=0;
                            }
                        }
                    }
                }
                jijieObject.put(fangwei,jijiearr);
            }
        }
        double dong=0.0;double chun=0.0;double xia=0.0;double qiu=0.0;
        String dong_S="";String chun_S="";String xia_S="";String qiu_S="";
        for (Map.Entry<String, Object> map:jijieObject.entrySet()){
            JSONArray fangweidate = JSONArray.parseArray(map.getValue().toString());
            if(Double.parseDouble(fangweidate.get(0).toString())>dong){
                dong=Double.parseDouble(fangweidate.get(0).toString());
                dong_S=map.getKey();
            }
            if(Double.parseDouble(fangweidate.get(1).toString())>chun){
                chun=Double.parseDouble(fangweidate.get(1).toString());
                chun_S=map.getKey();
            }
            if(Double.parseDouble(fangweidate.get(2).toString())>xia){
                xia=Double.parseDouble(fangweidate.get(2).toString());
                xia_S=map.getKey();
            }
            if(Double.parseDouble(fangweidate.get(3).toString())>qiu){
                qiu=Double.parseDouble(fangweidate.get(3).toString());
                qiu_S=map.getKey();
            }
        }
        String [] arr={dong_S,chun_S,xia_S,qiu_S};
        String [] arr_S=new String[4];
        for (int i=0;i<arr.length;i++){
            if(arr[i].length()==3){
                arr_S[i]=winDir.get(arr[i]).substring(1)+"偏"+winDir.get(arr[i]).substring(0,1)+"风";
            }else{
                arr_S[i]=winDir.get(arr[i])+"风";
            }
        }
       String  body=beginTime+"~"+endTime+"年，"+stationName+"地区主导风向为"+year_fangwei_max_ch+"（"+year_fangwei_max+"），其次为"+year_fangwei_sen_ch+"（"+year_fangwei_sen+"），" +
                "风向频率分别为"+year_max+"%和"+year_sen+"%。" +
                "从季节看，"+stationName+"地区春季以"+arr_S[1]+"（"+arr[1]+"）为主导风向，出现频率为"+chun+"%，" +
                "夏季也以"+arr_S[2]+"（"+arr[2]+"）为主导风向，出现频率为"+xia+"%，" +
                "秋季仍以"+arr_S[3]+"（"+arr[3]+"）为主导风向，出现频率为"+qiu+"%，" +
                "冬季以"+arr_S[0]+"（"+arr[0]+"）为主导风向，出现频率为"+dong+"%。";
        return  body;
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        int year=Integer.parseInt(endTime)-Integer.parseInt(beginTime)+1;
        String chartName="";
        if(keyInParaText.endsWith("chartName1")){
            chartName=stationName+"气象站累年各月平均风速";
        }else if(keyInParaText.endsWith("chartName")){
            chartName=stationName+"近"+year+"年的年、季风向频率";
        }
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
