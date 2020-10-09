package zhwy.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.ChanPinDao;
import zhwy.util.GeneralDaoImpl;
import zhwy.util.StrUtil;
import zhwy.util.Wind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChanPinDaoImpl implements ChanPinDao {
    @Autowired
    private GeneralDaoImpl generalDao;
    @Autowired
    private Wind wind;

    public JSONArray getSumYrarDate(String stationType, String beginTime, String endTime, String stationNum)throws  Exception{
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject;
        String Yeartable="" ;
        if(stationType.equals("国家站")){
            Yeartable="surf_aws_year_data";
        }
        String [] obsvVal={"tem","tem_max","tem_min","prs","pre","win_s","win_f","vap","rhu","gst","gst_80","gst_160","gst_320"};
        String [] obsvName={"气温（℃）","平均最高气温（℃）","平均最低气温（℃）","气压（hPa）","降水量（mm）","风速（m/s）","主导风向","水汽压（hPa）","相对湿度（%）","地表温度（℃）","0.8m地温（℃）","1.6m地温（℃）","3.2m地温（℃）"};
        String [] fangwei={"NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW","N"};
        StringBuffer buffer=new StringBuffer();
        buffer.append("select cast(AVG(surf.prs_avg) as decimal(5, 1))as prs,cast(AVG(surf.tem_avg) as decimal(5, 1))as tem,cast(avg(surf.tem_max) as decimal(5, 1))as tem_max,cast(AVG(tem_min) as decimal(5, 1))as tem_min," +
               "cast(AVG(surf.pre_year) as decimal(5, 1))as pre,cast(AVG(surf.win_s_10_year) as decimal(5, 1))as win_s,"+
                "cast(AVG(surf.vap_avg) as decimal(5, 1))as vap,cast(AVG(surf.rhu_avg) as decimal(5, 1))as rhu,cast(AVG(surf.gst_avg) as decimal(5, 1))as gst," +
                "cast(AVG(surf.gst_avg_80cm) as decimal(5, 1))as gst_80,cast(AVG(surf.gst_avg_160cm) as decimal(5, 1))as gst_160,cast(AVG(surf.gst_avg_320cm) as decimal(5, 1))as gst_320 " );
                for(int i=0;i<fangwei.length;i++){
                    buffer.append(",cast(AVG(surf.win_"+fangwei[i]+"_freq) as decimal(5, 1)) as "+fangwei[i]);
        }
        buffer.append(" from ");
        buffer.append(Yeartable);
        buffer.append(" surf where surf.observe_date>='");
        buffer.append(beginTime);
        buffer.append("'and surf.observe_date<='");
        buffer.append(endTime);
        buffer.append("' and surf.station_num='");
        buffer.append(stationNum);
        buffer.append("' ");
        Map<String,Object> map=generalDao.getDataMap(buffer.toString());
        for (int i=0;i<obsvVal.length;i++){
            jsonObject=new JSONObject(true);
            jsonObject.put("气象要素",obsvName[i]);
            String value="";
            if(obsvVal[i].equals("win_f")){
                double win_f=0.0;
                double win_ff=0.0;
                for (int f=0;f<fangwei.length;f++){
                    if(map.get(fangwei[f])!=null){
                        if(f==0){
                            win_f=0.0;
                        }else{
                            win_f=Double.parseDouble(String.valueOf(map.get(fangwei[f-1])));
                        }
                        win_ff=Double.parseDouble(String.valueOf(map.get(fangwei[f])));
                        if(win_ff>win_f){
                            value=fangwei[f];
                        }
                    }
                }
                jsonObject.put("累年平均值",value);
            }else{
                for (Map.Entry<String,Object> entry:map.entrySet()){
                    if(obsvVal[i].equals(entry.getKey())){
                        if(null==entry.getValue()){
                            jsonObject.put("累年平均值","-");
                        }else{
                            jsonObject.put("累年平均值",entry.getValue());
                        }
                        break;
                    }
                }
            }
            jsonArray.add(i,jsonObject);
        }
        return jsonArray;
    }

    public JSONArray getJiZhiDate(String stationType, String beginTime, String endTime, String stationNum)throws  Exception{
        StrUtil strUtil=new StrUtil();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject;
        String Yeartable="";
        if(stationType.equals("国家站")){
            Yeartable="surf_aws_year_data";
        }
        String [] obsvVal={"tem_max","tem_min","rhu_min","PRE_Max_Day","EVP","win_s_max_year","FRS_Depth_Max","Snow_Depth_Max"};
        String [] obsvtime={"TEM_Max_otime","TEM_Min_otime","RHU_Min_Otime","PRE_Max_Mon_Otime","observe_date","win_s_max_year_otime","FRS_Depth_Max_Otime","Snow_Depth_Max_otime"};
        String [] obsvName={"极端最高气温","极端最低气温","日最低相对湿度","日最大降水量","年最大蒸发量","最大风速及其风向","最大冻土深度","最大积雪深度"};
        String [] danwei={"℃","℃","%","mm","mm","m/s","cm","cm"};
        StringBuffer buffer;
        List<Map<String,Object>> map;
        StringBuffer fbuffer=new StringBuffer();
        //CONVERT(varchar(100), data."+obsvtime[i]+", 102)
        for (int i=0;i<obsvVal.length;i++){
            map=new ArrayList<Map<String,Object>>();
            jsonObject=new JSONObject(true);
            buffer=new StringBuffer();
            if(obsvVal[i].equals("win_s_max_year")){
                buffer.append("select data."+obsvVal[i]+" as value,  "+obsvtime[i]+" as observe_time, observe_date as year,win_f_s_max_year as win_d_s_max_c  from  " );
            }else{
                buffer.append("select data."+obsvVal[i]+" as value,  "+obsvtime[i]+" as observe_time, observe_date as year from  " );
            }

            buffer.append(Yeartable);
            buffer.append(" data where DATa."+obsvVal[i]+"=(select max(surf."+obsvVal[i]+") from ");
            buffer.append(Yeartable);
            buffer.append(" surf where surf.observe_date>='");
            buffer.append(beginTime);
            buffer.append("'and surf.observe_date<='");
            buffer.append(endTime);
            buffer.append("' and surf.station_num='");
            buffer.append(stationNum);
            buffer.append("') and data.observe_date>='");
            buffer.append(beginTime);
            buffer.append("'and data.observe_date<='");
            buffer.append(endTime);
            buffer.append("' and data.station_num='");
            buffer.append(stationNum);
            buffer.append("'");
            map=generalDao.getDataList(buffer.toString());

            jsonObject.put("气象要素",obsvName[i]);
            String time="";
            String value="";
            if(map.size()>0){
                time=strUtil.NullToSpace(String.valueOf(map.get(0).get("observe_time")));
                if(obsvVal[i].equals("win_s_max_year")){
                    String fengxiang=wind.ConvertWindDirNumberToText(Double.parseDouble(String.valueOf(map.get(0).get("value"))),Double.parseDouble(String.valueOf(map.get(0).get("win_d_s_max_c"))),16,false);
                    value=map.get(0).get("value")+danwei[i]+","+fengxiang;
                }else{
                    value=map.get(0).get("value")+danwei[i];
                }
                String year=String.valueOf(map.get(0).get("year")).substring(0,4);
                if(time.length()==4&&!obsvVal[i].equals("EVP")){
                    time=year+"."+time.substring(0,2)+"."+time.substring(2);
                }else if(time.length()==2){
                    time=year+"."+time.substring(0,1)+"."+time.substring(1);
                } else if(time.length()==3){
                    int y=Integer.valueOf(time.substring(0,1));
                    if(y>1){
                        time=year+"."+time.substring(0,1)+"."+time.substring(1);
                    }else{
                        time=year+"."+time.substring(0,1)+"."+time.substring(1);
                    }
                }else if(obsvVal[i].equals("EVP")){
                    time=year;
                }
            }
            jsonObject.put("数值",value);
            jsonObject.put("出现时间",time);
            jsonArray.add(i,jsonObject);
        }
        return  jsonArray;
    }

    public JSONArray getAvgMathDate( String beginTime, String endTime, String stationNum,String obsv,String[] key,String tableName)throws  Exception{
        JSONArray jsonArray;
        StringBuffer buffer=new StringBuffer();
        buffer.append("select DatePart (Month,observe_date) as math " );
        buffer.append(obsv);
        buffer.append(" from ");
        buffer.append(tableName);
        buffer.append(" surf where surf.observe_date>='");
        buffer.append(beginTime);
        buffer.append("'and surf.observe_date<='");
        buffer.append(endTime);
        buffer.append("' and surf.station_num='");
        buffer.append(stationNum);
        buffer.append("' group by DatePart (Month,observe_date) ORDER BY DatePart(MONTH, observe_date)");
        jsonArray= generalDao.getDataBySql(buffer.toString(),key);
        return  jsonArray;
    }

}
