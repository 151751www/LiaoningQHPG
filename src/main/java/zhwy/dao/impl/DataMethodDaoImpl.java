package zhwy.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.DataMethodDao;
import zhwy.util.GeneralDaoImpl;

import java.util.List;
import java.util.Map;
@Component
public class DataMethodDaoImpl implements DataMethodDao {
    private static Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;

    public List<Map<String,Object>> getXulieYanchangData(String timeType, String beginTime, String endTime, String stationNum, String obsvName){
        String SQLTableName="";
        List<Map<String,Object>> list=null;
        int dnum=0;
        if (timeType.equals("时"))
        {
            SQLTableName = "surf_aws_hour_data";
            beginTime = beginTime + ":00";
            endTime = endTime + ":00";
            dnum=14;
        }
        else if (timeType.equals("日"))
        {
            SQLTableName = "surf_aws_day_data";
            beginTime = beginTime + " 00:00";
            endTime = endTime + " 23:00";
            dnum=11;
        }
        else if (timeType.equals("月"))
        {
            SQLTableName = "surf_aws_month_data";
            beginTime = beginTime + "-01 00:00";
            endTime = endTime + "-01 23:00";
            dnum=8;
        }
        else if (timeType.equals("年"))
        {
            SQLTableName = "surf_aws_year_data";
            beginTime = beginTime + "-01-01 00:00";
            endTime = endTime + "-12-31 23:00";
            dnum=5;
        }
        StringBuilder sql=new StringBuilder();
        sql.append("select SUBSTRING(CONVERT(varchar(20), observe_date, 25),0,"+dnum+") as 时间,"+obsvName+" as 长序列 from "+SQLTableName );
        sql.append(" where station_num='"+stationNum+"' and observe_date>='"+beginTime+"' and observe_date<='"+endTime+"'");
        sql.append(" and "+obsvName+" is not null ");
        sql.append(" order by observe_date asc");
        try {
            list= generalDao.getDataList(sql.toString());
        }catch (Exception e){
            logger.error("查询长历史数据出错 DataMethodDaoImpl----getXulieYanchangData  "+e.getMessage());
        }

        return list;
    }

}
