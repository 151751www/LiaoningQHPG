package zhwy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.StationDao;
import zhwy.service.StationService;

import javax.annotation.Resource;

@Service
@Resource
public class StationServiceImpl implements StationService {
    private static Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    @Autowired
    public StationDao stationDao;

    public  String  getCity(String  stationType,String pageType)  {
        String result="";
        try {
            result=stationDao.getCity(stationType,pageType);
        }catch (Exception e){
            logger.error("市名查询失败"+e.getMessage());
            result="市名查询失败"+e.getMessage();
        }
    return result;
    }

    public String getCounty(String stationType, String city,String pageType) {
        String result="";
        try {
            result=stationDao.getCounty(stationType,city,pageType);
        }catch (Exception e){
            logger.error("县名查询失败"+e.getMessage());
            result="县名查询失败"+e.getMessage();
        }
        return result;

    }
    public String getRegStation(String stationType,String city, String cnty,String pageType) {
        String result="";
        try {
            result=stationDao.getStation(stationType,city,cnty,pageType);
        }catch (Exception e){
            logger.error("台站号查询失败"+e.getMessage());
            result="台站号查询失败"+e.getMessage();
        }
        return result;

    }

    @Override
    public String getAreaCode(String areaName, String areaType) {
        String result="";
        try {
            result=stationDao.getAreaCode(areaName,areaType);
        }catch (Exception e){
            logger.error("地区编码查询失败"+e.getMessage());
            result="地区编码查询失败"+e.getMessage();
        }
        return result;
    }

    public String checkFileName(String fileName) throws Exception {
        String result="失败";
        String Name[]=fileName.split("_");
            Boolean  iiiii=stationDao.getstation(Name[0]);
            if(iiiii){
                result="成功";
            }
    return result;
    }

}
