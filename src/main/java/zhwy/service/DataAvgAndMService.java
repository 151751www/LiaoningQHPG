package zhwy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.impl.DataAvgAndMDaoImpl;

import javax.annotation.Resource;

@Service
@Resource
public class DataAvgAndMService {
    private static Logger logger = LoggerFactory.getLogger(DataAvgAndMService.class);
    @Autowired
    private DataAvgAndMDaoImpl dataAvgAndMDao;

    public String getDataForAvg (String stationType,String dataType,String  key,String value, String StartTime,String EndTime, String city,String cnty){
       String message="";
        try {
            message=dataAvgAndMDao.DataForAvg(stationType,dataType,key,value,StartTime,EndTime,city,cnty);
        }catch (Exception e){
            logger.error("DataAvgAndMService  文件中查询要素平均值错误 ："+e);
            message="DataAvgAndMService  文件中查询要素平均值错误 ";
        }
        return  message;
    }

    public String getDataForM(String stationType, String dataType, String  key,String value, String startTime, String endTime, String city, String cnty) {
        String message="";
        try {
            message=dataAvgAndMDao.DataForM(stationType,dataType,key,value,startTime,endTime,city,cnty);
        }catch (Exception e){
            logger.error("DataAvgAndMService  文件中查询要素极值错误 ："+e);
            message="DataAvgAndMService  文件中查询要素极值错误 ";
        }
        return  message;
    }

    public String getDataForOrig(String stationType, String dataType, String key, String value, String startTime, String endTime, String city, String cnty, Integer page, Integer pagesize) {
        String message="";
        try {
            message=dataAvgAndMDao.getDataForOrig(stationType,dataType,key,value,startTime,endTime,city,cnty,page,pagesize);
        }catch (Exception e){
            logger.error("DataAvgAndMService  文件中查询要素原值错误 ："+e);
            message="DataAvgAndMService  文件中查询要素原值错误 ";
        }
        return  message;
    }
}
