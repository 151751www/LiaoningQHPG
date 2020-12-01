package zhwy.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.DataAvgAndMDao;
import zhwy.util.Common;
import zhwy.util.GeneralDaoImpl;
import zhwy.util.StrUtil;

import java.math.BigDecimal;


@Component
public class DataAvgAndMDaoImpl implements DataAvgAndMDao {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;
    @Autowired
    private Common common;

    String [] obsv={"气温","气压","水汽压","相对湿度","能见度","降水量","蒸发","积雪深度","风速","温度","地温","地面温度","界值","雪压"};
    String [] danwei={"℃","hPa","hPa","%","m","mm","mm","cm","m/s","℃","℃","℃","cm","kg/m²"};

    public String DataForAvg(String stationType,String dataType, String key,String value, String StartTime,String EndTime, String city,String stationNum,String suanfaType,String cnty){

        String sql= "";
        String result="";
        String [] keys={"站号","站名","时间","经度","纬度",value};
        StringBuilder  sqlData=new StringBuilder();
        String dataTableName = "";
        String stationTableName="";
        String  select="";
        int dnum=0;
        JSONArray total=new JSONArray();
        if(stationType.equals("国家站")){
            if(dataType.equals("时")){
                dataTableName="surf_aws_hour_data";
                dnum=14;
            }else if(dataType.equals("日")){
                dataTableName="surf_aws_day_data";
                dnum=11;
            }else if(dataType.equals("月")){
                dataTableName="surf_aws_month_data";
                dnum=8;
            }else  if(dataType.equals("年")){
                dataTableName="surf_aws_year_data";
                dnum=5;
            }
            stationTableName="meto_surf_aws_info";
        }else if(stationType.equals("区域站")){
            if(dataType.equals("时")){
                dataTableName="surf_reg_hour_data";
                dnum=14;
            }else if(dataType.equals("日")){
                dataTableName="surf_reg_day_data";
                dnum=11;
            }else if(dataType.equals("月")){
                dataTableName="surf_reg_month_data";
                dnum=8;
            }else  if(dataType.equals("年")){
                dataTableName="surf_reg_year_data";
                dnum=5;
            }
            stationTableName="meto_surf_reg_info";
        }
        if(value.indexOf("风速")>0){
            if(suanfaType.equals("极大值")||suanfaType.equals("极小值")){
                String wind=value+"的风向";
                String keywind=key.replace("_s_","_d_");
                if(key.equals("win_s_max")){
                    keywind="win_d_s_max";
                }
                keys= new String[]{"站号", "经度","纬度","站名", value+"的"+suanfaType,wind,value+"的"+suanfaType + "出现时间"};
                sqlData.append("select station_num as 站号,station_name  as 站名,lon as 经度,lat as 纬度,SUBSTRING(CONVERT (varchar(100),observe_date,120),0,"+dnum+") as  '"+value+"的"+suanfaType+"出现时间',"+key+" as '"+value+"的"+suanfaType+"',"+keywind+" as '"+wind+"'");
            }else if(suanfaType.equals("原始值")){
                keys= new String[]{"站号", "经度","纬度","站名", "时间",value ,value.replace("风速","风向")};
                sqlData.append("SELECT  meto.station_num AS 站号,  meto.station_name AS 站名,SUBSTRING(CONVERT (varchar(100),surf.observe_date,120),0,"+dnum+") as 时间 ,meto.lon as 经度,meto.lat as 纬度,surf."+key+" AS '"+value+"',surf."+key.replace("_s_","_d_")+" as '"+value.replace("风速","风向")+"' " );
            }else if(suanfaType.equals("平均值")){
                keys= new String[]{"站号", "经度","纬度","站名", value+"的平均值"};
                sqlData.append("SELECT  meto.station_num AS 站号,  meto.station_name AS 站名,meto.lon as 经度,meto.lat as 纬度,Convert(decimal(18,1),avg(surf."+key+")) AS '"+value+"的平均值' " );
            }
        }else{
            if(suanfaType.equals("平均值")){
                keys= new String[]{"站号","经度","纬度", "站名", value+"的平均值"};
                sqlData.append("SELECT  meto.station_num AS 站号,  meto.station_name AS 站名,meto.lon as 经度,meto.lat as 纬度,Convert(decimal(18,1),avg(surf."+key+")) AS  '"+ value+"的平均值'");
            }else if(suanfaType.equals("原始值")){
                sqlData.append("SELECT  meto.station_num AS 站号,  meto.station_name AS 站名,meto.lon as 经度,meto.lat as 纬度,SUBSTRING(CONVERT (varchar(100),surf.observe_date,120),0,"+dnum+") as 时间,surf."+key+" AS '"+value+"'");
            }else if(suanfaType.equals("极大值")||suanfaType.equals("极小值")){
                keys= new String[]{"站号", "站名","经度","纬度", value +"的"+ suanfaType,value +"的"+ suanfaType+"出现时间"};
                sqlData.append("select station_num as 站号,station_name  as 站名,lon as 经度,lat as 纬度,SUBSTRING(CONVERT (varchar(100),observe_date,120),0,"+dnum+") as  '"+value +"的"+ suanfaType+"出现时间',"+key+" as '"+value +"的"+ suanfaType+"'");
            }
        }

          if(city!=null&&!city.equals("")&&!city.equals("全部")){
             select+=" and meto.city='"+city+"' ";
         }
          if(cnty!=null&&!cnty.equals("")&&!cnty.equals("全部")){
             select+=" and meto.cnty='"+cnty+"' ";
         }
          if(stationNum!=null&&!stationNum.equals("")&&!stationNum.equals("全部")){
            select+=" and meto.station_num='"+stationNum+"' ";
        }

         if(!dataTableName.equals("")&&!stationTableName.equals("")&&result.equals("")){
             if(suanfaType.equals("平均值")||suanfaType.equals("原始值")){
                         sqlData.append( "  FROM  " );
                         sqlData.append( stationTableName+"   meto left join " );
                         sqlData.append( dataTableName+"   surf "  );
                         sqlData.append( "  on " );
                         sqlData.append( "  meto.station_num = surf.station_num "  );
                         sqlData.append( "  where  surf.observe_date BETWEEN CONVERT (datetime, '"+StartTime+"',20) " );
                         sqlData.append( "  AND CONVERT (datetime, '"+EndTime+"',20) "+select );
                         if(suanfaType.equals("平均值")){
                             sqlData.append( "  GROUP BY "  );
                             sqlData.append( "  meto.station_num, "  );
                             sqlData.append( "  meto.station_name, "  );
                             sqlData.append( "  meto.lon, "  );
                             sqlData.append( "  meto.lat "  );
                         }
                         sqlData.append( "  ORDER BY "  );
                         sqlData.append(  "  meto.station_num asc "  );
                 sql=sqlData.toString();
             }else if(suanfaType.equals("极大值")||suanfaType.equals("极小值")){
                 String order=" desc ";
                 if(suanfaType.equals("极小值")){
                     order=" asc ";
                 }
                 String keywind="";
                 if(value.indexOf("风速")!=-1){
                     if(key.equals("win_s_max")){
                         keywind=",win_d_s_max";
                     }else{
                         keywind=","+key.replace("_s_","_d_");
                     }
                 }
                 sqlData.append(" from ");
                 sqlData.append( " (SELECT  meto.station_num,meto.station_name,meto.lon,meto.lat,observe_date, ");
                 sqlData.append(key +keywind);
                 sqlData.append(", row_number () OVER ( " );
                 sqlData.append( " partition BY surf.station_num " );
                 sqlData.append( " ORDER BY " );
                 sqlData.append(   key+order );
                 sqlData.append( " ) rn  FROM " );
                 sqlData.append(dataTableName+" surf  LEFT JOIN "+stationTableName+" meto ON meto.station_num = surf.station_num" );
                 sqlData.append(" WHERE  " +key+" is not null and ");
                 sqlData.append(" observe_date BETWEEN CONVERT (datetime, '"+StartTime+"',20) " );
                 sqlData.append( "  AND CONVERT (datetime, '"+EndTime+"',20)"+select);
                 sqlData.append(" ) t " );
                 sqlData.append( "WHERE " );
                 sqlData.append( " t.rn = 1");
                 sqlData.append("  order by station_num asc,observe_date asc ");
                 sql=sqlData.toString();
             }

             try {
                 JSONArray rearr=generalDao.getDataBySql(sql,keys);
                 String odanwei="";
                for ( int i=0;i<obsv.length;i++){
                    if(value.indexOf(obsv[i])!=-1){
                        odanwei=danwei[i];
                        continue;
                    }
                }
                 total.add(0,odanwei);
                 total.add(1,rearr);
                 result=total.toJSONString();
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
         if(total.size()==0){
             result=StrUtil.packageResult(result);
         }
        return result;

    }

    @Override
    public String getquahi(String stationType,String dataType, String obsveName, String startTime, String endTime, String stationNum,String city,String cnty) {
        String result="";
        String SQLTableName="";
        String stationTable="";
        int dnum=0;
        if(stationType.equals("国家站")){
            stationTable="meto_surf_aws_info";
            if (dataType.equals("时") )
            {
                SQLTableName = "surf_aws_hour_data";
                dnum=14;
            }
            else if (dataType.equals("日") )
            {
                SQLTableName = "surf_aws_day_data";
                dnum=11;
            }
            else if (dataType.equals("月"))
            {
                SQLTableName = "surf_aws_month_data";
                dnum=8;

            }else if(dataType.equals("年") ){
                SQLTableName = "surf_aws_year_data";
                dnum=5;
            }
        }else if(stationType.equals("区域站")){
            if (dataType.equals("时") )
            {
                SQLTableName = "surf_reg_hour_data";
                dnum=14;
            }
            else if (dataType.equals("日") )
            {
                SQLTableName = "surf_reg_day_data";
                dnum=11;
            }
            else if (dataType.equals("月"))
            {
                SQLTableName = "surf_reg_month_data";
                dnum=8;

            }else if(dataType.equals("年") ){
                SQLTableName = "surf_reg_year_data";
                dnum=5;
            }
            stationTable="meto_surf_reg_info";
        }

        StringBuilder sql=new StringBuilder();
        sql.append("select meto.station_name as staname,surf.station_num as stanum,SUBSTRING(CONVERT (varchar(100),observe_date,120),0,"+dnum+") as time,"+obsveName+"  as data from  "+SQLTableName+" surf,"+stationTable+" meto" );
        sql.append(" where  surf.station_num=meto.station_num  and "+obsveName +" is not null  ");
        if(startTime.equals(endTime)&&!startTime.equals("")){
            sql.append(" and surf.observe_date='"+startTime+"'");
        }else {
            sql.append(" and  surf.observe_date>= '"+startTime+"' and  surf.observe_date<='"+endTime+"'" );
        }
        if(!stationNum.equals("")){
            sql.append(" and surf.station_num='"+stationNum+"'");
        }
        if(city!=null&&!city.equals("")&&!city.equals("全部")){
            sql.append(" and meto.city='"+city+"'");
        }
        if(cnty!=null&&!cnty.equals("")&&!cnty.equals("全部")){
            sql.append(" and meto.cnty='"+cnty+"'");
        }
        sql.append(" order by observe_date asc");
        JSONArray json=new JSONArray();
        try {
            json = generalDao.getDataBySql(sql.toString(),new String[]{"staname","stanum","time","data"});
            result=json.toJSONString();
        }catch (Exception e){
            logger.error("DataAvgAndMDaoImpl----getquahi 趋势分析数据查询失败"+e.getMessage());
            result="DataAvgAndMDaoImpl 文件----getquahi  趋势分析数据查询失败"+e.getMessage();
        }
        if(json==null||json.size()==0){
            result=StrUtil.packageResult(result);
        }
        return result;
    }

    public double[] GetFileNiHeCanShuAB(JSONArray date,String X,String Y)
    {

        int arrLength = date.size() - 1;

        double[] arrX = new double[arrLength];
        double[] arrY = new double[arrLength];

        for (int i = 0; i < arrLength; i++)
        {
            if(!date.getJSONObject(i).getString(Y).equals("")){
                arrX[i] = Double.parseDouble(i+"");
                arrY[i] = Double.parseDouble(date.getJSONObject(i).getString(Y));
            }
        }
        double[] arrXiShuAB = GetXiangGuanFenXi(arrX, arrY);
        return arrXiShuAB;
    }

    /// <summary>
    /// 获取X,Y数组的相关性，返回k,b,r
    /// </summary>
    /// <param name="arrX">X数组</param>
    /// <param name="arrY">Y数组</param>
    /// <returns></returns>   y=k*x+b
    public double[] GetXiangGuanFenXi(double[] arrX, double[] arrY)
    {
        if (arrX.length == arrY.length)
        {
            int arrLength = arrX.length;
            double sumX = 0;
            double sumY = 0;
            double sumXY = 0;
            double sumXX = 0;
            double sumYY = 0;
            double[] XY = new double[arrLength];
            double[] XX = new double[arrLength];
            double[] YY = new double[arrLength];
            for (int i = 0; i < arrLength; i++)
            {
                sumX += arrX[i];
                sumY += arrY[i];
                XY[i] = arrX[i] * arrY[i];
                XX[i] = arrX[i] * arrX[i];
                YY[i] = arrY[i] * arrY[i];
            }
            double avgX = sumX / arrLength;
            double avgY = sumY / arrLength;
            for (int i = 0; i < arrLength; i++)
            {
                sumXY += XY[i];
                sumXX += XX[i];
                sumYY += YY[i];
            }

            double k = (arrLength * sumXY - sumX * sumY) / (arrLength * sumXX - sumX * sumX);
            double b = avgY - (k * avgX);
            double r = (arrLength * sumXY - sumX * sumY) / Math.sqrt((arrLength * sumXX - sumX * sumX) * (arrLength * sumYY - sumY * sumY));
            BigDecimal bigK = new BigDecimal(k);
            BigDecimal bigb = new BigDecimal(b);
            BigDecimal bigr = new BigDecimal(r);
            k=bigK.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            b=bigb.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            r=bigr.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            double[] result = { k, b, r };
            return result;
        }
        return null;
    }


}
