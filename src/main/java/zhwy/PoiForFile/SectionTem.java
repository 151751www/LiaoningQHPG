package zhwy.PoiForFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.service.impl.ChanPinServiceImpl;

import java.math.BigDecimal;

@Service()
public class SectionTem extends BaseSection {
    @Autowired
    ChanPinServiceImpl chanPinService;


    @Override
    public void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph) {
        String sectionTitle ="气温";
        poiUtils.setLevelTitle1(document,paragraph,sectionTitle);
    }

    @Override
    public void replaceBody(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) throws  Exception{
        JSONArray array=chanPinService.getAvgMathDate(stationType,beginTime,endTime,stationNum,"气温");
        JSONArray leiNianarr=chanPinService.getSumYrarDate(stationType,beginTime,endTime,stationNum);
        String tem_avg_year="";
        String tem_avg_year_max="";
        String tem_avg_year_min="";
        double tem_avg_min=0;
        double tem_avg_max=0.0;
        String tem_avg_min_month="";
        String tem_avg_max_month="";
        double tem_max_max=0.0;
        double tem_max_min=0.0;
        double tem_min_max=0.0;
        double tem_min_min=0.0;

        JSONObject temObjece;
        for (int i=0;i<leiNianarr.size();i++){
            temObjece=leiNianarr.getJSONObject(i);
            if(temObjece.getString("气象要素").startsWith("气温")){
                tem_avg_year=temObjece.getString("累年平均值");
            }else if(temObjece.getString("气象要素").startsWith("平均最高气温")){
                tem_avg_year_max=temObjece.getString("累年平均值");
            }else if(temObjece.getString("气象要素").startsWith("平均最低气温")){
                tem_avg_year_min=temObjece.getString("累年平均值");
            }
        }
        for (int i=0;i<array.size();i++){
            temObjece=array.getJSONObject(i);
            if(temObjece.getDouble("val")!=null&&temObjece.getDouble("val")>tem_avg_max){
                tem_avg_max=temObjece.getDouble("val");
                tem_avg_max_month=temObjece.getString("math");
            }else if(temObjece.getDouble("val")!=null&&temObjece.getDouble("val")<tem_avg_min){
                tem_avg_min=temObjece.getDouble("val");
                tem_avg_min_month=temObjece.getString("math");
            }

            if(temObjece.getDouble("vmax")!=null&&temObjece.getDouble("vmax")>tem_max_max){
                tem_max_max=temObjece.getDouble("vmax");
            }else if(temObjece.getDouble("vmax")!=null&&temObjece.getDouble("vmax")<tem_max_min){
                tem_max_min=temObjece.getDouble("vmax");
            }

            if(temObjece.getDouble("vmin")!=null&&temObjece.getDouble("vmin")>tem_min_max){
                tem_min_max=temObjece.getDouble("vmin");
            }else if(temObjece.getDouble("vmin")!=null&&temObjece.getDouble("vmin")<tem_min_min){
                tem_min_min=temObjece.getDouble("vmin");
            }
        }
        double avgminus=BigDecimal.valueOf(tem_avg_max-tem_avg_min).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  ;
        double maxminus=BigDecimal.valueOf(tem_max_max-tem_max_min).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  ;
        double minminus=BigDecimal.valueOf(tem_min_max-tem_min_min).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  ;
        String body =beginTime+"~"+endTime+"年，"+stationName+"地区年平均气温"+tem_avg_year+"℃，年平均最高气温为"+tem_avg_year_max+"℃，年平均最低气温为"+tem_avg_year_min+"℃。" +
                "气温存在明显的年内变化特征，"+tem_avg_min_month+"月气温最低，为-"+tem_avg_min+"℃，"+tem_avg_max_month+"月气温最高，为"+tem_avg_max+"℃。1月和7月平均气温温差为"+avgminus+"℃，" +
                "平均最高气温的温差为"+maxminus+"℃，平均最低气温温差为"+minminus+"℃。";
        poiUtils.setTextPro(paragraph,body);
    }


    @Override
    public void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName) {
        int year=Integer.parseInt(endTime)-Integer.parseInt(beginTime)+1;
        String chartName=stationName+"气象站各月累年平均气温、最高气温、最低气温（近"+year+"年）";
        poiUtils.setTableOrChartTitle(paragraph,chartName);
    }
}
