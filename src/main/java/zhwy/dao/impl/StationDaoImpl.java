package zhwy.dao.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.StationDao;
import zhwy.util.GeneralDaoImpl;

import java.util.List;
import java.util.Map;

@Component
public class StationDaoImpl implements StationDao {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;

    public String  getCity(String stationType,String pageType)   {
        String sql;
        String result;
        String [] keys={"name"};
        JSONArray array = new JSONArray();


        if(stationType.equals("国家站")){
            sql = "select   DISTINCT(city) as name from meto_surf_aws_info where  city is not null and  city!='' order by city";
        }else if(stationType.equals("区域站")){
            sql = "select   DISTINCT(city) as name from meto_surf_reg_info  where  city is not null and  city!='' order by city";
        }else{
            return "台站类型内容填写错误";
        }
        try {
            array=generalDao.getDataBySql(sql,keys);
            if("重现期，序列订正延长,产品服务".indexOf(pageType)==-1){
                JSONObject object=new JSONObject();
                object.put("name","全部");
                array.add(0,object);
            }
            result=array.toString();
        }catch (Exception e){
            logger.error("市名查询失败"+e.getMessage());
            result="市名查询失败"+e.getMessage();
        }
        return result;
    }


    public String getCounty(String stationType, String city,String pageType) {
        String sql;
        String result;
        String [] keys = new String[]{"name"};
        JSONArray array ;
        String cityName="";
        String order=" order by cnty";
        if(city!=null &&!city.equals("")&&!city.equals("全部")){
            cityName=" and   city='"+city+"' ";
        }
        if(stationType.equals("国家站")){
            sql="select  DISTINCT(cnty) as name from meto_surf_aws_info where 1=1 and cnty is not null and cnty!='' "+cityName +order;
        }else if(stationType.equals("区域站")){
            sql="select  DISTINCT(cnty) as name from meto_surf_reg_info where 1=1 and cnty is not null and cnty!=''"+cityName +order;
        }else{
            result="台站类型内容填写错误";
            return result;
        }
        try {
            array=generalDao.getDataBySql(sql,keys);
            if("重现期，序列订正延长,产品服务".indexOf(pageType)==-1){
                JSONObject object=new JSONObject();
                object.put("name","全部");
                array.add(0,object);
            }
            result=array.toString();
        }catch (Exception e){
            logger.error("县名查询失败"+e.getMessage());
            result="县名查询失败"+e.getMessage();
        }
        return result;
    }

    public String getStation(String stationType, String city,String cnty,String pageType) {
        String sql;
        String result;
        String [] keys = new String[]{"value","name"};
        JSONArray array = new JSONArray();
        String tableName="";
        if(stationType.equals("国家站")){
            tableName="meto_surf_aws_info";
        }else if(stationType.equals("区域站")){
            tableName="meto_surf_reg_info";
        }
        String cityName="";
        if(city!=null &&!city.equals("")&&!city.equals("全部")){
            cityName+=" and   city='"+city+"' ";
        }
        if(cnty!=null&&!cnty.equals("")&&!cnty.equals("全部")){
            cityName+=" and   cnty='"+cnty+"' ";
        }
        sql="select station_num as value , station_name as name from "+tableName+" where 1=1  "+cityName +" order by station_num";

        try {
            array=generalDao.getDataBySql(sql,keys);
            if("重现期，序列订正延长,产品服务".indexOf(pageType)==-1){
                JSONObject object=new JSONObject();
                object.put("name","全部");
                object.put("value","");
                array.add(0,object);
            }
            result=array.toString();
        }catch (Exception e){
            logger.error("台站名称查询失败"+e.getMessage());
            result="台站名称查询失败"+e.getMessage();
        }
        return result;
    }

    @Override
    public String getAreaCode(String areaName, String areaType) throws Exception {
        String sql="";
        JSONArray array;
        String result;
        if(areaType.equals("市")){
            if(areaName.equals("全部")){
                sql="select SUBSTRING(areacode,1,2)+'0000' as code from meto_surf_aws_info a   GROUP  by SUBSTRING(areacode,1,2)";
            }else{
                sql="select SUBSTRING(areacode,1,4)+'00' as code from meto_surf_aws_info a where a.city='"+areaName+"'  GROUP  by SUBSTRING(areacode,1,4)";
            }
        }else{
            sql="select areacode as code from meto_surf_aws_info a where a.cnty='"+areaName+"'  GROUP  by areacode";
        }
        try {
            array=generalDao.getDataBySql(sql,new String[]{"code"});
            result=array.toString();
        }catch (Exception e){
            logger.error("地区编码查询失败"+e.getMessage());
            result="地区编码查询失败"+e.getMessage();
        }

        return result;
    }

    @Override
    public Boolean getstation(String iiiii)throws Exception {
        String sql="";
        Boolean result= false;

        sql="select station_name  from meto_surf_aws_info a where a.station_num='"+iiiii+"' " +
                "union select station_name  from meto_surf_reg_info a where a.station_num='"+iiiii+"'";
            List<Map<String,Object>> list=generalDao.getDataList(sql);
            if(list.size()>0){
                result=true;
            }
        return result;
    }
}
