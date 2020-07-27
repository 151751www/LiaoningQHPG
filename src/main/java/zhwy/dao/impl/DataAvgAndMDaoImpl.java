package zhwy.dao.impl;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.util.Common;
import zhwy.util.GeneralDaoImpl;

import java.util.Map;

@Component
public class DataAvgAndMDaoImpl {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;
    @Autowired
    private Common common;

    public String DataForAvg(String stationType,String dataType, String key,String value, String StartTime,String EndTime, String city,String cnty){
        String sql="";
        String result="";
        Map<String,String> obsvMap;
        if("时".equals(dataType)){
            obsvMap=common.itemH;
        }else if("日".equals(dataType)){
            obsvMap=common.itemD;
        }else if("月".equals(dataType)){
            obsvMap=common.itemM;
        }else if("年".equals(dataType)){
            obsvMap=common.itemY;
        }
        String [] keys={"站号","站名",value+"的平均值"};
        JSONArray array = new JSONArray();
         String dataTableName = "";
         String stationTableName="";
         String  select="";
         if(cnty!=null&&!cnty.equals("")&&!cnty.equals("县")&&!cnty.equals("全部")){
             select=" and meto.city='"+city+"' and meto.cnty='"+cnty+"' ";
         }else if(city!=null&&!city.equals("")&&!city.equals("市")&&!city.equals("全部")){
             select=" and meto.city='"+city+"' ";
         }
         if(stationType.equals("国家站")){
             if(dataType.equals("时")){
                 dataTableName="surf_aws_hour_data";
             }else if(dataType.equals("日")){
                 dataTableName="surf_aws_day_data";
             }else if(dataType.equals("月")){
                 dataTableName="surf_aws_month_data";
             }else  if(dataType.equals("年")){
                 dataTableName="surf_aws_year_data";
             }
             stationTableName="meto_surf_aws_info";
         }else if(stationType.equals("区域站")){

         }
         if(!dataTableName.equals("")&&!stationTableName.equals("")&&result.equals("")){
             sql="SELECT\n" +
                     "\tmeto.station_num AS 站号,\n" +
                     "\tmeto.station_name AS 站名,\n" +
                     "\tRound(AVG (surf."+key+"),2) AS \n" +value+"的平均值"+
                     "\tFROM \n" +
                     stationTableName+"\t meto,\n" +
                     dataTableName+"\t surf\n" +
                     "\tWHERE\n" +
                     "\tmeto.station_num = surf.station_num\n" +
                     "\tAND surf.observe_date BETWEEN CONVERT (datetime, '"+StartTime+"')\n" +
                     "\tAND CONVERT (datetime, '"+EndTime+"') "+select+
                     "\tGROUP BY\n" +
                     "\tmeto.station_num,\n" +
                     "\tmeto.station_name\n" +
                     "\tORDER BY\n" +
                     "\tmeto.station_num" ;
             try {
                 result=generalDao.getDataBySql(sql,keys).toString();
             }catch (Exception e){
                 logger.error("DataAvgAndMDaoImpl 文件平均数查询失败"+e.getMessage());
                 result="DataAvgAndMDaoImpl 文件  平均数查询失败"+e.getMessage();
             }
         }else{
             if(stationTableName.equals("")){
                 result="台站类型不匹配";
             }else if(dataTableName.equals("")){
                 result="数据类型不匹配";
             }

         }
        return result;

    }

    public String DataForM(String stationType, String dataType, String key,String value, String startTime, String endTime, String city, String cnty) {
        String sql="";
        String result="";
        String [] keys={"站号","站名",value+"的极小值",value+"的极大值"};
        String dataTableName = "";
        String stationTableName="";
        String  select="";
        if(cnty!=null&&!cnty.equals("")&&!cnty.equals("县")&&!cnty.equals("全部")){
            select=" and meto.city='"+city+"' and meto.cnty='"+cnty+"' ";
        }else if(city!=null&&!city.equals("")&&!city.equals("市")&&!city.equals("全部")){
            select=" and meto.city='"+city+"' ";
        }
        if(stationType.equals("国家站")){
            if(dataType.equals("时")){
                dataTableName="surf_aws_hour_data";
            }else if(dataType.equals("日")){
                dataTableName="surf_aws_day_data";
            }else if(dataType.equals("月")){
                dataTableName="surf_aws_month_data";
            }else  if(dataType.equals("年")){
                dataTableName="surf_aws_year_data";
            }
            stationTableName="meto_surf_aws_info";
        }else if(stationType.equals("区域站")){

        }
        if(!dataTableName.equals("")&&!stationTableName.equals("")&&result.equals("")){
            sql="SELECT\n" +
                    "\tmeto.station_num AS 站号,\n" +
                    "\tmeto.station_name AS 站名,\n" +
                    "\tRound(MIN (surf."+key+"),2) AS \n" +value+"的极小值," +
                    " Round(MAX (surf."+key+"),2) AS  "+value+"的极大值 "+
                    " FROM \n" +
                    stationTableName+"\t meto,\n" +
                    dataTableName+"\t surf\n" +
                    " WHERE\n" +
                    "\tmeto.station_num = surf.station_num\n" +
                    " AND surf.observe_date BETWEEN CONVERT (datetime, '"+startTime+"')\n" +
                    " AND CONVERT (datetime, '"+endTime+"') "+select+
                    "GROUP BY\n" +
                    "\tmeto.station_num,\n" +
                    "\tmeto.station_name\n" +
                    " ORDER BY\n" +
                    "\tmeto.station_num" ;
            try {
                result=generalDao.getDataBySql(sql,keys).toString();
            }catch (Exception e){
                logger.error("DataAvgAndMDaoImpl 文件极值数据查询失败"+e.getMessage());
                result="DataAvgAndMDaoImpl 文件  极值数据查询失败"+e.getMessage();
            }
        }else{
            if(stationTableName.equals("")){
                result="台站类型不匹配";
            }else if(dataTableName.equals("")){
                result="数据类型不匹配";
            }

        }
        return result;
    }

    public String getDataForOrig(String stationType, String dataType, String key, String value, String startTime, String endTime, String city, String cnty, Integer page, Integer pagesize) {
        String sql="";
        String result="";
        String [] keys={"站号","站名","日期",value};
        String dataTableName = "";
        String stationTableName="";
        String  select="";
        if(cnty!=null&&!cnty.equals("")&&!cnty.equals("县")&&!cnty.equals("全部")){
            select=" and meto.city='"+city+"' and meto.cnty='"+cnty+"' ";
        }else if(city!=null&&!city.equals("")&&!city.equals("市")&&!city.equals("全部")){
            select=" and meto.city='"+city+"' ";
        }
        if(stationType.equals("国家站")){
            if(dataType.equals("时")){
                dataTableName="surf_aws_hour_data";
            }else if(dataType.equals("日")){
                dataTableName="surf_aws_day_data";
            }else if(dataType.equals("月")){
                dataTableName="surf_aws_month_data";
            }else  if(dataType.equals("年")){
                dataTableName="surf_aws_year_data";
            }
            stationTableName="meto_surf_aws_info";
        }else if(stationType.equals("区域站")){

        }
        if(!dataTableName.equals("")&&!stationTableName.equals("")&&result.equals("")){
            sql="SELECT\tTOP "+pagesize+" *FROM(\n" +
                    "\t\tSELECT\n" +
                    "\t\t\trow_number () OVER (\n" +
                    "\t\t\t\tORDER BY\tmeto.station_num ASC,\tsurf.observe_date ASC\n" +
                    "\t\t\t) AS rownumber,\n" +
                    "\t\t\tmeto.station_num AS 站号,\tmeto.station_name AS 站名,\n" +
                    "\t\t\tCONVERT (\tVARCHAR (100),\tsurf.observe_date,\t20) AS 日期,\n" +
                    "\t\t\tRound(surf."+key+", 2) AS " +value+
                    "\t\tFROM\n" +
                    "\t\t\t"+stationTableName+" meto,\t"+dataTableName+" surf\n" +
                    "\t\tWHERE\n" +
                    "\t\t\tmeto.station_num = surf.station_num\n" +select+
                    "\t\tAND surf.observe_date BETWEEN CONVERT (datetime, '"+startTime+"')\n" +
                    "\t\tAND CONVERT (datetime, '"+endTime+"')\n" +
                    "\t) temp_row\n" +
                    "WHERE\n" +
                    "\trownumber > (("+page+" - 1) * "+pagesize+")" ;
            try {
                result=generalDao.getDataBySql(sql,keys).toString();
            }catch (Exception e){
                logger.error("DataAvgAndMDaoImpl 文件原始数据查询失败"+e.getMessage());
                result="DataAvgAndMDaoImpl 文件  原始数据查询失败"+e.getMessage();
            }
        }else{
            if(stationTableName.equals("")){
                result="台站类型不匹配";
            }else if(dataTableName.equals("")){
                result="数据类型不匹配";
            }

        }
        return result;
    }
}
