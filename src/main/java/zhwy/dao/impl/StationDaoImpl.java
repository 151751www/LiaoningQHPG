package zhwy.dao.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.util.GeneralDaoImpl;
@Component
public class StationDaoImpl {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;

    public String  getCity(String stationType)   {
        String sql;
        String result;
        String [] keys={"cityName"};
        JSONArray array = new JSONArray();

        if(stationType.equals("国家站")){
            sql = "select   DISTINCT(city) as cityName from meto_surf_aws_info ";
        }else if(stationType.equals("区域站")){
            sql = "select   DISTINCT(city) as cityName from meto_surf_reg_info ";
        }else{
            return "台站类型内容填写错误";
        }
        try {
            array.add(new JSONObject().put("全部","全部"));
            array.addAll(generalDao.getDataBySql(sql,keys));
            result=array.toString();
        }catch (Exception e){
            logger.error("市名查询失败"+e.getMessage());
            result="市名查询失败"+e.getMessage();
        }
        return result;
    }


    public String getCounty(String stationType, String city) {
        String sql;
        String result;
        String [] keys = new String[]{"cnty"};
        JSONArray array = new JSONArray();
        String tableName;
        String cityName="";
        if(stationType.equals("国家站")){
            tableName="meto_surf_aws_info";
        }else if(stationType.equals("区域站")){
            tableName="meto_surf_reg_info";
        }else{
            result="台站类型内容填写错误";
            return result;
        }
        if(city!=null &&!city.equals("")&&!city.equals("全部")){
            cityName=" where  city='"+city+"' ";
        }
        sql="select  DISTINCT(cnty) as cnty from "+tableName+cityName ;
        try {
            array.add(new JSONObject().put("县","县"));
            array.addAll(generalDao.getDataBySql(sql,keys));
            result=array.toString();
        }catch (Exception e){
            logger.error("县名查询失败"+e.getMessage());
            result="县名查询失败"+e.getMessage();
        }
        return result;
    }
}
