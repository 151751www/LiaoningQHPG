package zhwy.dao.impl;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.DataMethodDao;
import zhwy.util.GeneralDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class DataMethodDaoImpl implements DataMethodDao {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;

    public JSONArray  getXulieYanchangData(String stationType, String timeType, String beginTime, String endTime, String stationNum, String obsvName, String name, String tiaojian,String []prarm){
        String SQLTableName="";
        JSONArray rearr = null;       
        int dnum=0;
        if (timeType.equals("时"))
        {
            SQLTableName = "surf_aws_hour_data";
            if(stationType.equals("区域站")){
                SQLTableName = "surf_aws_hour_data";
            }
            beginTime = beginTime + ":00";
            endTime = endTime + ":00";
            dnum=14;
        }
        else if (timeType.equals("日"))
        {
            SQLTableName = "surf_aws_day_data";
            if(stationType.equals("区域站")){
                SQLTableName = "surf_aws_hour_data";
            }
            beginTime = beginTime + " 00:00";
            endTime = endTime + " 23:00";
            dnum=11;
        }
        else if (timeType.equals("月"))
        {
            SQLTableName = "surf_aws_month_data";
            if(stationType.equals("区域站")){
                SQLTableName = "surf_aws_hour_data";
            }
            beginTime = beginTime + "-01 00:00";
            endTime = endTime + "-01 23:00";
            dnum=8;
        }
        else if (timeType.equals("年"))
        {
            SQLTableName = "surf_aws_year_data";
            if(stationType.equals("区域站")){
                SQLTableName = "surf_aws_hour_data";
            }
            beginTime = beginTime + "-01-01 00:00";
            endTime = endTime + "-12-31 23:00";
            dnum=5;
        }
        StringBuilder sql=new StringBuilder();
        sql.append("select SUBSTRING(CONVERT(varchar(20), observe_date, 25),0,"+dnum+") as 时间"+tiaojian+","+obsvName+" as "+name+" from "+SQLTableName );
        sql.append(" where station_num='"+stationNum+"' and observe_date>='"+beginTime+"' and observe_date<='"+endTime+"'");
        sql.append(" and "+obsvName+" is not null ");
        sql.append(" order by observe_date asc");
        try {
             rearr= generalDao.getDataBySql(sql.toString(),prarm);
        }catch (Exception e){
            logger.error("查询长历史数据出错 DataMethodDaoImpl----getXulieYanchangData  "+e.getMessage());
        }

        return rearr;
    }

    public String saveDuanxulie(List<Map<String,Object>> list,String dateType){
        String sql="insert into duanxulie_revised_val(stanum,obsv_time,obsv_name,duanxulie_data,data_type) values(?,?,?,?,?)";
        List<Object[]> objectList=new ArrayList<Object[]>();
        String resulr="";
        Object[] objects;
        for (int i=0;i<list.size();i++){
            objects=new Object[]{list.get(i).get("站号"),list.get(i).get("时间"),list.get(i).get("要素"),list.get(i).get("短序列订正值"),dateType};
            objectList.add(objects);
        }
        try {
            int[] num=  generalDao.updateDate(sql,objectList);
            for (int i=0;i<num.length;i++){
                if(num[i]!=1){
                    resulr+=list.get(i).get("时间")+"数据保存到数据库失败 ";
                }
            }
            if(resulr.equals("")){
                resulr="保存成功";
            }
        }catch (Exception e){
            logger.error("DataAvgAndMDaoImpl----saveDuanxulie 短序列订正数据保存失败 "+e.getMessage());
            e.printStackTrace();
            resulr="保存失败"+e.getMessage();
        }
        return  resulr;
    }

    @Override
    public String getXulieDingZheng(String timeType, String beginTime, String endTime, String stationNum, String obsvName) {
        String result="";
        int dnum=0;
        if (timeType.equals("时"))
        {
            beginTime = beginTime + ":00";
            endTime = endTime + ":00";
            dnum=14;
        }
        else if (timeType.equals("日"))
        {
            beginTime = beginTime + " 00:00";
            endTime = endTime + " 23:00";
            dnum=11;
        }
        else if (timeType.equals("月"))
        {
            beginTime = beginTime + "-01 00:00";
            endTime = endTime + "-01 23:00";
            dnum=8;
        }
        else if (timeType.equals("年"))
        {
            beginTime = beginTime + "-01-01 00:00";
            endTime = endTime + "-12-31 23:00";
            dnum=5;
        }
        String sql="select stanum as 站号,SUBSTRING(CONVERT(varchar(20),obsv_time, 25),0,"+dnum+") as 时间,obsv_name as 要素,duanxulie_data as 短序列订正值 from duanxulie_revised_val where " +
                "stanum='"+stationNum+"' and obsv_time>='"+beginTime+"'and obsv_time<='"+endTime+"'and obsv_name='"+obsvName+"'and data_type='"+timeType+"'";
        try {
            result=generalDao.getDataBySql(sql,new String[]{"站号","时间","要素","短序列订正值"}).toJSONString();
        }catch (Exception e){
            logger.error("DataAvgAndMDaoImpl----getXulieDingZheng 序列订正值查询失败 "+e.getMessage());
            result="查询失败"+e.getMessage();
        }
        return result;
    }

}
